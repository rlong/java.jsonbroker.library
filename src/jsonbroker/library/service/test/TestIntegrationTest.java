// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.service.test;

import jsonbroker.library.common.log.Log;
import jsonbroker.library.server.broker.DescribedService;
import jsonbroker.library.server.broker.Service;
import jsonbroker.library.test.JsonBrokerIntegrationTestUtilities;
import junit.framework.TestCase;

public class TestIntegrationTest extends TestCase  {

	
	private static final Log log = Log.getLog(TestIntegrationTest.class);

	static TestProxy _proxy;
	
	static {
		
		DescribedService describedService = new TestService();
		Service service = JsonBrokerIntegrationTestUtilities.wrapOpenService( describedService );		
		_proxy = new TestProxy( service );

	}

	public void test1() {
		log.enteredMethod();
	}
	
	public void testPing() {
		_proxy.ping();
	}

	public void test5Pings() {
		for( int i = 0; i < 5; i ++) {
			_proxy.ping();
		}
		
	}
	
	public void testEchoLong() {
		
		long expected = Integer.MAX_VALUE;
		expected *= 1024;		
		log.debug( expected, "expected" );
		
		long actual = _proxy.echo( expected );
		
		assertEquals( expected, actual);
		
	}

}
