// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//
package jsonbroker.library.common.json;

import jsonbroker.library.common.auxiliary.Data;
import jsonbroker.library.common.auxiliary.StringHelper;
import jsonbroker.library.common.json.input.JsonDataInput;
import jsonbroker.library.common.json.input.JsonInput;
import jsonbroker.library.common.json.input.JsonReader;
import jsonbroker.library.common.json.output.JsonStringOutput;
import jsonbroker.library.common.json.output.JsonWriter;

public class JsonArrayHelper {
	
	
	public static JsonArray buildFromString(String jsonString) {
		
		byte[] rawData = StringHelper.toUtfBytes(jsonString);
		Data data = new Data(rawData);
		JsonInput input = new JsonDataInput(data);
		
        JsonBuilder builder = new JsonBuilder();
        JsonReader.read(input, builder);
        
        JsonArray answer = builder.getArrayDocument();

        return answer;

	}
	
	public static String toString( JsonArray jsonArray ) {
		
		JsonStringOutput output = new JsonStringOutput();
		JsonWriter writer = new JsonWriter(output);
		JsonWalker.walk( jsonArray, writer);
		return  output.toString();

	}

}
