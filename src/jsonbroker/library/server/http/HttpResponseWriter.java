// Copyright (c) 2013 Richard Long & HexBeerium
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
		
		log.enteredMethod();
		
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

			if( 204 != statusCode ) {
				
				log.warnFormat("null == entity && 204 != statusCode; statusCode = %d", statusCode);
				statusLineAndHeaders.append( "Content-Length: 0\r\n");
				
			} else {
				// from ... 
				// http://stackoverflow.com/questions/912863/is-an-http-application-that-sends-a-content-length-or-transfer-encoding-with-a-2
				// ... it would 'appear' safest to not include 'Content-Length' on a 204
			}
			
			statusLineAndHeaders.append("Accept-Ranges: bytes\r\n\r\n");
			
			byte[] utfBytes = StringHelper.toUtfBytes( statusLineAndHeaders.toString());
			OutputStreamHelper.write( utfBytes, outputStream, HttpResponseWriter.class);

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
		OutputStreamHelper.write( headersUtf8Bytes, outputStream, HttpResponseWriter.class);
		
		////////////////////////////////////////////////////////////////////////
		// write the entity
		
		entity.writeTo( outputStream , seekPosition, amountToWrite);
		OutputStreamHelper.flush( outputStream, true, HttpResponseWriter.class);
		
	}
	
	public static void writeResponse(HttpResponse response, OutputStream outputStream ) {
		
		try {
			
			tryWriteResponse( response, outputStream);
			
		} finally {
			Entity entity = response.getEntity();
			if( null != entity ) {
				entity.teardownForCaller( false, HttpResponseWriter.class );				
			}
		}
	}

}
