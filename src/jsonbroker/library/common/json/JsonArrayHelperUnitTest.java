// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json;

import jsonbroker.library.common.auxiliary.StringHelper;
import jsonbroker.library.common.log.Log;
import junit.framework.TestCase;

public class JsonArrayHelperUnitTest extends TestCase {
	
	
	private static final Log log = Log.getLog(JsonArrayHelperUnitTest.class );

	
	public void test1() { 
		log.enteredMethod();
	}
	
	
	public void testEmptyArray() {

		JsonArray target = new JsonArray();
		
		{
			byte[] bytes = JsonArrayHelper.toBytes( target );
			assertEquals( 2, bytes.length );
			assertEquals( '[', bytes[0] );
			assertEquals( ']', bytes[1] );
		}
		
	}
	
	
	public void testSimpleArray() {

		JsonArray target;
		{
			target = new JsonArray();
			
			target.add( (Object)null );
			target.add( true);
			target.add( 314 );
			target.add( "value");
		}

		{			
			byte[] bytes = JsonArrayHelper.toBytes( target );
			assertEquals( 23, bytes.length );
			String expectedValue = StringHelper.fromUtf8Bytes( bytes, 0, bytes.length);
			log.debug( expectedValue, "expectedValue" );
			assertEquals( "[null,true,314,\"value\"]" , expectedValue);
			
		}
	}
	

}
