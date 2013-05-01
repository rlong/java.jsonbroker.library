// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.channel.endpoint;

import jsonbroker.library.common.broker.BrokerMessage;
import jsonbroker.library.common.channel.Channel;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.json.JsonArray;
import jsonbroker.library.common.json.JsonObject;
import jsonbroker.library.server.broker.DescribedService;
import jsonbroker.library.server.broker.ServicesRegistery;
import jsonbroker.library.server.channel.ChannelEndpoint;
import jsonbroker.library.server.channel.ChannelResponse;

public class JsonBrokerEndpoint implements ChannelEndpoint {
	
	
	public static final String ENDPOINT_NAME = "jsonbroker.JsonbrokerEndpoint";

	
	ServicesRegistery _servicesRegistery;

	public JsonBrokerEndpoint() {
		_servicesRegistery = new ServicesRegistery();
	}
	
	public void addService( DescribedService service ) {
		_servicesRegistery.addService( service );
	}

	
	public void handleRequest( Channel channel, JsonArray endpointRequest, ChannelResponse channelResponse  ) {
		
		BrokerMessage brokerRequest = new BrokerMessage( endpointRequest );
		BrokerMessage brokerResponse = _servicesRegistery.process( brokerRequest );
		JsonArray endpointResponse = brokerResponse.toJsonArray();
		
		channelResponse.writeResponse( channel, endpointResponse);
	}

	public void handleRequest( Channel channel, JsonObject endpointRequest, ChannelResponse channelResponse ) {
		throw new BaseException( this, "endpoint '"+ENDPOINT_NAME+"' does not support requests of type 'JsonObject'" );
		
	}

	
}
