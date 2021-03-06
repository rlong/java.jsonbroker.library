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
import jsonbroker.library.common.json.input.JsonInputHelper;
import jsonbroker.library.common.json.output.JsonStringOutput;



/**
 * 
 * @deprecated use jsonbroker.library.broker.Serializer
 *
 */
public class Serializer  {

	
	public static BrokerMessage deserialize( Data data  ) {
		
		JsonDataInput jsonInput = new JsonDataInput(data);
		
		JsonInputHelper.scanToNextToken( jsonInput ); 
		
		
		JsonArray messageComponents;
		try {
			messageComponents = JsonArrayHandler.readJsonArray( jsonInput );
		} catch( BaseException exception ) {
			exception.addContext( "Serializer.dataOffset", jsonInput.getCursor());
			throw exception;
		}
				
		BrokerMessage answer = new BrokerMessage(messageComponents);
		
		return answer;		
		
	}
	
	public static Data serialize( BrokerMessage message ) {
		
		return jsonbroker.library.broker.Serializer.serialize( message );
		
	}

}
