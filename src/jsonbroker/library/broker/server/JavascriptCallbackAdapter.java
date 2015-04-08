// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.broker.server;

import jsonbroker.library.broker.BrokerMessage;

public interface JavascriptCallbackAdapter {
	

	public abstract void onFault( BrokerMessage request, Throwable fault);
	public abstract void onNotification( BrokerMessage notification );
	public abstract void onResponse( BrokerMessage response );

}
