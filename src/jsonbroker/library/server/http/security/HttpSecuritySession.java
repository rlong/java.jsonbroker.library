// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.http.security;

import java.util.HashMap;

import jsonbroker.library.common.auxiliary.Data;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.http.DataEntity;
import jsonbroker.library.common.http.Entity;
import jsonbroker.library.common.http.headers.request.Authorization;
import jsonbroker.library.common.http.headers.response.AuthenticationInfo;
import jsonbroker.library.common.http.headers.response.WwwAuthenticate;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.common.security.SecurityUtilities;
import jsonbroker.library.common.security.Subject;
import jsonbroker.library.server.http.HttpErrorHelper;

public class HttpSecuritySession {

	
	private static final Log log = Log.getLog(HttpSecuritySession.class);
	
	
	///////////////////////////////////////////////////////////////////////
    private String _usersRealm;

    ///////////////////////////////////////////////////////////////////////
    String _cnonce;

    ///////////////////////////////////////////////////////////////////////
    HashMap<String, String> _cnoncesUsed;

    ///////////////////////////////////////////////////////////////////////
    long _nc;
    
    ///////////////////////////////////////////////////////////////////////
    String _nonce;

    
    ///////////////////////////////////////////////////////////////////////
    String _opaque;

    
    protected String getOpaque() {
		return _opaque;
	}

	///////////////////////////////////////////////////////////////////////
    Subject _registeredSubject;

    
    protected Subject getRegisteredSubject() {
		return _registeredSubject;
	}



	protected void setRegisteredSubject(Subject registeredSubject) {
		_registeredSubject = registeredSubject;
	}

	///////////////////////////////////////////////////////////////////////
    long _idleSince;
    
    ///////////////////////////////////////////////////////////////////////

    public HttpSecuritySession( String usersRealm )
    {
    	
    	_usersRealm = usersRealm;
    	
    	_idleSince = System.currentTimeMillis();
        _nonce = SecurityUtilities.generateNonce();
        _cnoncesUsed = new HashMap<String, String>();
        _opaque = SecurityUtilities.generateNonce();
    }


    // section 3.2.2.3 of RFC-2671
    // entity can be null
    public static String getHa2( String method, String requestUri, Entity entity ) {
    	

    	String entityBodyHash;
    	if( null == entity ) {
    		byte[] emptyEntity = {};
    		entityBodyHash = SecurityUtilities.md5HashOfBytes( emptyEntity );
    	} else {
    		if( !(entity instanceof DataEntity) ) {
    			String technicalError = String.format( "!(entity instanceof DataEntity); entity.getClass().getName() = '%s'", entity.getClass().getName());
    			throw new BaseException( HttpSecuritySession.class, technicalError);
    		}
    		DataEntity dataEntity = (DataEntity)entity;
    		Data data = dataEntity.getData();
    		entityBodyHash = SecurityUtilities.md5HashOfData( data );
    	}
    	
        log.debug(entityBodyHash, "entityBodyHash");
        
        String a2 = String.format( "%s:%s:%s", method, requestUri, entityBodyHash);
        log.debug(a2, "a2");

        String ha2 = SecurityUtilities.md5HashOfString(a2);
        log.debug(ha2, "ha2");
        return ha2;
    }
    
    
    // section 3.2.2.3 of RFC-2671
    public static String getHa2( String method, String requestUri ) {
    	    	
    	String a2 = String.format( "%s:%s", method, requestUri );
        log.debug(a2, "a2");

        String ha2 = SecurityUtilities.md5HashOfString(a2);
        log.debug(ha2, "ha2");
        
        return ha2;
    }

    
    // entity can be null
    public static String getHa2( String method, String qop, String requestUri, Entity entity ) {

        String ha2;
        
        if( "auth".equals( qop ) ) {
        	ha2 = getHa2( method, requestUri );
        } else if( "auth-int".equals( qop ) ) {
        	ha2 = getHa2(method, requestUri, entity);
        } else {
        	log.errorFormat( "unhandled qop; qop = '%s'", qop);            	
        	throw HttpErrorHelper.unauthorized401FromOriginator(HttpSecuritySession.class);
        }
        
        return ha2;
    }

    
    private void validateAuthorization( Authorization authorization ) {
    	
    	if( null == authorization ) {
    		log.error( "null == authorization" );
            throw HttpErrorHelper.unauthorized401FromOriginator(this);
    	}
        if (null == _registeredSubject) // should not happen, but ... 
        {
        	log.error( "null == _registeredSubject" );
            throw HttpErrorHelper.unauthorized401FromOriginator(this);
        }

        // username & realm ... 
        _registeredSubject.validateAuthorizationRequestHeader( authorization);

        // cnonce ... 
        String submittedCnonce = authorization.getCnonce();

        if (null == submittedCnonce)
        {
        	log.error( "null == submittedCnonce" );
            throw HttpErrorHelper.unauthorized401FromOriginator(this);
        }

        // cnonce & nc ... 
        if (submittedCnonce.equals(_cnonce)) // client is re-using cnonce
        {
            if ((_nc + 1) != authorization.getNc())
            {
            	log.errorFormat( "(_nc + 1) != authorizationRequestHeader.getNc(); _nc = %d; authorizationRequestHeader.getNc() = %d", _nc, authorization.getNc() );
                throw HttpErrorHelper.unauthorized401FromOriginator(this);
            }

        }
        else // client has a new cnonce
        {
            if (1 != authorization.getNc())
            {
            	log.errorFormat( "1 != authorizationRequestHeader.getNc(); authorizationRequestHeader.getNc() = %d", authorization.getNc() );
                throw HttpErrorHelper.unauthorized401FromOriginator(this);
            }
            // try detect a replay attack ... 
            if (_cnoncesUsed.containsKey(submittedCnonce))
            {
            	log.errorFormat( "_cnoncesUsed.containsKey(submittedCnonce); submittedCnonce = '%s'", submittedCnonce );
                throw HttpErrorHelper.unauthorized401FromOriginator(this);
            }
        }

        // nonce ... 	
        if (null == authorization.getNonce())
        {
        	log.errorFormat( "null == authorizationRequestHeader.getNonce()" );
            throw HttpErrorHelper.unauthorized401FromOriginator(this);
        }

        if (!_nonce.equals(authorization.getNonce()))
        {
        	log.errorFormat( "!_nonce.equals(authorizationRequestHeader.getNonce()); _nonce = %d; authorizationRequestHeader.getNonce() = %d", _nonce, authorization.getNonce() );
            throw HttpErrorHelper.unauthorized401FromOriginator(this);
        }

        // opaque ... 	
        if (!_opaque.equals(authorization.getOpaque())) // should not happen but ... 
        {
        	log.errorFormat( "!_opaque.equals(authorizationRequestHeader.getOpaque()); _opaque = '%s'; authorizationRequestHeader.getOpaque() = '%s'", _opaque, authorization.getOpaque() );
            throw HttpErrorHelper.unauthorized401FromOriginator(this);
        }

        // qop ... 
        // from section 3.1.2 of RFC-2617 ... in relation to qop-options ... 
        // "Unrecognized options MUST be ignored."
        if (null == authorization.getQop())
        {
        	log.error( "null == authorizationRequestHeader.getQop()");
            throw HttpErrorHelper.unauthorized401FromOriginator(this);
        }
        
        if( ("auth".equals(authorization.getQop()) || "auth-int".equals(authorization.getQop())) ) {
        	// ok 
        } else {
        	
        	log.errorFormat( "unsupported qop; authorization.getQop() = '%s'", authorization.getQop());
        	throw HttpErrorHelper.unauthorized401FromOriginator(this);
        }
        
        // response 
        if (null == authorization.getResponse())
        {
        	log.error( "null == authorizationRequestHeader.getResponse()");
            throw HttpErrorHelper.unauthorized401FromOriginator(this);
        }

        // uri 
        if (null == authorization.getUri())
        {
        	log.error( "null == authorizationRequestHeader.getUri()");
            throw HttpErrorHelper.unauthorized401FromOriginator(this);
        }

    }

