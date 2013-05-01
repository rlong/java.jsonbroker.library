// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.auxiliary;

import java.io.IOException;
import java.io.InputStream;

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

}
