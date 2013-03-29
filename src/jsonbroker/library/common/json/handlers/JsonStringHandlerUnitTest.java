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
	public void testWriteABC() { 
		JsonStringOutput output = new JsonStringOutput();
		
		JsonStringHandler handler = new JsonStringHandler();
		
		handler.writeValue( "ABC", output);
		
		String actual = output.toString();
		assertEquals( "\"ABC\"" , actual);
		
		
		
	}

	public void testReadABC() { 
		String expected = "\"ABC\"";
		
		MutableData data = new MutableData();
		data.append( StringHelper.toUtfBytes(expected));
		
		JsonDataInput input = new JsonDataInput(data);
				
		JsonStringHandler handler = new JsonStringHandler();
		Object handlerResponse = handler.readValue( input);
		
		assertTrue( handlerResponse instanceof String );
		
		String actual = (String)handlerResponse;
		assertEquals( "ABC" , actual);
		
	}
	
	
	public void testExtendedChars() { 
		
		String expected = "\"€‚ƒ„…†‡ˆ‰©®Ñíò\""; 
		
		MutableData data = new MutableData();
		data.append( StringHelper.toUtfBytes(expected));
		
		JsonDataInput input = new JsonDataInput(data);
		
		JsonStringHandler handler = new JsonStringHandler();
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
		
		JsonStringHandler handler = new JsonStringHandler();
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
		
		
		JsonStringHandler handler = new JsonStringHandler();
		Object handlerResponse = handler.readValue( input);
		
		assertTrue( handlerResponse instanceof String );
		
		String actual = (String)handlerResponse;
		assertEquals( "" , actual);

	}
	

}
