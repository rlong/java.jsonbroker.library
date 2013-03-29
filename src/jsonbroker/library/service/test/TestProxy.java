// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.service.test;

import jsonbroker.library.common.broker.BrokerMessage;
import jsonbroker.library.common.json.JsonObject;
import jsonbroker.library.server.broker.Service;

public class TestProxy {
	
	
    ////////////////////////////////////////////////////////////////////////////
	Service _service;
    
    
    public TestProxy( Service service ) { 
    	_service = service;
    }
    
    
    public long echo( long l ) {
    	
    	BrokerMessage request = BrokerMessage.buildRequest( TestService.SERVICE_NAME, "echo");
    	JsonObject associativeParamaters = request.getAssociativeParamaters();
    	associativeParamaters.put("value", l );
    			
    	BrokerMessage response = _service.process( request );
    	
    	associativeParamaters = response.getAssociativeParamaters();
    	return associativeParamaters.getLong( "value" );
    	
    }
    
    public void ping() {
    	
    	BrokerMessage request = BrokerMessage.buildRequest( TestService.SERVICE_NAME, "ping");
    	
    	_service.process( request );
    			
    }
    

}
