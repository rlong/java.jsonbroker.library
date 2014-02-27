// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import jsonbroker.library.common.auxiliary.Data;
import jsonbroker.library.common.auxiliary.InputStreamHelper;
import jsonbroker.library.common.auxiliary.StringHelper;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.json.input.JsonDataInput;
import jsonbroker.library.common.json.input.JsonInput;
import jsonbroker.library.common.json.input.JsonReader;
import jsonbroker.library.common.json.input.JsonStreamInput;
import jsonbroker.library.common.json.output.JsonStreamOutput;
import jsonbroker.library.common.json.output.JsonStringOutput;
import jsonbroker.library.common.json.output.JsonWriter;

public class JsonArrayHelper {
	
	
	public static JsonArray fromString(String jsonString) {
		
		byte[] rawData = StringHelper.toUtfBytes(jsonString);
		Data data = new Data(rawData);
		JsonInput input = new JsonDataInput(data);
		
        JsonBuilder builder = new JsonBuilder();
        JsonReader.read(input, builder);
        
        JsonArray answer = builder.getArrayDocument();
        return answer;
	}
	
	public static JsonArray read( InputStream inputStream ) {
		
		JsonStreamInput jsonStreamInput = new JsonStreamInput( inputStream );
        JsonBuilder builder = new JsonBuilder();
        JsonReader.read(jsonStreamInput, builder);
        
        JsonArray answer = builder.getArrayDocument();
        return answer;
		
	}
	

	public static JsonArray read( File file ) {
		
		FileInputStream fis = null;
		
		try {
			
			fis = new FileInputStream( file );
			return read( fis );
			
		} catch (FileNotFoundException e) {
			throw new BaseException( JsonArrayHelper.class, e );
		} finally {
			if( null != fis ) {
				InputStreamHelper.close( fis, false, JsonArrayHelper.class);
			}
		}
		
	}

	public static JsonArray read( String path ) {
		
		return read( new File( path ) );
		
	}
	
	public static String toString( JsonArray jsonArray ) {
		
		JsonStringOutput output = new JsonStringOutput();
		JsonWriter writer = new JsonWriter(output);
		JsonWalker.walk( jsonArray, writer);
		return  output.toString();
	}
	
	
	public static byte[] toBytes( JsonArray  jsonArray ) {
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		write( jsonArray, byteArrayOutputStream );
		return byteArrayOutputStream.toByteArray();
	}
	
	public static void write( JsonArray jsonArray, OutputStream destination ) {

		JsonStreamOutput jsonStringOutput = new JsonStreamOutput( destination );
		JsonWriter writer = new JsonWriter(jsonStringOutput);
		JsonWalker.walk( jsonArray, writer);
		jsonStringOutput.flush();

	}


}
