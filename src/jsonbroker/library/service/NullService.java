// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.service;

import jsonbroker.library.common.broker.BrokerMessage;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.server.broker.DescribedService;
import jsonbroker.library.server.broker.ServiceDescription;



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
