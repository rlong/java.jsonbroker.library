// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json.output;

import jsonbroker.library.common.json.JsonArray;
import jsonbroker.library.common.json.JsonObject;
import jsonbroker.library.common.json.JsonWalker;
import jsonbroker.library.common.log.Log;
import junit.framework.TestCase;

public class JsonWriterUnitTest extends TestCase {

	private static final Log log = Log.getLog(JsonWriterUnitTest.class );
	
	public void test1() { 
		log.enteredMethod();
	}
	
	public void testEmptyObject() {
		
		JsonStringOutput output = new JsonStringOutput();
		{
			JsonWriter writer = new JsonWriter(output);
			
			JsonObject target;
			{
				target = new JsonObject();
			}

			JsonWalker.walk( target, writer);
		}
		
		String actual = output.toString();
		log.debug( actual, "actual");
		assertEquals( "{}" , actual);		
	}

	public void testEmptyArray() {
		
		JsonStringOutput output = new JsonStringOutput();
		{
			JsonWriter writer = new JsonWriter(output);
			
			JsonArray target;
			{
				target = new JsonArray();
			}

			JsonWalker.walk( target, writer);
		}
		String actual = output.toString();
		log.debug( actual, "actual");
		assertEquals( "[]" , actual);
	}

	public void testSimpleObject() {
		
		JsonStringOutput output = new JsonStringOutput();
		{
			JsonWriter writer = new JsonWriter(output);
			
			JsonObject target;
			{
				target = new JsonObject();
				target.put( "nullKey", null );
				target.put( "booleanKey", true);
				target.put( "integerKey", 314 );
				target.put( "stringKey", "value");
			}

			JsonWalker.walk( target, writer);
		}
		
		String actual = output.toString();
		log.debug( actual, "actual");
		assertEquals( "{\"nullKey\":null,\"booleanKey\":true,\"integerKey\":314,\"stringKey\":\"value\"}" , actual);		
	}
	
	public void testSimpleArray() {
		JsonStringOutput output = new JsonStringOutput();
		{
			JsonWriter writer = new JsonWriter(output);
			
			JsonArray target;
			{
				target = new JsonArray();
				
				target.add( (Object)null );
				target.add( true);
				target.add( 314 );
				target.add( "value");
			}

			JsonWalker.walk( target, writer);
		}
		String actual = output.toString();
		log.debug( actual, "actual");
		assertEquals( "[null,true,314,\"value\"]" , actual);

	}


}
