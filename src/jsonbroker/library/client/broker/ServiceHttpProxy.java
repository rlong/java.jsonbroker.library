// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.client.broker;

import jsonbroker.library.client.http.Authenticator;
import jsonbroker.library.client.http.HttpDispatcher;


/**
 * 
 * @deprecated use jsonbroker.library.broker.client.ServiceHttpProxy
 *
 */
public class ServiceHttpProxy extends jsonbroker.library.broker.client.ServiceHttpProxy {

	public ServiceHttpProxy(HttpDispatcher httpDispatcher) {
		super(httpDispatcher);
	}
	
    public ServiceHttpProxy( HttpDispatcher httpDispatcher, Authenticator authenticator ) {
    	super(httpDispatcher,authenticator);
    }
   
}
