// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.broker;

import jsonbroker.library.broker.server.JavascriptCallbackAdapter;
import jsonbroker.library.broker.server.Service;
import jsonbroker.library.common.auxiliary.Data;

/**
 * 
 * @deprecated use jsonbroker.library.broker.server.BrokerJob
 *
 */
public class BrokerJob extends jsonbroker.library.broker.server.BrokerJob {
	

	public static final String JSON_BROKER_SCHEME = "jsonbroker:";
	public static final String JSON_BROKER_URI_ENCODED_SCHEME = "jsonbroker_uri_encoded:";
	
	public BrokerJob(String jsonRequest,boolean isUriEncoded, Service service, JavascriptCallbackAdapter callbackAdapter) {
		super( jsonRequest, isUriEncoded, service, callbackAdapter );
	}
	
	public BrokerJob(Data jsonRequest,JavascriptCallbackAdapter callbackAdapter) {
		super( jsonRequest, callbackAdapter );
		
	}
	

}
