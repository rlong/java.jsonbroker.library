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

public class JsonNumberHandlerUnitTest extends TestCase {

	private static final Log log = Log.getLog(JsonNumberHandlerUnitTest.class );
	
	public void test1() { 
		log.enteredMethod();
	}
	
	public void testWrite123() { 
		JsonStringOutput output = new JsonStringOutput();
		
		JsonNumberHandler handler = JsonNumberHandler.getInstance();
		
		handler.writeValue( new Integer(123), output);
		
		String actual = output.toString();
		assertEquals( "123" , actual);		
		
	}
	
	public void testRead123() { 

		
		String inputString = "123]";
		
		MutableData data = new MutableData();
		data.append( StringHelper.toUtfBytes(inputString));
		
		JsonDataInput input = new JsonDataInput(data);
		
		JsonNumberHandler handler = JsonNumberHandler.getInstance();
		Object handlerResponse = handler.readValue( input);
		
		assertTrue( handlerResponse instanceof Integer );
		
		Integer actual = (Integer)handlerResponse;
		assertEquals( 123 , actual.intValue());

	}
	
	public void testTwelvePointFive() {
		
		String inputString = "12.5]";
		
		MutableData data = new MutableData();
		data.append( StringHelper.toUtfBytes(inputString));
		
		JsonDataInput input = new JsonDataInput(data);
		
		JsonNumberHandler handler = JsonNumberHandler.getInstance();
		Object handlerResponse = handler.readValue( input);
		
		assertTrue( handlerResponse instanceof Double );
		
		Double actual = (Double)handlerResponse;
		assertEquals( 12.5 , actual.doubleValue());
		
	}


}
