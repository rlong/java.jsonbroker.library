// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.client.broker;

import jsonbroker.library.common.broker.BrokerMessage;
import jsonbroker.library.common.json.JsonObject;
import jsonbroker.library.server.broker.Service;

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
    		
    		int majorVersion = associativeParamaters.getInteger( "majorVersion" );
    		int minorVersion = associativeParamaters.getInteger( "minorVersion" );    		
    		int answer[] = {majorVersion,minorVersion};
    		return answer;
    		
    	}
    	
	}

}
