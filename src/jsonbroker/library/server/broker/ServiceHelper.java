// Copyright (c) 2014 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.broker;

import jsonbroker.library.broker.BrokerMessage;
import jsonbroker.library.common.exception.BaseException;


/**
 * 
 * @deprecated use jsonbroker.library.broker.server.ServiceHelper
 *
 */
public class ServiceHelper {
	
	
	
	public static BaseException methodNotFound( Object originator, BrokerMessage request ) {
		
		return jsonbroker.library.broker.server.ServiceHelper.methodNotFound( originator, request);
		
	}
}
