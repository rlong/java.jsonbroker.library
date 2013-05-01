// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.auxiliary;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.exception.ErrorCodeUtilities;
import jsonbroker.library.common.log.Log;


public class StreamUtilities {
	

	private static final int BASE_ERROR_CODE = ErrorCodeUtilities.getBaseErrorCode("jsonbroker.StreamUtilities");
	public static final int IOEXCEPTION_ON_STREAM_WRITE = BASE_ERROR_CODE | 0x01;
	
	
	public static void write( long count, InputStream inputStream, OutputStream outputStream ) {
		
		
		int bufferSize = 4 * 1024;
		if( count < bufferSize ) {
			bufferSize = (int)count;
		} else {
			if( count > 1024 * 1024 ) {				
				bufferSize = 64 * 1024;
			}
		}
		
		byte[] buffer = new byte[bufferSize];
		
		long remaining = count;
		
		while( remaining > 0 ) {
			
			int bytesToWrite = buffer.length;
			if( bytesToWrite > remaining ) {
				bytesToWrite = (int)remaining;
			}
			
			int bytesRead;
			try {
				bytesRead = inputStream.read( buffer, 0, bytesToWrite );
			} catch (IOException e) {
				throw new BaseException( StreamUtilities.class, e);
			}		
				
			//log.debug( bytesRead, "bytesRead");
			if( -1 == bytesRead ) {
				throw new BaseException( StreamUtilities.class, "-1 == bytesRead");
			}
			
			try {
				outputStream.write( buffer, 0, bytesRead );
			} catch (IOException ioe) {					
				BaseException e = new BaseException( StreamUtilities.class, ioe);
				e.setFaultCode( IOEXCEPTION_ON_STREAM_WRITE );
				throw e;
			}		

			remaining -= bytesRead;
		}
	}

	// write until the end of the stream 
	public static void write( InputStream inputStream, OutputStream outputStream ) {
		
		byte[] buffer = new byte[4 * 1024];
		int bytesRead;
		
		try {
			while( -1 != (bytesRead = inputStream.read( buffer )) ) {
				outputStream.write( buffer, 0, bytesRead );
			}
		} catch (IOException e) {
			throw new BaseException( StreamUtilities.class, e);
		}

	}

}
