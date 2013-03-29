// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.broker;

import jsonbroker.library.common.broker.BrokerMessage;
import jsonbroker.library.common.broker.BrokerMessageType;
import jsonbroker.library.common.json.JsonObject;

public class NotificationPoster {
	
	private JavascriptCallbackAdapter _callbackAdapter;
	
	JsonObject _metaData;
	
	String _serviceName;
	
	
	public void postNotification( String notificationName, JsonObject associativeParamaters) {
		
		BrokerMessage notification = new BrokerMessage();

		notification.setMessageType( BrokerMessageType.NOTIFICATION);
		notification.setServiceName( _serviceName );
		notification.setMethodName( notificationName);
		
		if( null == associativeParamaters ) {
			
			associativeParamaters = new JsonObject();
			
		}
		
		notification.setAssociativeParamaters( associativeParamaters );

		_callbackAdapter.onNotification( notification);
		
	}
	

}
