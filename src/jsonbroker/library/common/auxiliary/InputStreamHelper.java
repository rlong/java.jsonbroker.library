// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.auxiliary;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.log.Log;

public class InputStreamHelper {
	
	
	private static final Log log = Log.getLog(InputStreamHelper.class);


	
	public static final int available( InputStream inputStream, Object caller ) {
		
		try {
			return inputStream.available();
		} catch (IOException e) {
			throw new BaseException( caller, e);
		}		
	}
	
	public static final int read( InputStream inputStream, byte[] destination, Object caller ) {
		try {
			return inputStream.read(destination);			
		} catch (IOException e) {
			throw new BaseException( caller, e);
		}
	}

	public static final int read( InputStream inputStream, byte[] destination, int offset, int length, Object caller ) {
		try {
			return inputStream.read( destination, offset, length );
		} catch (IOException e) {
			throw new BaseException( caller, e);
		}
	}

	
	/**
	 * 
	 * @param inputStream
	 * @param caller
	 * 
	 * @return the next byte of data, or -1 if the end of the stream is reached
	 */
	public static final int readByte( InputStream inputStream, Object caller ) {
	
		try {
			return inputStream.read();
		} catch (IOException e) {
			throw new BaseException( caller, e);
		}
	}

	public static void close( InputStream inputStream, boolean swallowException, Object caller ) {
		
		if( null == inputStream ) {
			return;
		}
		
		try {
			inputStream.close();
		} catch (IOException e) {
			if( swallowException ) {
				log.warn( e.getMessage(), "e.getMessage()" );			
			} else {
				throw new BaseException( caller, e);
			}			
		}
	}

	public static void close( InputStream inputStream , Object caller ) {
		
		if( null == inputStream ) {
			return;
		}
		
		try {
			inputStream.close();
		} catch (IOException e) {
			throw new BaseException( caller, e);
		}
	}

	public static void skip( long count, InputStream inputStream, Object caller  ) {
		
		if( null == inputStream ) {
			return;
		}
		
		try {
			inputStream.skip( count );
		} catch (IOException e) {
			throw new BaseException( caller, e);
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

	public static void write( InputStream inputStream, long count, OutputStream outputStream ) {
		
		
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
			
			if( -1 == bytesRead ) {
				throw new BaseException( InputStreamHelper.class, "-1 == bytesRead");
			}
			
			try {
				outputStream.write( buffer, 0, bytesRead );
			} catch (IOException ioe) {					
				BaseException e = new BaseException( StreamUtilities.class, ioe);
				e.setFaultCode( StreamUtilities.IOEXCEPTION_ON_STREAM_WRITE );
				throw e;
			}		
	
			remaining -= bytesRead;
		}
	}



}
