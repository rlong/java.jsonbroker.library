// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.broker.server;

import jsonbroker.library.broker.BrokerMessage;

public interface Service {
	
	public BrokerMessage process( BrokerMessage request );
	
	

}
