// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.channel;

import jsonbroker.library.common.channel.Channel;
import jsonbroker.library.common.json.JsonArray;
import jsonbroker.library.common.json.JsonObject;

public interface ChannelEndpoint {

	public void handleRequest( Channel channel, JsonArray endpointRequest, ChannelResponse channelResponse  );
	public void handleRequest( Channel channel, JsonObject endpointRequest, ChannelResponse channelResponse );
	
}
