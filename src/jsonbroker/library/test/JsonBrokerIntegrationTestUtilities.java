// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.test;

import jsonbroker.library.client.broker.ServiceHttpProxy;
import jsonbroker.library.client.http.Authenticator;
import jsonbroker.library.client.http.HttpDispatcher;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.common.net.NetworkAddress;
import jsonbroker.library.common.security.SecurityConfiguration;
import jsonbroker.library.common.security.Subject;
import jsonbroker.library.server.broker.DescribedService;
import jsonbroker.library.server.broker.Service;
import jsonbroker.library.server.broker.ServicesRegistery;
import jsonbroker.library.server.http.reqest_handler.AuthProcessor;
import jsonbroker.library.server.http.reqest_handler.OpenRequestHandler;
import jsonbroker.library.server.http.reqest_handler.ServicesProcessor;
import jsonbroker.library.server.http.security.HttpSecurityManager;
import jsonbroker.library.service.test.TestService;

public class JsonBrokerIntegrationTestUtilities {
	
	private static final Log log = Log.getLog(JsonBrokerIntegrationTestUtilities.class);

	
	
	public static final Subject TEST_SUBJECT = new Subject( Subject.TEST_USER, Subject.TEST_REALM, Subject.TEST_PASSWORD, "Test User");
	
	
	public enum TestType { CO_LOCATED, EXTERNAL/*EMBEDDED_OPEN_SERVER, EMBEDDED_AUTH_SERVER,EXTERNAL_OPEN_SERVER, EXTERNAL_AUTH_SERVER*/  };
	
	public enum Target { JAVA_WINDOWS_XP, CSHARP_WINDOWS_XP, OBJ_OSX }; 

	
	private static TestType getTestType() {
		return TestType.CO_LOCATED;
	}
	
	public static Target getTarget() {
		
		//return Target.JAVA_WINDOWS_XP;
		//return Target.CSHARP_WINDOWS_XP;
		return Target.OBJ_OSX;
		
	}
	
	
	
	public static NetworkAddress getNetworkAddress() {
		
		NetworkAddress answer;
		
		Target target = getTarget();
		if( Target.JAVA_WINDOWS_XP == target  ){
			answer = new NetworkAddress( "127.0.0.1", 8081 ); // localhost
		} else if( Target.OBJ_OSX == target || Target.CSHARP_WINDOWS_XP == target ) {
			BaseException e = new BaseException(JsonBrokerIntegrationTestUtilities.class, "unsupported target");
			throw e;
		} else {
			BaseException e = new BaseException(JsonBrokerIntegrationTestUtilities.class, "unknown target");
			throw e;
		}
		
		return answer;
		
	}
	
	public static String separator() {
		String answer;
		
		Target target = getTarget();
		if( Target.JAVA_WINDOWS_XP == target || Target.CSHARP_WINDOWS_XP == target ){
			answer = "\\"; // localhost
		} else if( Target.OBJ_OSX == target ) {
			answer = "/"; // Mac/OSX
		} else {
			BaseException e = new BaseException(JsonBrokerIntegrationTestUtilities.class, "unknown target");
			throw e;
		}

		return answer;
	}

	
	static OpenRequestHandler buildOpenProcessor( DescribedService openService ) {
		
		OpenRequestHandler answer = new OpenRequestHandler();		
		
		ServicesRegistery servicesRegistery = new ServicesRegistery();
		
		servicesRegistery.addService( openService );
		
		if( !(openService instanceof TestService) ) {
			servicesRegistery.addService( new TestService() );
		}

		////////////////////////////////////////////////////////////////////			
		ServicesProcessor synchronousServiceProcessor = new ServicesProcessor(servicesRegistery);
		answer.addHttpProcessor( synchronousServiceProcessor ); 
		
		return answer;
		
	}
	
	
	public static HttpSecurityManager buildHttpSecurityManager() {
		
		
		HttpSecurityManager answer = new HttpSecurityManager(SecurityConfiguration.TEST);
		return answer;
	}


	static AuthProcessor buildAuthProcessor( DescribedService authService ) {
		
		AuthProcessor answer = new AuthProcessor( buildHttpSecurityManager() );		
		
		ServicesRegistery servicesRegistery = new ServicesRegistery();
		
		servicesRegistery.addService( authService );
		
		if( !(authService instanceof TestService) ) {
			servicesRegistery.addService( new TestService() );
		}

		////////////////////////////////////////////////////////////////////			
		ServicesProcessor synchronousServiceProcessor = new ServicesProcessor(servicesRegistery);
		answer.addHttpProcessor( synchronousServiceProcessor ); 
		
		return answer;
		
	}
	
	
	
	private static Service buildProxy( TestType testType ) {
		
		// implicitly ... testType == TestType.EXTERNAL_OPEN_SERVER
		log.info( "setting up open proxy ... ");

		
		NetworkAddress networkAddress = getNetworkAddress();
		HttpDispatcher httpDispatcher = new HttpDispatcher( networkAddress );

		
		Service answer;
		
		if( /*TestType.EMBEDDED_AUTH_SERVER == testType ||*/ TestType.EXTERNAL == testType ) {
			
			Authenticator authenticator = new Authenticator( false, SecurityConfiguration.TEST );
			answer = new ServiceHttpProxy( httpDispatcher, authenticator );

		} else {
			answer = new ServiceHttpProxy( httpDispatcher );
		}
		
		return answer;

	}
	
	public static ServiceHttpProxy buildOpenHttpProxy() {
		
		NetworkAddress networkAddress = getNetworkAddress();
		HttpDispatcher httpDispatcher = new HttpDispatcher( networkAddress );
		ServiceHttpProxy answer = new ServiceHttpProxy( httpDispatcher );
		
		return answer;
	}

	public static ServiceHttpProxy buildAuthenticatingHttpProxy() {
		
		NetworkAddress networkAddress = getNetworkAddress();
		HttpDispatcher httpDispatcher = new HttpDispatcher( networkAddress );
		Authenticator authenticator = new Authenticator( false, SecurityConfiguration.TEST );
		ServiceHttpProxy answer = new ServiceHttpProxy( httpDispatcher, authenticator );
		
		return answer;
	}

	

	public static Service wrapAuthenticatedService( DescribedService service ) {
		
		if( getTestType() == TestType.CO_LOCATED ) {
			return service;
		}
		
		return buildAuthenticatingHttpProxy();

	}
	
	public static Service wrapOpenService( DescribedService service ) {
		
		if( getTestType() == TestType.CO_LOCATED ) {
			return service;
		}

		return buildOpenHttpProxy();

		
	}

	
	public static Service setupService( TestType testType, DescribedService service ) {
		
		if( testType == TestType.CO_LOCATED ) {
			return service;
		}

//		if( testType == TestType.EMBEDDED_OPEN_SERVER ) {
//			
//			OpenProcessor openProcessor = buildOpenProcessor( service );
//			startServer(openProcessor);
//		}
//		
//		if( testType == TestType.EMBEDDED_AUTH_SERVER ) {
//			AuthProcessor authProcessor = buildAuthProcessor( service );
//			startServer(authProcessor);
//			
//		}
		
		return buildProxy( testType );
		

	}

}
