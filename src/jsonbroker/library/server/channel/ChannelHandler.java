// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.channel;

import jsonbroker.library.common.channel.Channel;
import jsonbroker.library.common.log.Log;

public class ChannelHandler implements Runnable {

	private static final Log log = Log.getLog(ChannelHandler.class);

	private static int _threadId = 1;

	Channel _channel;	
	ChannelEndpoint _channelEndpoint;
	
	private ChannelHandler( Channel channel, ChannelEndpoint channelEndpoint ) {
		
		_channel = channel;
		_channelEndpoint = channelEndpoint;
		
	}
	
	private boolean processRequest() {
		
		ChannelRequest request = ChannelRequest.readRequest( _channel );
		if( null == request ) {
			return false;
		}
		
		_channelEndpoint.handleRequest( _channel, request );
		
		return true;
		
	}
	
	private void processRequests() {
		
		try { 
			while( processRequest() ) {
			}
		} catch( Throwable t) {
			log.error( t );
		}
		
		
	}
	
	@Override
	public void run() {
		
		try { 
			processRequests();
		} catch( Throwable t) {
			log.error( t );
		}
		
		
		try { 
			_channel.close(true);
		} catch( Throwable t) {
			log.error( t );
		}
		
		log.debug( "finished" );

	}

	
	public static void handleConnection( Channel channel, ChannelEndpoint channelEndpoint ) {
		
		log.enteredMethod();
		
		ChannelHandler channelHandler = new ChannelHandler(channel,channelEndpoint);
		
		Thread thread = new Thread(channelHandler, "ChannelHandler." + _threadId++);
		
		thread.start();
	}

	
}
