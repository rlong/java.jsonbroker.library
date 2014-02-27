// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json.handlers;


import jsonbroker.library.common.auxiliary.MutableData;
import jsonbroker.library.common.auxiliary.StringHelper;
import jsonbroker.library.common.json.input.JsonDataInput;
import jsonbroker.library.common.json.output.JsonStringOutput;
import jsonbroker.library.common.log.Log;
import junit.framework.TestCase;

public class JsonStringHandlerUnitTest extends TestCase {
	
	private static final Log log = Log.getLog(JsonStringHandlerUnitTest.class );
	
	public void test1() { 
		log.enteredMethod();
	}
	
	
	private String encodeJsonStringValue( String input ) {

		JsonStringOutput output = new JsonStringOutput();
		JsonStringHandler handler = JsonStringHandler.INSTANCE;
		
		handler.writeValue( input, output);
		
		return output.toString();

	}
	
	private String decodeJsonStringValue( String input ) {
		
		
		MutableData data = new MutableData();
		data.append( StringHelper.toUtfBytes(input));
		
		JsonDataInput jsonDataInput = new JsonDataInput(data);
				
		String answer = JsonStringHandler.readString( jsonDataInput);
		return answer;

	}

	
	public void testWriteABC() {
		
		String actual = encodeJsonStringValue( "ABC" );
		assertEquals( "\"ABC\"" , actual);
		
		
	}
	
	public void testReadABC() { 
		
		String actual = decodeJsonStringValue( "\"ABC\"" );
		assertEquals( "ABC" , actual);
		
	}
	
	
	
	
	public void testReadWriteSlashes() {
		
		{
			String encodedValue = encodeJsonStringValue( "\\" );
			assertEquals( "\"\\\\\"" , encodedValue);
			String decodedValue = decodeJsonStringValue( encodedValue );
			assertEquals( "\\", decodedValue);
		}

		{
			String encodedValue = encodeJsonStringValue( "/" );
			assertEquals( "\"\\/\"" , encodedValue);
			String decodedValue = decodeJsonStringValue( encodedValue );
			assertEquals( "/", decodedValue);
		}
		
	}

	
	
	public void testExtendedChars() { 
		
		String expected = "\"€‚ƒ„…†‡ˆ‰©®Ñíò\""; 
		
		MutableData data = new MutableData();
		data.append( StringHelper.toUtfBytes(expected));
		
		JsonDataInput input = new JsonDataInput(data);
		
		JsonStringHandler handler = JsonStringHandler.INSTANCE;
		Object handlerResponse = handler.readValue( input);
		
		assertTrue( handlerResponse instanceof String );
		
		String actual = (String)handlerResponse;
		assertEquals( "€‚ƒ„…†‡ˆ‰©®Ñíò" , actual);

	}
	
	public void testHebrew() {
		
		String expected = "\"עולמו של יובל המבולבל\"";
		MutableData data = new MutableData();
		data.append( StringHelper.toUtfBytes(expected));
		
		JsonDataInput input = new JsonDataInput(data);
		
		JsonStringHandler handler = JsonStringHandler.INSTANCE;
		Object handlerResponse = handler.readValue( input);
		
		assertTrue( handlerResponse instanceof String );
		
		String actual = (String)handlerResponse;
		assertEquals( "עולמו של יובל המבולבל" , actual);
		
	}
	
	public void testEmptyString() { 
		
		String expected = "\"\"";
		
		MutableData data = new MutableData();
		data.append( StringHelper.toUtfBytes(expected));
		
		JsonDataInput input = new JsonDataInput(data);
		
		
		JsonStringHandler handler = JsonStringHandler.INSTANCE;
		Object handlerResponse = handler.readValue( input);
		
		assertTrue( handlerResponse instanceof String );
		
		String actual = (String)handlerResponse;
		assertEquals( "" , actual);

	}
	

}
