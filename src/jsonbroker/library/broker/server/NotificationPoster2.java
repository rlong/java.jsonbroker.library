// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.broker.server;

import jsonbroker.library.broker.BrokerMessage;
import jsonbroker.library.broker.BrokerMessageType;
import jsonbroker.library.common.log.Log;

public class NotificationPoster2 {
	
	private static final Log log = Log.getLog(NotificationPoster2.class);

	JavascriptCallbackAdapter _callbackAdapter;
	String _serviceName;
	String _methodName; 
	
	public NotificationPoster2( JavascriptCallbackAdapter callbackAdapter, String serviceName, String methodName ) {
		
		_callbackAdapter = callbackAdapter;
		_serviceName = serviceName;
		_methodName = methodName;
		
	}
	
	public void post() {
		
		log.enteredMethod();
		
		BrokerMessage notificaiton = new BrokerMessage();
		notificaiton.setMessageType( BrokerMessageType.NOTIFICATION );
		notificaiton.setServiceName( _serviceName);
		notificaiton.setMethodName( _methodName );
		
		_callbackAdapter.onNotification( notificaiton );
	}

}
