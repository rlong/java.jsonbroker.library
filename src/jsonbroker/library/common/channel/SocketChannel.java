// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.channel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.log.Log;

public class SocketChannel implements Channel {
	
	
	private static final Log log = Log.getLog(SocketChannel.class);

	Socket _socket;
	InputStream _inputStream;
	OutputStream _outputStream;
	
	

	public SocketChannel( Socket socket ) {
		
		try {
			
			_socket = socket;
			_inputStream = _socket.getInputStream();
			_outputStream = _socket.getOutputStream();
			
		} catch (UnknownHostException e) {
			throw new BaseException(this, e);
		} catch (IOException e) {
			throw new BaseException(this, e);
		}
	}

	
	public SocketChannel( String dstName, int dstPort ) {
		
		try {
			
			_socket = new Socket(dstName, dstPort);
			_inputStream = _socket.getInputStream();
			_outputStream = _socket.getOutputStream();
			
		} catch (UnknownHostException e) {
			throw new BaseException(this, e);
		} catch (IOException e) {
			throw new BaseException(this, e);
		}
	}
	
	@Override	
	public void close(boolean ignoreErrors) {
		try {
			_socket.close();
		} catch (IOException e) {
			if( ignoreErrors ){
				log.warn( e );
			} else {
				throw new BaseException(this, e);
			}
		}
	}
	
	@Override
	public String readLine() {
		
		String answer = ChannelHelper.readLine( _inputStream, this );
		return answer;
	}
	
	@Override
	public void write( byte[] bytes ) {
		ChannelHelper.write( _outputStream, bytes );		
	}
	
	@Override
	public void write( String line ) {
		
		ChannelHelper.write( _outputStream, line);		
	}
	
	@Override
	public void writeLine( String line ) {
		ChannelHelper.writeLine( _outputStream, line);
	}
	

}
