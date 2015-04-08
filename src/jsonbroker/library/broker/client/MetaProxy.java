// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.broker.client;

import jsonbroker.library.broker.BrokerMessage;
import jsonbroker.library.common.json.JsonObject;
import jsonbroker.library.broker.server.Service;

public class MetaProxy {
	
    ////////////////////////////////////////////////////////////////////////////
	//
	Service _service;

	
	public MetaProxy( Service service )  {
		
		_service = service;
		
	}
	
	public int[] getVersion( String serviceName ) {
		
    	BrokerMessage request = BrokerMessage.buildMetaRequest( serviceName, "getVersion");
		
    	BrokerMessage response = _service.process( request );
    	
    	JsonObject associativeParamaters = response.getAssociativeParamaters();
    	boolean exists = associativeParamaters.getBoolean( "exists" );
    	if( !exists ) {
    		
    		return null;
    		
    	} else {
    		
    		int majorVersion = associativeParamaters.getInt( "majorVersion" );
    		int minorVersion = associativeParamaters.getInt( "minorVersion" );    		
    		int answer[] = {majorVersion,minorVersion};
    		return answer;
    		
    	}
    	
	}

}
