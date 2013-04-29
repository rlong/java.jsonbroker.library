// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import jsonbroker.library.common.auxiliary.Data;
import jsonbroker.library.common.auxiliary.StringHelper;
import jsonbroker.library.common.json.input.JsonDataInput;
import jsonbroker.library.common.json.input.JsonInput;
import jsonbroker.library.common.json.input.JsonReader;
import jsonbroker.library.common.json.output.JsonStreamOutput;
import jsonbroker.library.common.json.output.JsonStringOutput;
import jsonbroker.library.common.json.output.JsonWriter;

public class JsonObjectHelper {

	
	public static JsonObject fromString(String jsonString) {
		
		byte[] rawData = StringHelper.toUtfBytes(jsonString);
		Data data = new Data(rawData);
		JsonInput input = new JsonDataInput(data);
		
        JsonBuilder builder = new JsonBuilder();
        JsonReader.read(input, builder);
        
        JsonObject answer = builder.getObjectDocument();

        return answer;

	}
	
	public static String toString( JsonObject jsonObject ) {
		
		JsonStringOutput output = new JsonStringOutput();
		JsonWriter writer = new JsonWriter(output);
		JsonWalker.walk( jsonObject, writer);
		return  output.toString();

	}
	
	public static byte[] toBytes( JsonObject jsonObject ) {
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		write( jsonObject, byteArrayOutputStream );
		return byteArrayOutputStream.toByteArray();
	}

	public static void write( JsonObject jsonObject, OutputStream destination ) {

		JsonStreamOutput jsonStreamOutput = new JsonStreamOutput( destination );
		JsonWriter writer = new JsonWriter(jsonStreamOutput);
		JsonWalker.walk( jsonObject, writer);
		jsonStreamOutput.flush();
	}
	
}
