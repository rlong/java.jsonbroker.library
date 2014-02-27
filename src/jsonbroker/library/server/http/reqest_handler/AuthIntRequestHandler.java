// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.http.reqest_handler;

import java.util.HashMap;

import jsonbroker.library.common.auxiliary.Data;
import jsonbroker.library.common.http.DataEntity;
import jsonbroker.library.common.http.Entity;
import jsonbroker.library.common.http.headers.HttpHeader;
import jsonbroker.library.common.http.headers.request.Authorization;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.server.http.HttpErrorHelper;
import jsonbroker.library.server.http.HttpRequest;
import jsonbroker.library.server.http.HttpResponse;
import jsonbroker.library.server.http.RequestHandler;
import jsonbroker.library.server.http.security.HttpSecurityManager;

public class AuthIntRequestHandler implements RequestHandler {
	
	private static final Log log = Log.getLog(AuthIntRequestHandler.class);
	
	private static final String REQUEST_URI = "/_dynamic_/auth-int";
	

	////////////////////////////////////////////////////////////////////////////
	
	private HashMap<String, RequestHandler> _processors;


	////////////////////////////////////////////////////////////////////////////	
	private HttpSecurityManager _securityManager;
	
	
	
	public AuthIntRequestHandler( HttpSecurityManager securityManager ) {
		
		_processors = new HashMap<String, RequestHandler>();
		_securityManager = securityManager;
	}

	////////////////////////////////////////////////////////////////////////////
	
	public void addHttpProcessor( RequestHandler processor ) {
		
		String requestUri = REQUEST_URI + processor.getProcessorUri();
		log.info( requestUri, "requestUri" );
		_processors.put( requestUri, processor );
	}
	
	
	private RequestHandler getHttpProcessor( String requestUri ) {
	
		log.debug( requestUri, "requestUri" );
		
		int indexOfQuestionMark = requestUri.indexOf( '?' );
		if( -1 != requestUri.indexOf( '?' ) ) {
			requestUri = requestUri.substring( 0, indexOfQuestionMark);
		}
		
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
    		log.error( "null == authorization");
    		throw HttpErrorHelper.unauthorized401FromOriginator(this);
    	}
    	answer = Authorization.buildFromString( authorization );

		if( !"auth-int".equals( answer.getQop() ) ) {
			log.errorFormat( "!\"auth-int\".equals( answer.getQop() ); answer.getQop() = '%s'", answer.getQop());
    		throw HttpErrorHelper.unauthorized401FromOriginator(this);
		}
    	
    	return answer;
		
	}
	
	
	// can return null
	private static DataEntity toDataEntity( Entity entity ) {
		
		if( null == entity ) {
			return null;
		}
		
		if( entity instanceof DataEntity ) {
			return (DataEntity)entity; 
		}
		
		// limit of 64K
		if (entity.getContentLength() > 64 * 1024) {
			
			log.errorFormat( "entity.getContentLength() > 64 * 1024; entity.getContentLength() = %d", entity.getContentLength());
			
			throw HttpErrorHelper.requestEntityTooLarge413FromOriginator(AuthIntRequestHandler.class);
		}
		Data data = new Data( entity.getContent(), (int)entity.getContentLength());
		DataEntity answer = new DataEntity( data );
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

		DataEntity entity = toDataEntity( request.getEntity() );
		
		// we *may* have just consumed the entity data from a stream ... we need to reset the entity in the request
		request.setEntity( entity );
		
		HttpResponse answer = null;
		Authorization authorization = null;
		
		try {
			
			authorization = getAuthorizationRequestHeader( request );
			_securityManager.authenticateRequest( request.getMethod().getName(), authorization, entity);

			answer = httpProcessor.processRequest( request );
			return answer;
			
		} catch( Throwable t ) {
			log.warn( t.getMessage() );
			answer = HttpErrorHelper.toHttpResponse( t );
			return answer;
		} finally {
			HttpHeader header = _securityManager.getHeaderForResponse( authorization, answer.getStatus(), answer.getEntity() );
			answer.putHeader( header.getName(), header.getValue() );
		}		
	}

}
