// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.channel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import jsonbroker.library.common.channel.Channel;
import jsonbroker.library.common.channel.SocketChannel;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.log.Log;

public class SocketServerChannel implements ServerChannel {
	
	private static Log log = Log.getLog( SocketServerChannel.class );
	
	
	public static final int DEFAULT_PORT = 8082;
	
	ServerSocket _serverSocket;
	
	public SocketServerChannel( int port ) {
		
		
		try {
			_serverSocket = new ServerSocket( port );
		} catch (IOException e) {
			throw new BaseException(this, e);
		}
	}

	
	// returns null if the channel has been 'close'd down
	public Channel accept() {
		
		
		if( null == _serverSocket ) {
			return null;
		}

		if( _serverSocket.isClosed() ) {
			return null;
		}

		try {
			Socket socket = _serverSocket.accept();
			return new SocketChannel( socket );
			
		} catch (IOException e) {
			
			throw new BaseException(this, e);
			
		}
	}

	public void close( boolean ignoreErrors ) {
		
		if( null == _serverSocket ) {
			return;
		}

		if( _serverSocket.isClosed() ) {
			return;
		}
		
		try {
			_serverSocket.close();
			
		} catch (IOException e) {
			
			if( ignoreErrors ) {
				log.warn( e );					
			} else {
				throw new BaseException(this, e);
			}
		}
	}

}
