// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.service.log;

import jsonbroker.library.common.broker.BrokerMessage;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.json.JsonArray;
import jsonbroker.library.common.json.JsonObject;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.server.broker.DescribedService;
import jsonbroker.library.server.broker.ServiceDescription;

public class LogService implements DescribedService {

	
	private static final Log log = Log.getLog(LogService.class);
	
	public static final String SERVICE_NAME = "log";
	public static final ServiceDescription SERVICE_DESCRIPTION = new ServiceDescription( SERVICE_NAME );

	

	////////////////////////////////////////////////////////////////////////////
	
	private static LogService _instance = new LogService();
	
	
	public static LogService resolve() {
		
		return _instance;
		
	}

	////////////////////////////////////////////////////////////////////////////
	
	
	public void log( String scope, Object value, String name ) {
		
		String stringValue = "?";
		if( null == value  ) {
			stringValue = "NULL";
		} else if( value instanceof String ) {
			stringValue = String.format( "\'%s\'", (String)value);
		} else if( value instanceof JsonObject ) {
			JsonObject jsonObject = (JsonObject)value;
			stringValue = jsonObject.toString();
		}  else if( value instanceof JsonArray ) {
			JsonArray jsonArray = (JsonArray)value;
			stringValue = jsonArray.toString();
		}
		
		String message = String.format( "%s: %s = %s", scope, name, stringValue );
		
		log.info( message );
		
	}




	@Override
	public BrokerMessage process(BrokerMessage request) {

		String methodName = request.getMethodName();

		if( "log".equals( methodName ) ) {
			
			JsonArray parameters = request.getParamaters();
			
			String scope = parameters.getString( 0);

			Object value = parameters.getObject(1);

			String name = parameters.getString(2);
			
			this.log( scope, value, name );
			
			return BrokerMessage.buildResponse( request );			
		}
		
        String technicalError = String.format("Unknown methodName. methodName = '%s'", methodName);
        
        throw new BaseException(this, technicalError);

	}
	
	
	@Override
	public ServiceDescription getServiceDescription() {
		return SERVICE_DESCRIPTION;
	}



}
