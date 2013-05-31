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
import jsonbroker.library.common.log.Log;


public class HttpConnectionHandler implements Runnable{

	
	private static Log log = Log.getLog(HttpConnectionHandler.class);
	
	
	private static int _connectionId = 1;
	
	
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
	private RequestHandler _httpProcessor;
	
	
	////////////////////////////////////////////////////////////////////////////
	private HttpConnectionHandler(Socket socket, RequestHandler httpProcessor) {
		
		_socket = socket;
		_httpProcessor = httpProcessor;
		
		try {
			
			_socket.setSoLinger( true, 60);
			
			_inputStream = _socket.getInputStream();
			_outputStream = _socket.getOutputStream();
		} catch (IOException e) {
			throw new BaseException(this, e);
		}		
	}
	
	
	private HttpRequest readRequest() 
	{ 
		HttpRequest answer = null;
		
		try {
			answer = HttpRequestReader.readRequest(_inputStream);
			
		} catch( Exception e ) {
			log.warn( e );
		}
		
		return answer;

	}
	
	private HttpResponse processRequest(HttpRequest request) {
		
		try {
			return _httpProcessor.processRequest( request );
			
		} catch( Throwable t ) {
			log.warn( t );
			return HttpErrorHelper.toHttpResponse( t );
		}
		
	}
	
	private boolean writeResponse( HttpResponse response ) {
		
		if( _socket.isOutputShutdown() ) {
			log.warn( "_socket.isOutputShutdown()" );
			return false;
		}  
			
		if( _socket.isClosed() ) {
			log.warn( "_socket.isClosed()" );
			return false;
		}  
		
		if( !_socket.isConnected() ) {
			log.warn( "!_socket.isConnected()" );
			return false;
		}  

		try {			
			HttpResponseWriter.writeResponse(response,_outputStream );
		} catch( BaseException e ) {
			
			if( e.getFaultCode() == StreamUtilities.IOEXCEPTION_ON_STREAM_WRITE ) {
				log.warn("IOException raised while writing response (socket closed ?)");
				return false;
			} else {
				log.warn( e );
				return false;
			}
		} catch ( Throwable t ) {
			log.warn( t );
			return false;
		}
		return true;
	}
	
	
	private void logRequestResponse(HttpRequest request, HttpResponse response, boolean writeResponseSucceded) {
		
		int statusCode = response.getStatus();
		
		String requestUri = request.getRequestUri();
		
		long contentLength = 0;
		if( null != response.getEntity() ) {
			contentLength = response.getEntity().getContentLength();
            if (null != response.getRange())
            {
                contentLength = response.getRange().getContentLength(contentLength);
            }
		}
		
		long timeTaken = System.currentTimeMillis() - request.getCreated();

        String completed;
        if (writeResponseSucceded)
        {
            completed = "true";
        } else 
        {
            completed = "false";
        }

        String rangeString;
        {
            if (null == response.getRange())
            {
                rangeString = "bytes";
            }
            else
            {
                rangeString = response.getRange().toContentRange(response.getEntity().getContentLength());
            }
        }

        log.infoFormat("status:%d uri:%s content-length:%d time-taken:%d completed:%s range:%s", statusCode, requestUri, contentLength, timeTaken, completed, rangeString);

		
	}
	
	private boolean processRequest() {
		
		// get the request ... 
		HttpRequest request = readRequest();
		if( null == request ) {
			log.debug("null == request");
			return false;
		}
		
		// process the request ... 
		HttpResponse response = processRequest(request); 

		boolean continueProcessingRequests = true;
		
		// vvv http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.10
        if( request.isCloseConnectionIndicated() ) {  

            continueProcessingRequests = false;
        }

		// ^^^ http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.10
		
		int statusCode = response.getStatus();
        if (statusCode < 200 || statusCode > 299) {
            continueProcessingRequests = false;
        }
        
        if( continueProcessingRequests ) {
        	response.putHeader( "Connection", "keep-alive" );
        } else {
        	response.putHeader( "Connection", "close" );
        	
        }
        
		// write the response ... 
		boolean writeResponseSucceded = writeResponse(response);

	    // do some logging ...				
		logRequestResponse(request, response, writeResponseSucceded);
		
        if (!writeResponseSucceded)
        {
        	continueProcessingRequests = false;
        }

        // if the processing completed, we will permit more requests on this socket
        return continueProcessingRequests;
		
	}
	
	private void processRequests() {

		try {
			while(processRequest()) {
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
		
		HttpConnectionHandler connectionHandler = new HttpConnectionHandler(socket,httpProcessor);
		
		Thread thread = new Thread(connectionHandler, "ConnectionHandler." + _connectionId++);
		
		thread.start();
	}
}
