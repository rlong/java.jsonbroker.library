// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.StreamHandler;

import jsonbroker.library.common.auxiliary.Data;
import jsonbroker.library.common.auxiliary.InputStreamHelper;
import jsonbroker.library.common.auxiliary.OutputStreamHelper;
import jsonbroker.library.common.auxiliary.StringHelper;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.json.input.JsonDataInput;
import jsonbroker.library.common.json.input.JsonInput;
import jsonbroker.library.common.json.input.JsonReader;
import jsonbroker.library.common.json.input.JsonStreamInput;
import jsonbroker.library.common.json.output.JsonPrettyPrinter;
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
	
	public static void prettyPrint( JsonObject jsonObject, OutputStream destination ) {

		JsonStreamOutput jsonStreamOutput = new JsonStreamOutput( destination );
		JsonPrettyPrinter prettyPrinter = new JsonPrettyPrinter(jsonStreamOutput);
		JsonWalker.walk( jsonObject, prettyPrinter);
		jsonStreamOutput.flush();
	}

	
	public static JsonObject read( InputStream inputStream ) {
		
		JsonStreamInput jsonStreamInput = new JsonStreamInput( inputStream );
        JsonBuilder builder = new JsonBuilder();
        JsonReader.read(jsonStreamInput, builder);
        
        JsonObject answer = builder.getObjectDocument();
        return answer;		
	}
	
	public static JsonObject read( File file ) {
		
		FileInputStream fis = null;
		
		try {
			
			fis = new FileInputStream( file );
			return read( fis );
			
		} catch (FileNotFoundException e) {
			throw new BaseException( JsonObjectHelper.class, e );
		} finally {
			if( null != fis ) {
				InputStreamHelper.close( fis, false, JsonObjectHelper.class);
			}
		}
		
	}

	public static JsonObject read( String path ) {
		
		return read( new File( path ) );
		
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
	
	
	public static void write( JsonObject jsonObject, File destination ) {
		
		FileOutputStream fos = null;
		
		try {
			fos = new FileOutputStream( destination );
		} catch (FileNotFoundException e) {
			throw new BaseException( JsonObjectHelper.class, e );
		}
		
		write( jsonObject, fos );
		
		OutputStreamHelper.close( fos, JsonObjectHelper.class );

	}
	
}
