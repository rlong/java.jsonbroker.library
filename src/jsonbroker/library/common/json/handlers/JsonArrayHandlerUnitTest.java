// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json.handlers;

import java.util.AbstractList;
import java.util.ArrayList;

import jsonbroker.library.common.auxiliary.MutableData;
import jsonbroker.library.common.auxiliary.StringHelper;
import jsonbroker.library.common.json.JsonArray;
import jsonbroker.library.common.json.input.JsonDataInput;
import jsonbroker.library.common.json.output.JsonStringOutput;
import jsonbroker.library.common.log.Log;
import junit.framework.TestCase;

public class JsonArrayHandlerUnitTest extends TestCase {

	private static final Log log = Log.getLog(JsonArrayHandlerUnitTest.class );
	
	public void test1() { 
		log.enteredMethod();
	}

	
	public void testWrite123() {
		
		ArrayList<Object> arrayList;
		
		{
			arrayList = new ArrayList<Object>();
			
			arrayList.add( new Integer(1));
			arrayList.add( new Integer(2));
			arrayList.add( new Integer(3));
			
		}
		
		JsonStringOutput output;
		{
			
			output = new JsonStringOutput();

			JsonArrayHandler handler = JsonArrayHandler.getInstance();
			
			handler.writeValue( arrayList, output);

		}
		
		
		String actual = output.toString();
		assertEquals( "[1,2,3]" , actual);		
		
	}
	
	public void testWriteABC() { 
		ArrayList<Object> arrayList;
		
		{
			arrayList = new ArrayList<Object>();
			
			arrayList.add( "A");
			arrayList.add( "B");
			arrayList.add( "C");
			
		}
		
		JsonStringOutput output;
		{
			
			output = new JsonStringOutput();
			

			JsonArrayHandler handler = JsonArrayHandler.getInstance();
			
			handler.writeValue( arrayList, output);

		}
		
		
		String actual = output.toString();
		assertEquals( "[\"A\",\"B\",\"C\"]" , actual);		

	}
	
	
	public void testRead123() {
		
		JsonDataInput reader;
		{
			String input = "[1,2,3]";
			
			MutableData data = new MutableData();
			data.append( StringHelper.toUtfBytes(input));
			
			reader = new JsonDataInput(data);
			
		}
		
		Object handlerResponse;
		{
			JsonArrayHandler handler = JsonArrayHandler.getInstance();
			handlerResponse = handler.readValue( reader);
			
		}
		
		assertTrue( handlerResponse instanceof JsonArray );
		
		JsonArray values = (JsonArray)handlerResponse;
		
		assertEquals( 3, values.size());
		
		assertEquals( 1, values.getInteger( 0));
		assertEquals( 2, values.getInteger( 1));
		assertEquals( 3, values.getInteger( 2));

	}
	
	public void testReadABC() {
		JsonDataInput reader;
		{
			String input = "[\"A\",\"B\",\"C\"]";
			
			MutableData data = new MutableData();
			data.append( StringHelper.toUtfBytes(input));
			
			reader = new JsonDataInput(data);
			
		}
		
		Object handlerResponse;
		{

			JsonArrayHandler handler = JsonArrayHandler.getInstance();
			handlerResponse = handler.readValue( reader);
			
		}
		
		assertTrue( handlerResponse instanceof JsonArray);
		
		JsonArray values = (JsonArray)handlerResponse;
		
		assertEquals( 3, values.size());
		
		assertEquals( "A", values.getString(0));
		assertEquals( "B", values.getString(1));
		assertEquals( "C", values.getString(2));

	}
	
	public void testReadEmptyArray() { 
		JsonDataInput reader;
		{
			String input = "[]";
			
			MutableData data = new MutableData();
			data.append( StringHelper.toUtfBytes(input));
			
			reader = new JsonDataInput(data);
			
		}
		
		Object handlerResponse;
		{

			JsonArrayHandler handler = JsonArrayHandler.getInstance();
			handlerResponse = handler.readValue( reader);
			
		}
		
		assertTrue( handlerResponse instanceof AbstractList<?> );
		
		AbstractList<Object> values = (AbstractList<Object>)handlerResponse;
		
		assertEquals( 0, values.size());
		
		
	}
	
	public void testOrbCall() { 
		JsonDataInput input;
		{
			String stringInput = "[\"call\",{},\"endpoint\",\"ping\",[]]";
			
			MutableData data = new MutableData();
			data.append( StringHelper.toUtfBytes(stringInput));
			
			log.debug( data, "input");
			
			input = new JsonDataInput(data);
			
		}
		
		Object handlerResponse;
		{

			JsonArrayHandler handler = JsonArrayHandler.getInstance();
			handlerResponse = handler.readValue( input);
			
		}
		
		assertTrue( handlerResponse instanceof AbstractList<?> );
		
		AbstractList<Object> values = (AbstractList<Object>)handlerResponse;
		
		assertEquals( 5, values.size());

	}
	
	public void testChromeResponse() { 
		String inputString = "[ {\n";
		inputString += "   \"faviconUrl\": \"http://localhost:8082/favicon.ico\",\n";
		inputString += "   \"thumbnailUrl\": \"/thumb/http://localhost:8082/index.html\",\n";
		inputString += "   \"title\": \"osx.skeleton\",\n";
		inputString += "   \"url\": \"http://localhost:8082/index.html\"\n";
		inputString += "} ]";
		
		MutableData data = new MutableData();
		data.append( StringHelper.toUtfBytes(inputString));
		
		log.debug( inputString, "input");
		
		JsonDataInput input = new JsonDataInput(data);
		JsonArrayHandler.readJsonArray( input);
		
	}

}
