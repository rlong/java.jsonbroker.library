// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.broker;

import jsonbroker.library.common.broker.BrokerMessage;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.exception.ErrorCodeUtilities;

public class ServiceHelper {
	
	
	private static final int BASE = ErrorCodeUtilities.getBaseErrorCode("jsonbroker.ServiceHelper");
	private static final int METHOD_NOT_FOUND = BASE | 0x01;

	
	public static BaseException methodNotFound( Object originator, BrokerMessage request ) {
		
		String methodName = request.getMethodName();
		
		BaseException answer = new BaseException( originator, "Unknown methodName; methodName = '%s'", methodName );
		answer.setFaultCode( METHOD_NOT_FOUND );
		
		return answer;
		
				
	}
	
}
