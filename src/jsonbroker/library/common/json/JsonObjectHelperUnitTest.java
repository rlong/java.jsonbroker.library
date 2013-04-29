// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json;

import jsonbroker.library.common.auxiliary.StringHelper;
import jsonbroker.library.common.log.Log;
import junit.framework.TestCase;

public class JsonObjectHelperUnitTest extends TestCase {

	private static final Log log = Log.getLog(JsonObjectHelperUnitTest.class );

	
	public void test1() { 
		log.enteredMethod();
	}
	
	public void testEmptyObject() {

		JsonObject target = new JsonObject();
		
		{
			byte[] bytes = JsonObjectHelper.toBytes( target );
			assertEquals( 2, bytes.length );
			assertEquals( '{', bytes[0] );
			assertEquals( '}', bytes[1] );
		}
	}

	public void testSimpleObject() {

		JsonObject target;
		{
			target = new JsonObject();
			target.put( "nullKey", null );
			target.put( "booleanKey", true);
			target.put( "integerKey", 314 );
			target.put( "stringKey", "value");
		}
		
		{			
			byte[] bytes = JsonObjectHelper.toBytes( target );
			assertEquals( 71, bytes.length );
			String expectedValue = StringHelper.fromUtf8Bytes( bytes, 0, bytes.length);
			log.debug( expectedValue, "expectedValue" );
			assertEquals( "{\"nullKey\":null,\"booleanKey\":true,\"integerKey\":314,\"stringKey\":\"value\"}" , expectedValue);
		}
		
	}
	
	
}
