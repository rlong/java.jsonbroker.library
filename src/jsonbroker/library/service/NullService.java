// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.service;

import jsonbroker.library.broker.BrokerMessage;
import jsonbroker.library.broker.server.DescribedService;
import jsonbroker.library.broker.server.ServiceDescription;
import jsonbroker.library.common.log.Log;



public class NullService implements DescribedService {
	
	Log log = Log.getLog( NullService.class );
	
	private ServiceDescription _serviceDescription; 
	
	public NullService( String serviceName ) {
		_serviceDescription = new ServiceDescription( serviceName );
	}

	

	@Override
	public BrokerMessage process(BrokerMessage request) {
		
		String requestAsString = request.toJsonArray().toString();
		
		log.warnFormat( "unimplemented; requestAsString = '%s'", requestAsString );
		
		return BrokerMessage.buildResponse( request );
	}
	
	@Override
	public ServiceDescription getServiceDescription() {
		return _serviceDescription;
	}

}
