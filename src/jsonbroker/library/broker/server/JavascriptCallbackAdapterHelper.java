// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.broker.server;

import jsonbroker.library.broker.BrokerMessage;
import jsonbroker.library.broker.FaultSerializer;
import jsonbroker.library.common.json.JsonArray;
import jsonbroker.library.common.json.handlers.JsonHandler;
import jsonbroker.library.common.json.handlers.JsonObjectHandler;
import jsonbroker.library.common.json.output.JsonStringOutput;
import jsonbroker.library.common.log.Log;

public class JavascriptCallbackAdapterHelper {

	private static Log log = Log.getLog( JavascriptCallbackAdapterHelper.class );

	public static String buildJavascriptFault(BrokerMessage request, Throwable fault) {

		log.enteredMethod();
		
		JsonStringOutput jsonWriter = new JsonStringOutput();
		jsonWriter.append("client.ClientBroker.forwardFault(\"fault\",");
		
		JsonObjectHandler jsonObjectHandler = JsonObjectHandler.getInstance();
		jsonObjectHandler.writeValue( request.getMetaData(), jsonWriter);
		jsonWriter.append(",\"");
		jsonWriter.append( request.getServiceName() );
		jsonWriter.append("\",1,0,\"");
		jsonWriter.append( request.getMethodName() );
		jsonWriter.append("\",");
		jsonObjectHandler.writeValue( FaultSerializer.toJsonObject(fault), jsonWriter );
		jsonWriter.append(");");

		String answer = jsonWriter.toString();
		
		log.debug( answer, "answer");
		
		return answer;
	}
	
	public static String buildJavascriptResponse(BrokerMessage response) {
		
		//log.enteredMethod();
		
		JsonStringOutput jsonWriter = new JsonStringOutput();
		jsonWriter.append("client.ClientBroker.forwardResponse(\"response\",");
		JsonObjectHandler jsonObjectHandler = JsonObjectHandler.getInstance();
		jsonObjectHandler.writeValue( response.getMetaData(), jsonWriter);
		jsonWriter.append(",\"");
		jsonWriter.append( response.getServiceName() );
		jsonWriter.append("\",1,0,\"");
		jsonWriter.append( response.getMethodName() );
		jsonWriter.append("\",");
		jsonObjectHandler.writeValue( response.getAssociativeParamaters(), jsonWriter );
		JsonArray parameters = response.getOrderedParamaters();
		
		for( int i = 0, count = parameters.size(); i < count; i++ ) {
			jsonWriter.append( ',');
			Object blob = parameters.getObject( i );
			JsonHandler handler = JsonHandler.getHandler( blob );
			handler.writeValue( blob, jsonWriter);			
		}
		
		jsonWriter.append(");");
		
		String answer = jsonWriter.toString();
		
		return answer;

	}

	public static String buildJavascriptNotification(BrokerMessage notification) {
		
		log.enteredMethod();
		
		JsonStringOutput jsonWriter = new JsonStringOutput();
		jsonWriter.append("jsonbroker.onNotification(");
		JsonObjectHandler jsonObjectHandler = JsonObjectHandler.getInstance();
		jsonObjectHandler.writeValue( notification.getMetaData(), jsonWriter);
		jsonWriter.append(",\"");
		jsonWriter.append( notification.getServiceName() );
		jsonWriter.append("\",1,0,\"");
		jsonWriter.append( notification.getMethodName() );
		jsonWriter.append("\",");
		jsonObjectHandler.writeValue( notification.getAssociativeParamaters(), jsonWriter );
		JsonArray parameters = notification.getOrderedParamaters();
		
		for( int i = 0, count = parameters.size(); i < count; i++ ) {
			jsonWriter.append( ',');
			Object blob = parameters.getObject( i );
			JsonHandler handler = JsonHandler.getHandler( blob );
			handler.writeValue( blob, jsonWriter);			
		}
		
		jsonWriter.append(");");
		
		String answer = jsonWriter.toString();
		
		log.debug( answer, "answer");

		return answer;

	}

}
