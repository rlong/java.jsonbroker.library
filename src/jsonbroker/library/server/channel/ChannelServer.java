// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.channel;

import jsonbroker.library.common.channel.Channel;
import jsonbroker.library.common.log.Log;

public class ChannelServer implements Runnable {

	private static final Log log = Log.getLog(ChannelServer.class);
	


	ServerChannel _serverChannel;	
	ChannelEndpoint _channelEndpoint;
	
	public ChannelServer( ServerChannel serverChannel, ChannelEndpoint channelEndpoint ) {
		_serverChannel = serverChannel;
		_channelEndpoint = channelEndpoint;
	}

	
	@Override
	public void run() {

		log.info("'accept'ing connections" );
		
		try {
			
			while (null != _serverChannel) {
				
				ServerChannel serverChannel;
				// make a local copy on the stack
				synchronized (this) {
					
					serverChannel = _serverChannel;					
				}
				
				if( null != serverChannel ) {
					
					Channel channel = serverChannel.accept();
					ChannelHandler.handleConnection( channel, _channelEndpoint);
				}
			}
		} finally {
			_serverChannel.close( true );
		}
		log.info( "stopped 'accept'ing connections" );
		
	}
	
	
	public void start() { 
		
		Thread t = new Thread( this, "ChannelServer" );
		
		t.start();
	}
	
	public void stop() {
		
		if( null == _serverChannel ) {
			log.warn( "null == _serverChannel" );
			return;			
		}
		
		_serverChannel.close( true );
		_serverChannel = null;
		
		
	}

	
	
}
