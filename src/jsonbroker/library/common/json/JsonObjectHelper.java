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

public class JsonObjectHelper {

	
	public static JsonObject buildFromString(String jsonString) {
		
		byte[] rawData = StringHelper.toUtfBytes(jsonString);
		Data data = new Data(rawData);
		JsonInput input = new JsonDataInput(data);
		
        JsonBuilder builder = new JsonBuilder();
        JsonReader.read(input, builder);
        
        JsonObject answer = builder.getObjectDocument();

        return answer;

	}
	
}
