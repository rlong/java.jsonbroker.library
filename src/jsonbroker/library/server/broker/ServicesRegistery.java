// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.broker;

import java.util.HashMap;
import java.util.Map;

import jsonbroker.library.common.broker.BrokerMessage;
import jsonbroker.library.common.broker.BrokerMessageType;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.json.JsonObject;
import jsonbroker.library.common.log.Log;


public class ServicesRegistery implements Service {
	
	
    public static class ErrorDomain
    {
        public static final String SERVICE_NOT_FOUND = "jsonbroker.ServicesRegistery.SERVICE_NOT_FOUND";
    }

	private static Log log = Log.getLog(ServicesRegistery.class);
	

	private HashMap<String, DescribedService> _services; 
	
	////////////////////////////////////////////////////////////////////////////
	//
	private ServicesRegistery _next;
	

	////////////////////////////////////////////////////////////////////////////
	public ServicesRegistery() {		
		_services = new HashMap<String, DescribedService>();
	}
	
	
	private Service getService( String serviceName ) {
		
		Service answer = _services.get( serviceName );
		
		if( null != answer ) {
			return answer;
		}
		
        if (null == _next) {
        	
			String technicalError = String.format( "null == answer, serviceName = '%s'", serviceName );
			BaseException e = new BaseException( this, technicalError);
			e.setErrorDomain( ErrorDomain.SERVICE_NOT_FOUND );
			throw e;
        }
        	
		return _next.getService( serviceName );
	}
	
	public boolean containsService( String serviceName ) {
		
        if( _services.containsKey( serviceName ) )
        {
            return true;
        }
        if (null != _next)
        {
            return _next.containsService(serviceName);
        }
        return false;

	}
	
	public void addService( DescribedService service ) {
		String serviceName = service.getServiceDescription().getServiceName();
		log.infoFormat("'%s' -> %s", service.getServiceDescription().getServiceName(), service.getClass().getName());
		_services.put( serviceName, service);
	}
	
	public void removeService( DescribedService service ) {
		String serviceName = service.getServiceDescription().getServiceName();
		_services.remove(serviceName);
	}
	
	public void debug() {
		
		for (Map.Entry<String, DescribedService> entry : _services.entrySet()) {
			String message = String.format( "%s -> %s", entry.getKey(), entry.getValue().getClass().getName() );
			log.debug( message );
		}
	}

	
	private BrokerMessage processMetaRequest( BrokerMessage request) {
		
		
		String methodName = request.getMethodName();
		
		if( "getVersion".equals( methodName )) {
			
			JsonObject associativeParamaters = request.getAssociativeParamaters();
			String serviceName = request.getServiceName();
			
			BrokerMessage answer= BrokerMessage.buildMetaResponse( request );
			associativeParamaters = answer.getAssociativeParamaters();
			
			DescribedService describedService = _services.get( serviceName );			
			if( null == describedService ) {
				
				associativeParamaters.put( "exists", false);
				
			} else {
				associativeParamaters.put( "exists", true );
				ServiceDescription serviceDescription = describedService.getServiceDescription();
				associativeParamaters.put( "majorVersion", serviceDescription.getMajorVersion() );
				associativeParamaters.put( "minorVersion", serviceDescription.getMinorVersion() );
			}
			
			return answer;			
		}

		
		throw ServiceHelper.methodNotFound( this, request);

	}

	@Override
	public BrokerMessage process(BrokerMessage request) {
		
		if( BrokerMessageType.META_REQUEST ==  request.getMessageType() ) {
			
			return this.processMetaRequest( request );
			
		}
		
		Service serviceDelegate = this.getService( request.getServiceName() );		
		return serviceDelegate.process( request );
		
	}

}

