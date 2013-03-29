// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.http.reqest_handler;

import java.util.ArrayList;

import jsonbroker.library.common.log.Log;
import jsonbroker.library.server.http.HttpErrorHelper;
import jsonbroker.library.server.http.RequestHandler;
import jsonbroker.library.server.http.HttpRequest;
import jsonbroker.library.server.http.HttpResponse;

public class RootProcessor implements RequestHandler {

	
	private static final Log log = Log.getLog(RootProcessor.class);

	////////////////////////////////////////////////////////////////////////////
	private ArrayList<RequestHandler> _httpProcessors = new ArrayList<RequestHandler>();
	
	
	////////////////////////////////////////////////////////////////////////////
	private RequestHandler _defaultProcessor;
	
	////////////////////////////////////////////////////////////////////////////
	
	public RootProcessor() {
		_httpProcessors = new ArrayList<RequestHandler>();
	}
	
	public RootProcessor( RequestHandler defaultProcessor ) {
		
		_httpProcessors = new ArrayList<RequestHandler>();
		
		_defaultProcessor = defaultProcessor;
	}
		
	public void addHttpProcessor( RequestHandler httpProcessor ) {
		
		String requestUri = httpProcessor.getProcessorUri();
		log.debug( requestUri, "requestUri" );
		
		_httpProcessors.add( httpProcessor );
	}

	@Override
	public String getProcessorUri() {
		return "/";
	}

	@Override
	public HttpResponse processRequest(HttpRequest request) {
		
		
		String requestUri = request.getRequestUri();
		
		for( RequestHandler httpProcessor : _httpProcessors ) {
			
			String processorUri = httpProcessor.getProcessorUri();
			
			if( 0 == requestUri.indexOf( processorUri ) ) {
				return httpProcessor.processRequest( request );
			}
		}
		
		if( null != _defaultProcessor ) {
			return _defaultProcessor.processRequest( request );
		}
		
		log.errorFormat( "bad requestUri; requestUri = '%s'" , requestUri );
		throw HttpErrorHelper.notFound404FromOriginator( this );
	}

}