    // this method has no effect on the state of 'self' ...
    // sections 3.2.2.1-3.2.2.3 of RFC-2617
    // entity can be null
    public void authorise(String method, Authorization authorization, Entity entity)
    {
    	this.validateAuthorization( authorization );
    	
    	{
    		
            String ha1 = _registeredSubject.getHa1();
            log.debug(ha1, "ha1");
            
            String ha2 = getHa2( method, authorization.getQop(), authorization.getUri(), entity ); 
            
            String unhashedResponse = String.format("%s:%s:%08x:%s:%s:%s",
                    ha1, authorization.getNonce(), authorization.getNc(),
                    authorization.getCnonce(), authorization.getQop(), ha2);

            String expectedResponse = SecurityUtilities.md5HashOfString(unhashedResponse);

            if (!expectedResponse.equals(authorization.getResponse()))
            {
            	log.errorFormat( "!expectedResponse.equals(authorizationRequestHeader.getResponse()); expectedResponse = '%s'; authorizationRequestHeader.getResponse() = '%s'", expectedResponse, authorization.getResponse() );
                throw HttpErrorHelper.unauthorized401FromOriginator(this);

            }
            log.debug(expectedResponse, "expectedResponse");

    	}
        _idleSince = System.currentTimeMillis();

    }

    public void authorise(String method, Authorization authorization )
    {
    	authorise(method, authorization, null );

    }


    //pre: authoriseRequest returned without an exception being thrown
    // this is the method that updates our state
    public void updateUsingAuthenticatedAuthorization( Authorization authorizationRequestHeader ) {


        String cnonce = authorizationRequestHeader.getCnonce();
        _cnonce = authorizationRequestHeader.getCnonce();

        _cnoncesUsed.put( cnonce, cnonce);

        _nc = authorizationRequestHeader.getNc();

        _nonce = SecurityUtilities.generateNonce();

    }

    public WwwAuthenticate buildWwwAuthenticate()
    {
        WwwAuthenticate answer = new WwwAuthenticate();

        answer.setNonce( _nonce );
        answer.setOpaque( _opaque );
        answer.setRealm( _usersRealm );

        return answer;
    }

    
    public AuthenticationInfo buildAuthenticationInfo(Authorization authorization, Entity responseEntity)
    {
        log.enteredMethod();

        AuthenticationInfo answer = new AuthenticationInfo();

        answer.setCnonce( _cnonce );
        answer.setNc( _nc );
        answer.setNextnonce( _nonce );
        answer.setQop( authorization.getQop() );

        /*
         * rspauth field
         */
        String ha1 = _registeredSubject.getHa1();
        
        // from RFC-2617, section 3.2.3, we leave the method out ...   
        String ha2 = getHa2( "", authorization.getQop(), authorization.getUri(), responseEntity);

        String unhashedRspauth = String.format("%s:%s:%08x:%s:%s:%s",
        		ha1, authorization.getNonce(), authorization.getNc(),
        		authorization.getCnonce(), authorization.getQop(), ha2);

        String rspauth = SecurityUtilities.md5HashOfString(unhashedRspauth);
        answer.setRspauth( rspauth );

        return answer;

    }

    public long idleTime()
    {
    	long now = System.currentTimeMillis();

    	long idleMillis = now - _idleSince;

    	long idleSeconds = idleMillis / 1000;

        return idleSeconds;
    }




    

}
