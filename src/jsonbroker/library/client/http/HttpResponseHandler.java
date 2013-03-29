// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.client.http;

import java.util.HashMap;

import jsonbroker.library.common.http.Entity;

public interface HttpResponseHandler {
	
	
	// the entity can be null ... in the case of a http 204 response
	public abstract void handleResponseEntity( HashMap<String, String> headers, Entity responseEntity );

}
