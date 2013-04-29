// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.channel.endpoint;

import jsonbroker.library.common.broker.BrokerMessage;
import jsonbroker.library.common.channel.Channel;
import jsonbroker.library.common.json.JsonArray;
import jsonbroker.library.common.json.JsonArrayHelper;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.server.broker.DescribedService;
import jsonbroker.library.server.broker.ServicesRegistery;
import jsonbroker.library.server.channel.ChannelEndpoint;
import jsonbroker.library.server.channel.ChannelRequest;

public class JsonBrokerEndpoint implements ChannelEndpoint {
	
	
	private static final Log log = Log.getLog(JsonBrokerEndpoint.class);
	
	private static final byte[] NEWLINE = { '\n' };

	ServicesRegistery _servicesRegistery;

	public JsonBrokerEndpoint() {
		_servicesRegistery = new ServicesRegistery();
	}
	
	public void addService( DescribedService service ) {
		_servicesRegistery.addService( service );
	}


	public void handleRequest( Channel channel, ChannelRequest channelRequest  ) {
		
		
		
		byte[] endpointResponseHeader;
		
		// do some stuff ...
		{
			JsonArray brokerMessageArray = channelRequest.getEndpointHeaderArray();
			BrokerMessage brokerRequest = new BrokerMessage( brokerMessageArray );
			BrokerMessage brokerResponse = _servicesRegistery.process( brokerRequest );
			
			endpointResponseHeader = JsonArrayHelper.toBytes( brokerResponse.toJsonArray() );
		}

		JsonArray channelHeader = channelRequest.getChannelHeader();

		// prepare the response ...
		{			
			channelHeader.set( 4, endpointResponseHeader.length);
			channelHeader.add( 200 ); // 200: OK
		}
		
		// write the response ...
		{
			byte[] channelHeaderBytes = JsonArrayHelper.toBytes( channelHeader );
			channel.write( channelHeaderBytes );
			channel.write( NEWLINE );
			channel.write( endpointResponseHeader );
			channel.write( NEWLINE );
			
		}
		
		
		
		
		
			
		
		
		
	}

	
}
