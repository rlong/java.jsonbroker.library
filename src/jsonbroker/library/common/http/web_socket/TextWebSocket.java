package jsonbroker.library.common.http.web_socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import jsonbroker.library.common.auxiliary.InputStreamHelper;
import jsonbroker.library.common.auxiliary.OutputStreamHelper;
import jsonbroker.library.common.auxiliary.StringHelper;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.log.Log;

public class TextWebSocket {

	private static Log log = Log.getLog(TextWebSocket.class);

	
	Socket _socket;
	InputStream _inputStream;
	OutputStream _outputStream;

	public TextWebSocket( Socket socket ) {
		_socket = socket;
		try {
			_inputStream = socket.getInputStream();
			_outputStream = socket.getOutputStream();
		} catch (IOException e) {
			throw new BaseException( this, e);
		}		
	}

	
	public void close() {
		
		try {
			_socket.close();
		} catch (IOException e) {
			throw new BaseException( this, e);
		}
		InputStreamHelper.close( _inputStream, false, this );
		OutputStreamHelper.close( _outputStream, false, this );
	}


	public String recieveTextFrame() {
		
		byte[] textFrameBytes = recieveTextFrameBytes();
		if( null == textFrameBytes ) {
			return null;
		}
		String answer = new String( textFrameBytes );
		return answer;
	}
	
	public byte[] recieveTextFrameBytes() {
		
		Frame frame = Frame.readFrame( _inputStream );
		if( null == frame ) {
			return null;
		}
		
		// browser is letting us know that it's closing the connection
		if( Frame.OPCODE_CONNECTION_CLOSE == frame._opCode ) {
			
			// vvv http://tools.ietf.org/html/rfc6455#section-5.5.1
			sendCloseFrame();
			// ^^^ http://tools.ietf.org/html/rfc6455#section-5.5.1
			
			return null;
		}
		
		byte[] answer = new byte[ frame._payloadLength ];
		InputStreamHelper.read( _inputStream, answer, this);
		
		frame.applyMask( answer );
		
		return answer;
	}
	
	public void sendCloseFrame() {
		
		log.enteredMethod();
		Frame frame = new Frame( Frame.OPCODE_CONNECTION_CLOSE, 0 );
		frame.write( _outputStream );		
	}


	public void sendTextFrame( String text ) {
		
		
		byte[] utfBytes = StringHelper.toUtfBytes( text );
		
		Frame frame = new Frame( Frame.OPCODE_TEXT_FRAME, utfBytes.length );
		frame.write( _outputStream );
		
		OutputStreamHelper.write( _outputStream, utfBytes, this);
		OutputStreamHelper.flush( _outputStream, false, this);
	}


}
