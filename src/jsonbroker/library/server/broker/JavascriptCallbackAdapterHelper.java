// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.broker;

import jsonbroker.library.broker.BrokerMessage;

/**
 * 
 * @deprecated use jsonbroker.library.broker.server.JavascriptCallbackAdapterHelper
 *
 */
public class JavascriptCallbackAdapterHelper extends jsonbroker.library.broker.server.JavascriptCallbackAdapterHelper {


	public static String buildJavascriptFault(BrokerMessage request, Throwable fault) {

		return jsonbroker.library.broker.server.JavascriptCallbackAdapterHelper.buildJavascriptFault( request, fault);
	}
	
	public static String buildJavascriptResponse(BrokerMessage response) {

		return jsonbroker.library.broker.server.JavascriptCallbackAdapterHelper.buildJavascriptResponse( response );

	}

	public static String buildJavascriptNotification(BrokerMessage notification) {

		return jsonbroker.library.broker.server.JavascriptCallbackAdapterHelper.buildJavascriptNotification( notification );

	}

}
