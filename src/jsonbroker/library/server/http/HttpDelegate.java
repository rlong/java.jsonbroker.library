// Copyright (c) 2014 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.http;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import jsonbroker.library.common.auxiliary.StreamUtilities;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.http.HttpStatus;
import jsonbroker.library.common.log.Log;


public class HttpDelegate implements ConnectionDelegate {

	
	private static Log log = Log.getLog(HttpDelegate.class);
	
	
	
	////////////////////////////////////////////////////////////////////////////
	//
	private RequestHandler _httpProcessor;
	
	
	////////////////////////////////////////////////////////////////////////////
	HttpDelegate( RequestHandler httpProcessor) {
		
		_httpProcessor = httpProcessor;
		
	}
	
	
	private HttpRequest readRequest( InputStream inputStream ) 
	{ 
		HttpRequest answer = null;
		
		try {
			answer = HttpRequestReader.readRequest(inputStream);
			
		} catch( Exception e ) {
			log.warn( e );
		}
		
		return answer;

	}
	
	private HttpResponse processRequest(HttpRequest request) {
		
		try {
			return _httpProcessor.processRequest( request );
			
		} catch( Throwable t ) {
			if( t instanceof BaseException )  {
				BaseException be = (BaseException)t;
				String errorDomain = be.getErrorDomain();
				if( HttpStatus.ErrorDomain.NOT_FOUND_404.equals( errorDomain ) ) {
					log.warnFormat( "errorDomain = '%s'; t.getMessage() = '%s'", errorDomain, t.getMessage() );
				} else {
					log.warn( t );
				}
			} else {
				log.warn( t );
			}
			
			return HttpErrorHelper.toHttpResponse( t );
		}
		
	}
	
	private boolean writeResponse( Socket socket, OutputStream outputStream, HttpResponse response ) {
		
		if( socket.isOutputShutdown() ) {
			log.warn( "socket.isOutputShutdown()" );
			return false;
		}  
			
		if( socket.isClosed() ) {
			log.warn( "socket.isClosed()" );
			return false;
		}  
		
		if( !socket.isConnected() ) {
			log.warn( "!socket.isConnected()" );
			return false;
		}  

		try {			
			HttpResponseWriter.writeResponse(response,outputStream );
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
	

	@Override
	public ConnectionDelegate processRequest( Socket socket, InputStream inputStream, OutputStream outputStream ) {
		
		// get the request ... 
		HttpRequest request = readRequest( inputStream );
		if( null == request ) {
			log.debug("null == request");
			return null;
		}
		
		// process the request ... 
		HttpResponse response = processRequest(request); 

		ConnectionDelegate answer = this;
		
		if( null != response._connectionDelegate ) {
			answer = response._connectionDelegate;
		}
		
		// vvv http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.10
        if( request.isCloseConnectionIndicated() ) {  

            answer = null;
        }

		// ^^^ http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.10
		
		int statusCode = response.getStatus();
        if ( statusCode > 399) {
            answer = null;
        }
        
        if( answer == this ) {
        	response.putHeader( "Connection", "keep-alive" );
        } else if( answer == null ) {
        	response.putHeader( "Connection", "close" );
        }
        
		// write the response ... 
		boolean writeResponseSucceded = writeResponse( socket, outputStream, response );

	    // do some logging ...				
		logRequestResponse(request, response, writeResponseSucceded);
		
        if (!writeResponseSucceded)
        {
        	answer = null;
        }

        // if the processing completed, we will permit more requests on this socket
        return answer;
		
	}
	
	
}
