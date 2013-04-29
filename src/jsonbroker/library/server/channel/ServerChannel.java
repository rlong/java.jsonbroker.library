// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.channel;

import jsonbroker.library.common.channel.Channel;

public interface ServerChannel {

	// returns null if the channel has been 'close'd down
	public Channel accept();
	
	public void close(boolean ignoreErrors);
	

}
