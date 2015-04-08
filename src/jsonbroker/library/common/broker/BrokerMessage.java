// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.broker;

import jsonbroker.library.common.json.JsonArray;
import jsonbroker.library.common.json.JsonObject;


/**
 * 
 * @deprecated use jsonbroker.library.broker.BrokerMessage
 *
 */
public class BrokerMessage extends jsonbroker.library.broker.BrokerMessage {
	
	///////////////////////////////////////////////////////////////////////
	public BrokerMessage() {
		super();
	}
	
	
	///////////////////////////////////////////////////////////////////////
	public BrokerMessage(JsonArray values) {
		
		super( values );
	}	
	
	public static BrokerMessage buildRequest( String serviceName, String methodName ) {

		BrokerMessage answer = new BrokerMessage();
		
		answer._messageType = BrokerMessageType.REQUEST;
		answer._serviceName = serviceName;
		answer._methodName = methodName;

		return answer;
	}
	
	
	public static BrokerMessage buildMetaRequest( String serviceName, String methodName ) {

		BrokerMessage answer = new BrokerMessage();
		
		answer._messageType = BrokerMessageType.META_REQUEST;
		answer._serviceName = serviceName;
		answer._methodName = methodName;

		return answer;
	}
	
	public static BrokerMessage buildFault( BrokerMessage request, Throwable t ) {

		BrokerMessage answer = new BrokerMessage();
		
		answer._messageType = BrokerMessageType.FAULT;
		answer._metaData = request.getMetaData();
		answer._serviceName = request._serviceName;
		answer._methodName = request._methodName;
		answer._associativeParamaters = FaultSerializer.toJsonObject( t );
		answer._orderedParamaters = new JsonArray(0);
		
		return answer;			

	}

	public static BrokerMessage buildMetaResponse( BrokerMessage request) {
		
		BrokerMessage answer = new BrokerMessage();
		
		answer._messageType = BrokerMessageType.META_RESPONSE;
		answer._metaData = request.getMetaData();
		answer._serviceName = request._serviceName;
		answer._methodName = request._methodName;
		answer._associativeParamaters = new JsonObject();
		answer._orderedParamaters = new JsonArray(0);
		
		return answer;			
	}

	public static BrokerMessage buildResponse( BrokerMessage request) {
				
		BrokerMessage answer = new BrokerMessage();
		
		answer._messageType = BrokerMessageType.RESPONSE;
		answer._metaData = request.getMetaData();
		answer._serviceName = request._serviceName;
		answer._methodName = request._methodName;
		answer._associativeParamaters = new JsonObject();
		answer._orderedParamaters = new JsonArray(0);
		
		return answer;			
	}
	

}
