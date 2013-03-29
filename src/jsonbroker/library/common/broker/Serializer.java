// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.broker;

import jsonbroker.library.common.auxiliary.Data;
import jsonbroker.library.common.auxiliary.StringHelper;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.json.JsonArray;
import jsonbroker.library.common.json.handlers.JsonArrayHandler;
import jsonbroker.library.common.json.input.JsonDataInput;
import jsonbroker.library.common.json.output.JsonStringOutput;




public class Serializer  {

	
	public static BrokerMessage deserialize( Data data  ) {
		
		JsonDataInput reader = new JsonDataInput(data);
		
		reader.scanToNextToken();
		
		JsonArray messageComponents;
		try {
			messageComponents = JsonArrayHandler.readJsonArray( reader );
		} catch( BaseException exception ) {
			exception.addContext( "Serializer.dataOffset", reader.getCursor());
			throw exception;
		}
				
		BrokerMessage answer = new BrokerMessage(messageComponents);
		
		return answer;		
		
	}
	
	public static Data serialize( BrokerMessage message ) {
		
		JsonStringOutput writer = new JsonStringOutput();
		
		JsonArray messageComponents = message.toJsonArray();
		JsonArrayHandler.writeJsonArray( messageComponents, writer);
		
		String json = writer.toString();
		byte[] jsonBytes = StringHelper.toUtfBytes( json);
		
		Data answer = new Data(jsonBytes);
		return answer;
		
	}
	

}
