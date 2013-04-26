// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.http.reqest_handler;

import java.util.HashMap;

import jsonbroker.library.common.log.Log;
import jsonbroker.library.server.http.HttpErrorHelper;
import jsonbroker.library.server.http.RequestHandler;
import jsonbroker.library.server.http.HttpRequest;
import jsonbroker.library.server.http.HttpResponse;

public class OpenRequestHandler implements RequestHandler {

	private static final Log log = Log.getLog(OpenRequestHandler.class);
	
	public static final String REQUEST_URI = "/_dynamic_/open";
	
	private HashMap<String, RequestHandler> _processors;
	
	
	public OpenRequestHandler() {
		
		_processors = new HashMap<String, RequestHandler>();
		
	}
		
	public void addRequestHandler( RequestHandler processor ) {
		
		String requestUri = REQUEST_URI + processor.getProcessorUri();
		log.debug( requestUri, "requestUri" );
		_processors.put( requestUri, processor );
	}
	
	public void removeRequestHandler( RequestHandler processor ) {
		
		String requestUri = REQUEST_URI + processor.getProcessorUri();
		log.info( requestUri, "requestUri" );
		_processors.remove( requestUri );
	}
	
	
	
	private RequestHandler getRequestHandler( String requestUri ) {
	
		log.debug( requestUri, "requestUri" );
		
		int indexOfQuestionMark = requestUri.indexOf( '?' );
		if( -1 != indexOfQuestionMark ) {
			requestUri = requestUri.substring( 0, indexOfQuestionMark);
		}
		
		RequestHandler answer = _processors.get( requestUri );
		return answer;

	}

	@Override
	public String getProcessorUri() {
		return REQUEST_URI;
	}


	@Override
	public HttpResponse processRequest(HttpRequest request) {
		
		String requestUri = request.getRequestUri();
		RequestHandler httpProcessor = getRequestHandler( requestUri );

		if( null == httpProcessor ) {
			log.errorFormat( "null == httpProcessor; requestUri = %s" , requestUri);
			throw HttpErrorHelper.notFound404FromOriginator(this);
		}
		return httpProcessor.processRequest( request );
	}

}
