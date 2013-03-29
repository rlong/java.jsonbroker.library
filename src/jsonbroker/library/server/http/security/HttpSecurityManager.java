// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.http.security;

import java.util.ArrayList;
import java.util.HashMap;

import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.http.Entity;
import jsonbroker.library.common.http.HttpStatus;
import jsonbroker.library.common.http.headers.HttpHeader;
import jsonbroker.library.common.http.headers.request.Authorization;
import jsonbroker.library.common.http.headers.response.AuthenticationInfo;
import jsonbroker.library.common.http.headers.response.WwwAuthenticate;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.common.security.SecurityConfiguration;
import jsonbroker.library.common.security.ServerSecurityConfiguration;
import jsonbroker.library.common.security.Subject;
import jsonbroker.library.server.http.HttpErrorHelper;

public class HttpSecurityManager {

	private static final Log log = Log.getLog(HttpSecurityManager.class);
	
    ///////////////////////////////////////////////////////////////////////
	//
	ServerSecurityConfiguration _securityConfiguration;
	
	
    public ServerSecurityConfiguration getSecurityConfiguration() {
		return _securityConfiguration;
	}

	///////////////////////////////////////////////////////////////////////
	//
	HashMap<String, HttpSecuritySession> _unauthenticatedSessions;

    ///////////////////////////////////////////////////////////////////////
	//
	HashMap<String, HttpSecuritySession> _authenticatedSessions;

	
	
    ///////////////////////////////////////////////////////////////////////
    //
    SubjectGroup _unregisteredSubjects;

	public SubjectGroup getUnregisteredSubjects() {
		return _unregisteredSubjects;
	}

    
    ///////////////////////////////////////////////////////////////////////
    //
	public HttpSecurityManager( SecurityConfiguration securityConfiguration)
    {
    	
		_securityConfiguration = securityConfiguration;

    	_unauthenticatedSessions = new HashMap<String,HttpSecuritySession>();
    	_authenticatedSessions = new HashMap<String,HttpSecuritySession>();
        _unregisteredSubjects = new SubjectGroup();

    }
    
    ///////////////////////////////////////////////////////////////////////

	
	private Subject registeredSubjectForAuthorizationRequestHeader(Authorization authorizationRequestHeader) { 
		
		String realm = _securityConfiguration.getRealm();
		String authRealm = authorizationRequestHeader.getRealm();
		
		if( !realm.equals( authRealm ) ) {
    		log.errorFormat( "!realm.equals( authRealm ); realm = '%s'; realm = '%s'" , realm, authRealm);
    		throw HttpErrorHelper.unauthorized401FromOriginator(this);
		}
		
		String clientUsername = authorizationRequestHeader.getUsername();
		Subject client = _securityConfiguration.getClient( clientUsername );
		if( null == client ) {
			log.errorFormat( "null == client; clientUsername = '%s'" , clientUsername);
			throw HttpErrorHelper.unauthorized401FromOriginator(this);
		}
		return client;
		
	}
	

    // entity can be null 
    public void authenticateRequest( String method, Authorization authorizationRequestHeader, Entity entity ) {


        String opaque = authorizationRequestHeader.getOpaque();

        log.debug(opaque, "opaque");

        //if( nil == opaque ) {
        if (null == opaque)
        {        	
        	log.error( "null == opaque" );
        	
            //@throw [HTTPException unauthorized401FromOriginator:self line:__LINE__];
            throw HttpErrorHelper.unauthorized401FromOriginator(this);
        }

        try
        {

            if (_unauthenticatedSessions.containsKey(opaque))
            {
            	HttpSecuritySession securitySession = _unauthenticatedSessions.get(opaque);
                Subject registeredSubject = this.registeredSubjectForAuthorizationRequestHeader(authorizationRequestHeader);
                securitySession.setRegisteredSubject( registeredSubject );
            	securitySession.authorise(method, authorizationRequestHeader, entity);
                securitySession.updateUsingAuthenticatedAuthorization(authorizationRequestHeader);
                _authenticatedSessions.put(opaque, securitySession);
                _unauthenticatedSessions.remove(opaque);

                return;
            }

            if (_authenticatedSessions.containsKey(opaque))
            {
            	HttpSecuritySession securitySession = _authenticatedSessions.get(opaque);
            	securitySession.authorise(method, authorizationRequestHeader, entity);
                securitySession.updateUsingAuthenticatedAuthorization(authorizationRequestHeader);

                return;
            }

            log.errorFormat( "bad opaque; opaque = '%s'", opaque );
            
            // bad opaque value ... 
            throw HttpErrorHelper.unauthorized401FromOriginator(this);

        }
        catch (BaseException e) // if we catch a 401 ... clean up sesssions associated with the opaque
        {
        	if( HttpStatus.UNAUTHORIZED_401 == e.getFaultCode() ) {
        		_unauthenticatedSessions.remove(opaque);
        		_authenticatedSessions.remove(opaque);
        	}            
            // rethrow
            throw e;
        }
    }
    

    public void authenticateRequest( String method, Authorization authorization ) {

    	this.authenticateRequest( method, authorization, null );

    }
    
