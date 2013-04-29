// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.channel;

import jsonbroker.library.common.channel.Channel;

public interface ChannelEndpoint {


	public void handleRequest( Channel channel, ChannelRequest channelRequest  );
	
}
