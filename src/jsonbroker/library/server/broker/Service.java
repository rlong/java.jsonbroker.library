// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.broker;

import jsonbroker.library.common.broker.BrokerMessage;

public interface Service {
	
	public BrokerMessage process( BrokerMessage request );
	
	

}