    public HttpHeader getHeaderForResponse( Authorization authorization, int responseStatusCode, Entity responseEntity ) {
    	
    	if( null == authorization || 401 == responseStatusCode ) {
    		
    		// setup a new unauthenticated security session ...
    		HttpSecuritySession securitySession = new HttpSecuritySession(_securityConfiguration.getRealm());
    		_unauthenticatedSessions.put( securitySession.getOpaque(), securitySession );
    		WwwAuthenticate answer = securitySession.buildWwwAuthenticate();
    		return answer;
    		
    	} else {
            String opaque = authorization.getOpaque();
            HttpSecuritySession securitySession = _authenticatedSessions.get( opaque );
            AuthenticationInfo answer = securitySession.buildAuthenticationInfo(authorization, responseEntity);
            return answer;    		
    	}
    }
    

    public void addUnregisteredSubject(Subject unregisteredSubject)
    {
        log.enteredMethod();
        _unregisteredSubjects.addSubject(unregisteredSubject);
    }
    
    

    public void addRegisteredSubject(Subject registeredSubject)
    {
        log.enteredMethod();
        _securityConfiguration.addClient(registeredSubject);

    }

    public void removeSubject(String userName)
    {
    	
        _unregisteredSubjects.removeSubject(userName);
        
        _securityConfiguration.removeClient( userName );

        ArrayList<String> sessionsForRemoval = new ArrayList<String>(); 

        for( String opaque : _authenticatedSessions.keySet() )
        {
        	HttpSecuritySession authenticatedSession = _authenticatedSessions.get(opaque);

        	Subject authenticatedSubject = authenticatedSession.getRegisteredSubject();

            // authenticated session with the subject that we are removing 
        	if (authenticatedSubject.getUsername().equals(userName))
            {
        		sessionsForRemoval.add(opaque);
            }

        }

        for( String opaque : sessionsForRemoval ) {
            log.info( opaque, "opaque");
            _authenticatedSessions.remove( opaque);
        }
    }


    public void runCleanup()
    {
        log.enteredMethod();

        ArrayList<String> stale = new ArrayList<String>();

        /*
         * unauthenticated sessions 
         */
        for (String opaque : _unauthenticatedSessions.keySet())
        {
        	HttpSecuritySession unauthenticatedSession = _unauthenticatedSessions.get( opaque );

            long idleTime = unauthenticatedSession.idleTime();

            if ((1 * 60) < idleTime)
            {
                stale.add(opaque);

                if (Log.isDebugEnabled())
                {
                	String message = String.format("removing stale unauthenticatedSession session '%s', age = %d", opaque, idleTime);
                    log.debug(message);
                }

            }

        }

        for( String opaque : stale ) 
        {
        	log.debugFormat( "removing stale unauthenticated session %s", opaque );
            _unauthenticatedSessions.remove(opaque);
        }

        stale.clear();

        /*
         * authenticated sessions 
         */

        for(String opaque : _authenticatedSessions.keySet())
        {
        	HttpSecuritySession authenticatedSession = _authenticatedSessions.get(opaque);

            long idleTime = authenticatedSession.idleTime();
            if ((20 * 60) < idleTime)
            {
                stale.add(opaque);

                if (Log.isDebugEnabled())
                {
                    String message = String.format("removing stale authenticated session '%s', age = %d", opaque, idleTime);
                    log.debug(message);
                }

            }
        }

        for( String opaque : stale )
        {
        	log.debugFormat( "removing stale authenticated session %s", opaque );
            _authenticatedSessions.remove(opaque);

        }

        stale.clear();

        /*
         * registration requests 
         */

        
        ArrayList<String> staleRegistrationRequests = new ArrayList<String>();

        long now = System.currentTimeMillis();

        for( Subject unregisteredSubject : _unregisteredSubjects.subjects() )
        {
        	long elapsedMillies = now - unregisteredSubject.getBorn();
        	

            long objectAge = elapsedMillies / 1000;

            if ((5 * 60) < objectAge)
            {
                if (Log.isDebugEnabled())
                {
                	String message = String.format("removing stale registration request for user '%s', age = %d", unregisteredSubject.getUsername(), objectAge);
                    log.debug(message);

                }
                staleRegistrationRequests.add(unregisteredSubject.getUsername());

            }
        }
        for( String unregisteredUsername : staleRegistrationRequests )
        {
            log.debugFormat( "removing stale registraction request for username %s", unregisteredUsername );
            _unregisteredSubjects.removeSubject(unregisteredUsername);
        }

    }

    public boolean subjectHasAuthenticatedSession(Subject target)
    {
    	for(String opaque : _authenticatedSessions.keySet())
        {
            HttpSecuritySession authenticatedSession = _authenticatedSessions.get(opaque);

            Subject candidate = authenticatedSession.getRegisteredSubject();
            if (candidate.getUsername().equals(target.getUsername()))
            {
                return true;
            }
        }

        return false;

    }
    
    
    public Subject approveSubject( String userName ) {
    	Subject approvedSubject = _unregisteredSubjects.getSubject( userName );
    	_securityConfiguration.addClient( approvedSubject );
    	_unregisteredSubjects.removeSubject( userName );
    	return approvedSubject;
    }
    
}
