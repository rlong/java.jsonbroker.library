// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.client.http;

import java.util.HashMap;

import jsonbroker.library.common.auxiliary.Data;
import jsonbroker.library.common.http.DataEntity;
import jsonbroker.library.common.http.Entity;
import jsonbroker.library.common.http.headers.request.Authorization;
import jsonbroker.library.common.http.headers.response.AuthenticationInfo;
import jsonbroker.library.common.http.headers.response.WwwAuthenticate;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.common.security.ClientSecurityConfiguration;
import jsonbroker.library.common.security.SecurityUtilities;
import jsonbroker.library.common.security.Subject;

public class Authenticator  {

	
	private static Log log = Log.getLog(Authenticator.class);

    
    ///////////////////////////////////////////////////////////////////////
    //
    String _ha1;
    

    ///////////////////////////////////////////////////////////////////////
    //
    ClientSecurityConfiguration _securityConfiguration;
    
    public ClientSecurityConfiguration getSecurityConfiguration() {
		return _securityConfiguration;
	}

	///////////////////////////////////////////////////////////////////////
    //
    private boolean _authInt = true;

    public boolean authInt() {
		return _authInt;
	}

    ///////////////////////////////////////////////////////////////////////
    //
	Authorization _authorization;

	public Authenticator( boolean authInt, ClientSecurityConfiguration clientSecurityConfiguration ) {
		
		_authInt = authInt;
		_securityConfiguration = clientSecurityConfiguration;
	}
	
    
    
    // can return null
    private String getEntityBodyHash( Entity requestEntity ) {
    	
		if( null == requestEntity ) {
			byte[] emptyEntity = {};
			return SecurityUtilities.md5HashOfBytes( emptyEntity );
		}
		
		if( !(requestEntity instanceof DataEntity) ) {
			log.warnFormat("!(requestEntity instanceof DataEntity); requestEntity.getClass().getName() = '%s'", requestEntity.getClass().getName() );
			return null;
		}
		DataEntity dataEntity = (DataEntity)requestEntity;
		Data data = dataEntity.getData();

    	return SecurityUtilities.md5HashOfData( data );
		

    }
    
    // can return null 
    private String getHa2( String method, String requestUri, Entity requestEntity ) {
    	
    	// RFC-2617 3.2.2.3 A2
    	String a2;
    	
    	if( _authInt ) {
    		
    		String entityBodyHash = getEntityBodyHash( requestEntity );
    		if( null == entityBodyHash ) {
    			return null;
    		}
        	log.debug( entityBodyHash, "entityBodyHash" );
        	
        	a2 = String.format("%s:%s:%s", method, requestUri, entityBodyHash);

    	} else {
    		a2 = String.format("%s:%s", method, requestUri);
    	}
    	
        String ha2 = SecurityUtilities.md5HashOfString(a2);
        log.debug( ha2, "ha2" );
        String answer = ha2;

        return answer;
    	
    }
    
    
	public String getRequestAuthorization(String method, String requestUri, Entity requestEntity) {

		log.enteredMethod();

        if (null == _authorization)
        {
        	log.info("null == _authorization"); 
            return null;
        }
        
        if( null == _ha1 ) { // should not happen, but ... 
        	log.warn("null == _ha1"); 
            return null;
        }
        
        String ha2 = getHa2( method, requestUri, requestEntity);
        if( null == ha2 ) { 
            log.warn("null == ha2");
            return null;
        }

        _authorization.setUri( requestUri );
        
        if( _authInt ) {
        	_authorization.setQop( "auth-int");
        } else {
        	_authorization.setQop( "auth");
        }
        
        
        String response;
        {
        	
        	
            String unhashedResponse = String.format("%s:%s:%08x:%s:%s:%s",
                    _ha1, _authorization.getNonce(), _authorization.getNc(),
                    _authorization.getCnonce(), _authorization.getQop(), ha2);
         
            response = SecurityUtilities.md5HashOfString( unhashedResponse);
        }
        
        _authorization.setResponse( response );
        

        return _authorization.toString();
		
	}
		
	// '[Authenticator handleResponseHeaders:]' is part of a private Apple API
	public void handleHttpResponseHeaders(HashMap<String, String> headers ) {
		
		log.enteredMethod();

    	String wwwAuthenticate = headers.get( "www-authenticate" );
    	if( null != wwwAuthenticate ) {
    		
    		log.warn( wwwAuthenticate, "wwwAuthenticate"  );
    		
    		WwwAuthenticate authenticateResponseHeader = WwwAuthenticate.buildFromString( wwwAuthenticate );
    		
    		String serverRealm = authenticateResponseHeader.getRealm();
    		
    		Subject server = _securityConfiguration.getServer( serverRealm );
    		
    		if( null == server ) {
    			log.warnFormat( "null == _subject; serverRealm = '%s'", serverRealm );
    			_authorization = null;
    			_ha1 = null;
    			return;    			
    		}
    		
    		_ha1 = server.getHa1();
    		
            _authorization = new Authorization();

            _authorization.setNc(1);

            _authorization.setCnonce( SecurityUtilities.generateNonce() );

            _authorization.setNonce( authenticateResponseHeader.getNonce() );

            _authorization.setOpaque( authenticateResponseHeader.getOpaque() );
            
            _authorization.setQop( authenticateResponseHeader.getQop() );

            _authorization.setRealm( server.getRealm() );
            
            _authorization.setUsername( server.getUsername() );

    		return; // our work here is done 
    	} 
    	
    	String authenticationInfo = headers.get( "authentication-info" );
    	if( null != authenticationInfo ) {
    		AuthenticationInfo authenticationInfoHeader = AuthenticationInfo.buildFromString( authenticationInfo );
    		
    		
            long nc = _authorization.getNc() + 1;

            _authorization.setNc( nc );

            _authorization.setNonce( authenticationInfoHeader.getNextnonce());

    		return; // our work here is done
    	}

    	log.warn("did not find a 'WWW-Authenticate' or a 'Authentication-Info'");
		
	}

	

}
