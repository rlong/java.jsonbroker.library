// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.broker.client;

import jsonbroker.library.common.log.Log;
import jsonbroker.library.service.test.TestService;
import jsonbroker.library.test.JsonBrokerIntegrationTestUtilities;
import junit.framework.TestCase;

public class MetaProxyIntegrationTest extends TestCase {
	
	
	private static final Log log = Log.getLog(MetaProxyIntegrationTest.class);
	
	
	public void test1() {
		log.enteredMethod();
	}

	public void testGetInterfaceVersion() {
		
		log.enteredMethod();
		
		ServiceHttpProxy serviceHttpProxy = JsonBrokerIntegrationTestUtilities.buildOpenHttpProxy();
		
		MetaProxy proxy = new MetaProxy( serviceHttpProxy ); 
		int[] version = proxy.getVersion( TestService.SERVICE_NAME );
		
		assertNotNull( version );
		assertEquals( 2, version.length);
		
		assertEquals( 1, version[0]);
		assertEquals( 0, version[1]);
		
	}
	
	public void testGetInterfaceVersionFromNonExistingService() {
		
		log.enteredMethod();
		
		ServiceHttpProxy serviceHttpProxy = JsonBrokerIntegrationTestUtilities.buildOpenHttpProxy();
		
		MetaProxy proxy = new MetaProxy( serviceHttpProxy ); 
		int[] version = proxy.getVersion( "module.non-existing-service" );
		
		assertNull( version );
		
	}

}
