// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.broker;


import jsonbroker.library.common.auxiliary.Data;
import jsonbroker.library.common.log.Log;
import junit.framework.TestCase;

public class SerializerUnitTest extends TestCase {
	
	private static final Log log = Log.getLog(SerializerUnitTest.class );
	
	public void test1() { 
		log.enteredMethod();
	}
	
	public void testPingCall() {
		
		log.enteredMethod();
		
		Data data;
		
		{
			BrokerMessage call = new BrokerMessage();
			call.setServiceName( "endpoint" );
			call.setMethodName("ping");
			
			data = Serializer.serialize( call );
		}
		
		log.debug( data, "data");		
		{
			BrokerMessage call = Serializer.deserialize( data );
			
			assertEquals( "endpoint", call.getServiceName());
			assertEquals( "ping", call.getMethodName());
			
		}
		
		
	}
	
}
