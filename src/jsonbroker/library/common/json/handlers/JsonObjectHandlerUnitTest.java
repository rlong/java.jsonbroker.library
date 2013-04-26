// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json.handlers;

import jsonbroker.library.common.auxiliary.MutableData;
import jsonbroker.library.common.auxiliary.StringHelper;
import jsonbroker.library.common.json.JsonObject;
import jsonbroker.library.common.json.input.JsonDataInput;
import jsonbroker.library.common.json.output.JsonStringOutput;
import jsonbroker.library.common.log.Log;
import junit.framework.TestCase;

public class JsonObjectHandlerUnitTest extends TestCase {
	
	private static final Log log = Log.getLog(JsonObjectHandlerUnitTest.class );
	
	public void test1() { 
		log.enteredMethod();
	}
	
	public void testWrite1() {
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.put( "k1", new Integer(1));
		jsonObject.put( "k2", "string-value");
			
		
		JsonStringOutput output;
		{
			output = new JsonStringOutput();

			JsonObjectHandler handler = JsonObjectHandler.getInstance();
			
			handler.writeValue( jsonObject, output);
		}

		String actual = output.toString();
		log.debug( actual, "actual");
		assertEquals( "{\"k1\":1,\"k2\":\"string-value\"}" , actual);		

		
		
	}
	
	public void testRead1() {
		
		JsonDataInput input;
		{
			String inputString = "{\"k1\":1,\"k2\":\"string-value\"}";
			
			MutableData data = new MutableData();
			data.append( StringHelper.toUtfBytes(inputString));
			
			input = new JsonDataInput(data);
			
		}
		
		Object handlerResponse;
		{			

			JsonObjectHandler handler = JsonObjectHandler.getInstance();
			handlerResponse = handler.readValue( input);
			
		}

		
		assertTrue( handlerResponse instanceof JsonObject);
		
		JsonObject value = (JsonObject)handlerResponse;
		
		
		assertEquals( 1, value.getInt("k1"));
		assertEquals( "string-value", value.getString("k2"));
		

		
	}
	
	public void testReadEmptyObject() { 
		JsonDataInput input;
		{
			String inputString = "{}";
			
			MutableData data = new MutableData();
			data.append( StringHelper.toUtfBytes(inputString));
			
			input = new JsonDataInput(data);
			
		}
		
		Object handlerResponse;
		{			

			JsonObjectHandler handler = JsonObjectHandler.getInstance();
			handlerResponse = handler.readValue( input);
			
		}

		assertTrue( handlerResponse instanceof JsonObject);

	}


}
