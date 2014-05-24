// Copyright (c) 2014 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.client.http;

import java.util.HashMap;

import jsonbroker.library.common.http.Entity;

public class HttpRequestAdapter {

	////////////////////////////////////////////////////////////////////////////
	String _requestUri;
	

	public String getRequestUri() {
		return _requestUri;
	}

	public void setRequestUri(String requestUri) {
		_requestUri = requestUri;
	}

	////////////////////////////////////////////////////////////////////////////
	HashMap<String, String> _requestHeaders;

	public HashMap<String, String> getRequestHeaders() {
		return _requestHeaders;
	}
	
	////////////////////////////////////////////////////////////////////////////
	Entity _requestEntity;


	public Entity getRequestEntity() {
		return _requestEntity;
	}

	public void setRequestEntity(Entity requestEntity) {
		_requestEntity = requestEntity;
	}

	////////////////////////////////////////////////////////////////////////////
	public HttpRequestAdapter( String requestUri ) {
		
		_requestUri = requestUri;
		_requestHeaders = new HashMap<String, String>();
	}
	
	////////////////////////////////////////////////////////////////////////////
	
}
