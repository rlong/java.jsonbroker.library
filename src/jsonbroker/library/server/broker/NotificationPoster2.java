// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.broker;

import jsonbroker.library.broker.server.JavascriptCallbackAdapter;

/**
 * 
 * @deprecated use jsonbroker.library.broker.server.NotificationPoster2
 *
 */
public class NotificationPoster2 extends jsonbroker.library.broker.server.NotificationPoster2 {
	
	public NotificationPoster2( JavascriptCallbackAdapter callbackAdapter, String serviceName, String methodName ) {
		super( callbackAdapter, serviceName, methodName );
		
	}
	
}
