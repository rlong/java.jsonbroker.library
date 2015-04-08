// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.service.test;

import jsonbroker.library.broker.BrokerMessage;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.json.JsonObject;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.broker.server.DescribedService;
import jsonbroker.library.broker.server.ServiceDescription;
import jsonbroker.library.broker.server.ServiceHelper;


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
	
	
	public void raiseError() {

		log.enteredMethod();
		BaseException e = new BaseException(this, "TestService.raiseError() called");
		e.setErrorDomain( "jsonbroker.TestService.RAISE_ERROR" );
		throw e;
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
