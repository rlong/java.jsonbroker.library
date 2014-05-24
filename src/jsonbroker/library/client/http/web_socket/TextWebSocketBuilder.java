package jsonbroker.library.client.http.web_socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import jsonbroker.library.common.auxiliary.DataHelper;
import jsonbroker.library.common.auxiliary.InputStreamHelper;
import jsonbroker.library.common.auxiliary.MutableData;
import jsonbroker.library.common.auxiliary.OutputStreamHelper;
import jsonbroker.library.common.auxiliary.RandomUtilities;
import jsonbroker.library.common.auxiliary.StringHelper;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.http.web_socket.TextWebSocket;
import jsonbroker.library.common.http.web_socket.WebSocketUtilities;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.common.security.SecurityUtilities;
import jsonbroker.library.server.http.HttpRequestReader;

public class TextWebSocketBuilder {
	
	private static Log log = Log.getLog(TextWebSocketBuilder.class);
	
	// null corresponds to the end of a stream
	private static String readLine(InputStream inputStream, MutableData buffer) {
		
		buffer.clear();
		
		int byteRead = InputStreamHelper.readByte(  inputStream, HttpRequestReader.class );
		
		if( -1 == byteRead ) { 
			return null;
		}
		
		int i = 0;
		
		while(true){ 
			
			// end of stream or end of the line
			if( -1 == byteRead || '\n' == byteRead ) {
				return DataHelper.toUtf8String( buffer );
			}
			
			// filter out '\r'
			if( '\r' != byteRead ) {
				buffer.append( (byte)byteRead );
			}

			byteRead = InputStreamHelper.readByte(  inputStream, HttpRequestReader.class);
			
			i++;
		}		
	}
	
	private static void writeLine( String line, OutputStream outputStream ) {
		
		byte[] utfBytes = StringHelper.toUtfBytes( line );
		OutputStreamHelper.write( outputStream, utfBytes, TextWebSocketBuilder.class);		
	}
	
	
	
	private static String buildSecWebSocketKey() {
		
		
    	byte[] bytes = new byte[16];
    	RandomUtilities.random( bytes );
    	String answer = SecurityUtilities.base64Encode( bytes );
    	log.debug( answer, "answer" );
    	
    	return answer;
    	
		
	}
	
	
	
	private static boolean isValidSecWebSocketAcceptHeader( String header, String secWebSocketAccept ) {
		
		String headerPrefix = "Sec-WebSocket-Accept: ";
		int indexOfPrefix = header.indexOf( headerPrefix );
		if( 0 != indexOfPrefix ) {
			return false;			
		}
		
		String value = header.substring( headerPrefix.length() );
		log.debug( value, "value" );
		if( !value.equals( secWebSocketAccept ) ) {
			log.warnFormat( "!value.equals( secWebSocketAccept ); value = '%s'; secWebSocketAccept = '%s'", value, secWebSocketAccept );
			return false;
		}
		return true;
	}
	
	// host can be null
	private static TextWebSocket tryBuild( Socket socket, String host, String path) throws IOException {

//		Socket socket = new Socket( host, port );
//		socket.setSoTimeout( 10 * 1000 );
		
		OutputStream os = socket.getOutputStream();
		InputStream is = socket.getInputStream();
		
		String secWebSocketKey = buildSecWebSocketKey();
		String secWebSocketAccept = WebSocketUtilities.buildSecWebSocketAccept( secWebSocketKey );
		log.debug( secWebSocketAccept, "secWebSocketAccept" );
		
		writeLine( "GET "+path+" HTTP/1.1\r\n", os);
		if( null != host ) {
			writeLine( "Host: "+host+"\r\n", os);
		}
		
		writeLine( "Upgrade: websocket\r\n", os);
		writeLine( "Connection: Upgrade\r\n", os);
		writeLine( "Sec-WebSocket-Version: 13\r\n", os);
		writeLine( "Sec-WebSocket-Key: "+secWebSocketKey+"\r\n", os);
		writeLine( "\r\n", os);
		OutputStreamHelper.flush( os, false, TextWebSocketBuilder.class );

		String header;
		MutableData buffer = new MutableData();
		boolean gotValidSecWebSocketAcceptHeader = false;
		do {
			header = readLine( is, buffer);
			log.debug( header, "header" );
			if( !gotValidSecWebSocketAcceptHeader ) {
				gotValidSecWebSocketAcceptHeader = isValidSecWebSocketAcceptHeader( header, secWebSocketAccept );
			}
		}		
		while( !"".equals( header ) );
		
		if( !gotValidSecWebSocketAcceptHeader ) {
			throw new BaseException( TextWebSocketBuilder.class, "!gotValidSecWebSocketAcceptHeader" );
		}
		
		TextWebSocket answer = new TextWebSocket(socket);
		return answer;

	}
		

	public static TextWebSocket build( Socket socket, String path ) {

		TextWebSocket answer;
		try {
			
			answer = tryBuild( socket, null, path );
		} catch (IOException e) {
			throw new BaseException( TextWebSocketBuilder.class, e);
		}
		return answer;

	}

	public static TextWebSocket build( String host, int port, String path ) {
		
		TextWebSocket answer;
		try {
			
			Socket socket = new Socket( host, port );
			socket.setSoTimeout( 10 * 1000 );
			
			
			answer = tryBuild( socket, host, path );
		} catch (IOException e) {
			throw new BaseException( TextWebSocketBuilder.class, e);
		}
		return answer;
		
	}

	public static void main( String args[] ) {
		
		log.enteredMethod();
		
		
		TextWebSocket webSocketConnection = TextWebSocketBuilder.build( "echo.websocket.org", 80, "/?encoding=text" );
		
		
		webSocketConnection.sendTextFrame( "hello web-sockets" );
		String response = webSocketConnection.recieveTextFrame();
		log.debug( response, "response" );

		log.info( "done" );
	}

}
