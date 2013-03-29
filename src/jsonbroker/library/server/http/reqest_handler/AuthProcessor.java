// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.http.reqest_handler;

import java.util.HashMap;

import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.http.headers.HttpHeader;
import jsonbroker.library.common.http.headers.request.Authorization;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.server.http.HttpErrorHelper;
import jsonbroker.library.server.http.RequestHandler;
import jsonbroker.library.server.http.HttpRequest;
import jsonbroker.library.server.http.HttpResponse;
import jsonbroker.library.server.http.security.HttpSecurityManager;

public class AuthProcessor implements RequestHandler {
	
	private static final Log log = Log.getLog(AuthProcessor.class);
	
	
	
	private static final String REQUEST_URI = "/_dynamic_/auth";
	
	private static final String ERROR_DOMAIN_NULL_AUTHORIZATION = "jsonbroker.AuthProcessor.NULL_AUTHORIZATION";
	
	


	////////////////////////////////////////////////////////////////////////////
	
	private HashMap<String, RequestHandler> _processors;


	////////////////////////////////////////////////////////////////////////////	
	private HttpSecurityManager _securityManager;
	
	
	public AuthProcessor( HttpSecurityManager securityManager ) {
		
		
		_processors = new HashMap<String, RequestHandler>();
		_securityManager = securityManager;
	}


	////////////////////////////////////////////////////////////////////////////
	
	
	public void addHttpProcessor( RequestHandler processor ) {
		
		String requestUri = REQUEST_URI + processor.getProcessorUri();
		log.debug( requestUri, "requestUri" );
		_processors.put( requestUri, processor );
	}
	
	
	private RequestHandler getHttpProcessor( String requestUri ) {
	
		
		int indexOfQuestionMark = requestUri.indexOf( '?' );
		if( -1 != requestUri.indexOf( '?' ) ) {
			requestUri = requestUri.substring( 0, indexOfQuestionMark);
		}

		log.debug( requestUri, "requestUri" );

		RequestHandler answer = _processors.get( requestUri );
		return answer;

	}
	
	@Override
	public String getProcessorUri() {
		
		return REQUEST_URI;
	}
	
	
	
	private Authorization getAuthorizationRequestHeader(HttpRequest request) {
		
		Authorization answer = null;
		
    	String authorization = request.getHttpHeader("authorization");
    	if( null == authorization ) {
    		log.warn( "null == authorization");
    		throw HttpErrorHelper.unauthorized401FromOriginator(AuthProcessor.class,ERROR_DOMAIN_NULL_AUTHORIZATION);
    	}
    	answer = Authorization.buildFromString( authorization );

		if( !"auth".equals( answer.getQop() ) ) {
			log.errorFormat( "!\"auth\".equals( answer.getQop() ); answer.getQop() = '%s'", answer.getQop());
    		throw HttpErrorHelper.unauthorized401FromOriginator(AuthProcessor.class);
		}
    	
    	return answer;
		
	}
	
	public HttpResponse processRequest(HttpRequest request) {
		
		
		RequestHandler httpProcessor;
		{
			String requestUri = request.getRequestUri();
			httpProcessor = getHttpProcessor( requestUri );

			if( null == httpProcessor ) {
				log.errorFormat( "null == httpProcessor; requestUri = %s" , requestUri);
				throw HttpErrorHelper.notFound404FromOriginator(this);
			}
		}

		HttpResponse answer = null;
		Authorization authorization = null;
		
		try {
			
			authorization = getAuthorizationRequestHeader( request );
			_securityManager.authenticateRequest( request.getMethod(), authorization );

			answer = httpProcessor.processRequest( request );
			return answer;
			
		} catch( BaseException e ) {
			
			if( ERROR_DOMAIN_NULL_AUTHORIZATION.equals( e.getErrorDomain() ) ) {
				log.warn( e.getMessage() );
			} else {
				log.error( e );
			}

			answer = HttpErrorHelper.toHttpResponse( e );
			return answer;

		} catch( Throwable t ) {
			
			log.error( t );
			answer = HttpErrorHelper.toHttpResponse( t );
			return answer;
			
		} finally {
			
			HttpHeader header = _securityManager.getHeaderForResponse( authorization, answer.getStatus(), answer.getEntity() );
			answer.putHeader( header.getName(), header.getValue() );
		}
	}



}
