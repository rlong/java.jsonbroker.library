// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.http;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.exception.ErrorCodeUtilities;
import jsonbroker.library.common.log.Log;


public class WebServer implements Runnable{
	
	private static final int BASE = ErrorCodeUtilities.getBaseErrorCode("jsonbroker.ConnectionListener");
	
	private static final int SOCKET_BIND_FAILED = BASE | 0x01;
	

	private static final Log log = Log.getLog(WebServer.class);
	
	////////////////////////////////////////////////////////////////////////////
	//
	private ConnectionPolicy _connectionPolicy;
	
	
	public void setConnectionPolicy(ConnectionPolicy connectionPolicy) {
		_connectionPolicy = connectionPolicy;
	}

	////////////////////////////////////////////////////////////////////////////
	//
	private int _port;
	
	public int getPort() {
		return _port;
	}
	

	////////////////////////////////////////////////////////////////////////////
	//
	private RequestHandler _httpProcessor;

	////////////////////////////////////////////////////////////////////////////
	//
	ServerSocket _serverSocket;
	
	////////////////////////////////////////////////////////////////////////////
	//
	public WebServer(int port, RequestHandler httpProcessor) {
		
		_port = port;
		_httpProcessor = httpProcessor;
		
	}

	public WebServer( RequestHandler httpProcessor) {
		this( 8081, httpProcessor );
	}

	////////////////////////////////////////////////////////////////////////////
	//
	
	private synchronized void closeServerSocket() {
		
		if( null == _serverSocket ) {
			log.debug( "null == _serverSocket" );			
			return;
		}
		
		if( _serverSocket.isClosed() ) {
			log.debug( "serverSocket.isClosed()" );
			_serverSocket = null;
			return;			
		}
		
		try {
			_serverSocket.close();
			
		} catch (IOException e) {
			log.warn( e );
		}
		_serverSocket = null;

	}
	
	

	private static void closeClientSocket( Socket clientSocket ) {
		
		try {

			clientSocket.close();
			
		} catch( Throwable t ) {
			log.error( t );
		}
		
	}

	@Override
	public void run() {
		
		
		log.infoFormat("about to start listening for connections on port %s", _port);
		
		try {
			
			while (null != _serverSocket) {
				
				ServerSocket serverSocket;
				// make a local copy of the serverSocket on the stack
				synchronized (this) {
					
					serverSocket = _serverSocket;					
					
				}
				
				if( null != serverSocket ) {
					
					// Accept a new connection from the net, blocking till one comes in				
					Socket clientSocket = serverSocket.accept();
					
					boolean clientSocketAccepted = true;
					
					if( null != _connectionPolicy ) {
						clientSocketAccepted = _connectionPolicy.accept( clientSocket); // can set `clientSocket` to null 
					}
					
					if( clientSocketAccepted ) { 
						ConnectionHandler.handleConnection( clientSocket,_httpProcessor);
					} else {
						closeClientSocket( clientSocket );
					}
				}
			}
			
		} catch (IOException e) {
			log.warn( e );
		} finally {
			closeServerSocket();
		}
		log.infoFormat( "stopped listening for connections on port %s", _port);
		
		
	}
	
	public void start() { 
		
		try {
			_serverSocket = new ServerSocket(_port);
		} catch( BindException e ) {
			
			BaseException be = new BaseException( this, e);
			be.setFaultCode( SOCKET_BIND_FAILED );
			throw be;
			
		} catch (IOException e) {
			throw new BaseException( this, e);
		}
		
		Thread t = new Thread( this, "WebServer:" + _port );
		
		t.start();
	}
	
	public void stop() {
		
		if( null == _serverSocket ) {
			// shouldn't be calling stop on an instance that is not listening
			log.warn( "null == _serverSocket" );
			return;			
		}
		
		closeServerSocket();
		
	}
	
	

}
