// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import jsonbroker.library.common.auxiliary.InputStreamHelper;
import jsonbroker.library.common.auxiliary.OutputStreamHelper;
import jsonbroker.library.common.auxiliary.StreamUtilities;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.http.HttpStatus;
import jsonbroker.library.common.log.Log;


public class ConnectionHandler implements Runnable{

	
	private static Log log = Log.getLog(ConnectionHandler.class);
	
	
	private static int _connectionId = 1;
	
	
	////////////////////////////////////////////////////////////////////////////
	//
	Delegate _delegate;
	
	
	////////////////////////////////////////////////////////////////////////////
	//
	private Socket _socket;
	
	////////////////////////////////////////////////////////////////////////////
	//
	private InputStream _inputStream;
	
	////////////////////////////////////////////////////////////////////////////
	//
	private OutputStream _outputStream;
	
	
	////////////////////////////////////////////////////////////////////////////
	private ConnectionHandler(Socket socket, RequestHandler httpProcessor) {
		
		_socket = socket;
		
		try {
			
			_socket.setSoLinger( true, 60);
			
			_inputStream = _socket.getInputStream();
			_outputStream = _socket.getOutputStream();
		} catch (IOException e) {
			throw new BaseException(this, e);
		}
		_delegate = new HttpDelegate( httpProcessor );
	}
	
	
	private void processRequests() {

		try {
			while( null != _delegate ) {				
				_delegate = _delegate.processRequest( _socket, _inputStream, _outputStream );
			}
		}
		catch( Throwable t ) {
			log.warn( t );
		}
		
        log.debug("finishing");

		if( _socket.isConnected() ) {
            OutputStreamHelper.flush(_outputStream, true, this);            
		}  


		try {
			
			if( _socket.isConnected() ) {
				
				// vvv http://stackoverflow.com/questions/2208387/java-socket-outputstream-is-not-flushing
				_socket.shutdownOutput();
				// ^^^ http://stackoverflow.com/questions/2208387/java-socket-outputstream-is-not-flushing
				
				_socket.close();

			} else {
				log.debug("!_socket.isConnected()");
			}
			
            InputStreamHelper.close(_inputStream, true, this);
            OutputStreamHelper.close(_outputStream, true, this);

            
		} catch (IOException e) {
			log.warn( e );
		}
	}
	
	@Override
	public void run() {
		
		try { 
			processRequests();
		} catch( Throwable t) {
			log.error( t );
		}
		
		log.debug( "finished" );

	}

	
	public static void handleConnection( Socket socket, RequestHandler httpProcessor ) {
		
		log.enteredMethod();
		
		ConnectionHandler connectionHandler = new ConnectionHandler(socket,httpProcessor);
		
		Thread thread = new Thread(connectionHandler, "ConnectionHandler." + _connectionId++);
		
		thread.start();
	}
	
	
	public interface Delegate {
		
		
		public Delegate processRequest( Socket socket, InputStream inputStream, OutputStream outputStream);

	}

}
