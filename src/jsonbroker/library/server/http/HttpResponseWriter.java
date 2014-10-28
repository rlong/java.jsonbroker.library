// Copyright (c) 2014 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.http;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import jsonbroker.library.common.auxiliary.OutputStreamHelper;
import jsonbroker.library.common.auxiliary.StringHelper;
import jsonbroker.library.common.http.Entity;
import jsonbroker.library.common.http.HttpStatus;
import jsonbroker.library.common.http.headers.request.Range;
import jsonbroker.library.common.log.Log;


public class HttpResponseWriter {
	
	private static final Log log = Log.getLog(HttpResponseWriter.class);
	
	public static void tryWriteResponse(HttpResponse response, OutputStream outputStream ) {
		
		
		int statusCode = response.getStatus();
		String statusString = HttpStatus.getReason( statusCode );
		
		StringBuilder statusLineAndHeaders = new StringBuilder();
		statusLineAndHeaders.append( String.format( "HTTP/1.1 %d %s\r\n", statusCode, statusString ));

		HashMap<String, String> headers = response.getHeaders();
		for (Map.Entry<String, String> item : headers.entrySet()) {

			String key = item.getKey();
			String value = item.getValue();
			statusLineAndHeaders.append( String.format( "%s: %s\r\n", key, value ));
		}
		
		
		Entity entity = response.getEntity();
		
		////////////////////////////////////////////////////////////////////////
		// no entity 
		
		if( null == entity ) {

			
			if( 101 == statusCode ) {
				
				// 'Switching Protocols'
				statusLineAndHeaders.append("\r\n");
				
			} else {
				
				if( 204 == statusCode ) {
					
					// from ... 
					// http://stackoverflow.com/questions/912863/is-an-http-application-that-sends-a-content-length-or-transfer-encoding-with-a-2
					// ... it would 'appear' safest to not include 'Content-Length' on a 204

				} else if( 304 == statusCode ) {
					
					// http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.3.5
					// http://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#sec4.3
					
				} else {
					
					log.warnFormat("null == entity; statusCode = %d", statusCode);
					statusLineAndHeaders.append( "Content-Length: 0\r\n");
					
				} 
				
				statusLineAndHeaders.append("Accept-Ranges: bytes\r\n\r\n");
			}
			
			byte[] utfBytes = StringHelper.toUtfBytes( statusLineAndHeaders.toString());
			OutputStreamHelper.write( outputStream, utfBytes, HttpResponseWriter.class);

			return; // our work is done
		}
		
		////////////////////////////////////////////////////////////////////////
		// has entity 
		
		long entityContentLength = entity.getContentLength();
		long seekPosition = 0;
		Long amountToWrite = entityContentLength;
		
		////////////////////////////////////////////////////////////////////////
		// headers relevant to range support
		Range range = response.getRange();
		
		if( null == range ) {

			statusLineAndHeaders.append( "Accept-Ranges: bytes\r\n");

			if( HttpStatus.PARTIAL_CONTENT_206 == statusCode ) {
				log.warn("null == range && HttpStatus.PARTIAL_CONTENT_206 == statusCode");
			}
			
		} else {
			
			String contentRangeHeader = String.format( "Content-Range: %s\r\n", range.toContentRange(entityContentLength));			
			statusLineAndHeaders.append( contentRangeHeader );
			
			amountToWrite = range.getContentLength(entityContentLength);
			seekPosition = range.getSeekPosition( entityContentLength );
			
            if (HttpStatus.PARTIAL_CONTENT_206 != statusCode)
            {
                log.warn("null != range && HttpStatus.PARTIAL_CONTENT_206 != statusCode");
            }

		}
		
		////////////////////////////////////////////////////////////////////////
		// content-length and final newline 
		statusLineAndHeaders.append( String.format( "Content-Length: %d\r\n\r\n", amountToWrite));
		
		////////////////////////////////////////////////////////////////////////
		// write the headers 

		
		byte[] headersUtf8Bytes = StringHelper.toUtfBytes( statusLineAndHeaders.toString());
		OutputStreamHelper.write( outputStream, headersUtf8Bytes, HttpResponseWriter.class);
		
		////////////////////////////////////////////////////////////////////////
		// write the entity
		
		entity.writeTo( outputStream , seekPosition, amountToWrite);
		OutputStreamHelper.flush( outputStream, true, HttpResponseWriter.class);
		
	}
	
	public static void writeResponse(HttpResponse response, OutputStream outputStream ) {
		
		tryWriteResponse( response, outputStream);
			
	}

}
