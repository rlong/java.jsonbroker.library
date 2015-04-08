// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.http.security;

import org.junit.experimental.categories.Category;

import jsonbroker.library.broker.client.ServiceHttpProxy;
import jsonbroker.library.client.http.Authenticator;
import jsonbroker.library.client.http.HttpDispatcher;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.http.HttpStatus;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.common.net.NetworkAddress;
import jsonbroker.library.common.security.SecurityConfiguration;
import jsonbroker.library.common.security.Subject;
import jsonbroker.library.service.test.TestProxy;
import jsonbroker.library.test.IntegrationTest;
import jsonbroker.library.test.JsonBrokerIntegrationTestUtilities;
import junit.framework.TestCase;

// vvv http://weblogs.java.net/blog/johnsmart/archive/2010/04/25/grouping-tests-using-junit-categories-0
@Category(IntegrationTest.class)
// ^^^ http://weblogs.java.net/blog/johnsmart/archive/2010/04/25/grouping-tests-using-junit-categories-0
public class HttpSecurityIntegrationTest extends TestCase {
	
	private static Log log = Log.getLog(HttpSecurityIntegrationTest.class);
	
	private static HttpDispatcher _httpDispatcher;
	
	static {
		
		NetworkAddress networkAddress = JsonBrokerIntegrationTestUtilities.getNetworkAddress(); 		
		_httpDispatcher = new HttpDispatcher( networkAddress );
	}
	
	
	public void test1() {
		log.enteredMethod();
	}
	

	private void testBadUsername( boolean authInt ) {

		
		SecurityConfiguration securityConfiguration;
		{
			securityConfiguration = SecurityConfiguration.buildTestConfiguration();
			Subject badUsername = new Subject("BAD-USER", "users@test-domain", "12345678", "label" );
			securityConfiguration.addClient( badUsername );
		}
		
		
		Authenticator authenticator = new Authenticator( authInt, securityConfiguration );
		
		ServiceHttpProxy ServiceHttpProxy = new ServiceHttpProxy( _httpDispatcher, authenticator );
		TestProxy proxy = new TestProxy( ServiceHttpProxy );
		
		
		try {
			proxy.ping();
			fail(); // bad
		} catch( BaseException e ) {
			assertEquals( HttpStatus.UNAUTHORIZED_401, e.getFaultCode() ); // good			 
		}
		
	}
	public void testBadUsername() {
		
		testBadUsername( true );
		testBadUsername( false );

	}

	public void testBadRealm(boolean authInt) {
		
		SecurityConfiguration securityConfiguration;
		{
			securityConfiguration = SecurityConfiguration.buildTestConfiguration();
			Subject badRealm = new Subject( "test", "users@BAD-REALM", "12345678", "label" );
			securityConfiguration.addClient( badRealm );
		}

		Authenticator authenticator = new Authenticator( authInt, securityConfiguration );

		ServiceHttpProxy ServiceHttpProxy = new ServiceHttpProxy( _httpDispatcher, authenticator );
		TestProxy proxy = new TestProxy( ServiceHttpProxy );
		
		try {
			proxy.ping();
			fail(); // bad
		} catch( BaseException e ) {
			assertEquals( HttpStatus.UNAUTHORIZED_401, e.getFaultCode() ); // good			 
		}
	}
	
	public void testBadRealm() {
		testBadRealm( true );
		testBadRealm( false );
	}

	public void testBadPassword(boolean authInt) {
		
		SecurityConfiguration securityConfiguration;
		{
			securityConfiguration = SecurityConfiguration.buildTestConfiguration();
			Subject badPassword = new Subject( "test", "users@test-domain", "BAD-PASSWORD", "label" );
			securityConfiguration.addClient( badPassword );
		}

		
		Authenticator authenticator = new Authenticator( false, securityConfiguration );

		ServiceHttpProxy ServiceHttpProxy = new ServiceHttpProxy( _httpDispatcher, authenticator );
		TestProxy proxy = new TestProxy( ServiceHttpProxy );
		
		try {
			proxy.ping();
			fail(); // bad
		} catch( BaseException e ) {
			assertEquals( HttpStatus.UNAUTHORIZED_401, e.getFaultCode() ); // good			 
		}
	}
	
	public void testBadPassword() {
		
		testBadPassword(true);
		testBadPassword(false);
		
	}


}
