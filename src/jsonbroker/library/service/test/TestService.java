// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.service.test;

import jsonbroker.library.common.broker.BrokerMessage;
import jsonbroker.library.common.json.JsonObject;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.server.broker.DescribedService;
import jsonbroker.library.server.broker.ServiceDescription;
import jsonbroker.library.server.broker.ServiceHelper;


public class TestService implements DescribedService {

	private static final Log log = Log.getLog(TestService.class);

	public static final String SERVICE_NAME = "jsonbroker.TestService";
	public static final ServiceDescription SERVICE_DESCRIPTION = new ServiceDescription( SERVICE_NAME );


	////////////////////////////////////////////////////////////////////////////
	public TestService() {
	}
	
	////////////////////////////////////////////////////////////////////////////
	

	public JsonObject echo(JsonObject associativeParamaters) {
		
		log.enteredMethod();
		return associativeParamaters;
		
	}

	public void ping() { 
		log.enteredMethod();		
	}
	
	private String toString( Object o ) {
		return o.toString();
	}
	
	
	public void raiseError() {

		log.enteredMethod();
		toString( null );
	}


	
	@Override
	public BrokerMessage process(BrokerMessage request) {
		
		
		String methodName = request.getMethodName();
		
		if( "echo".equals( methodName )) {
			
			JsonObject associativeParamaters = request.getAssociativeParamaters();
			
			associativeParamaters = this.echo( associativeParamaters );
			
			BrokerMessage answer= BrokerMessage.buildResponse( request );
			answer.setAssociativeParamaters( associativeParamaters );
			return answer;			
		}
		
		if( "ping".equals( methodName )) {
			this.ping();
			
			BrokerMessage answer = BrokerMessage.buildResponse( request );
			return answer;
		}
				
		if( "raiseError".equals( methodName )) {
			this.raiseError();
			
			BrokerMessage answer = BrokerMessage.buildResponse( request );
			return answer;
		}
		
		throw ServiceHelper.methodNotFound( this, request);
	}


	@Override
	public ServiceDescription getServiceDescription() {
		return SERVICE_DESCRIPTION;
	}


}
