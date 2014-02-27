// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.auxiliary;

import java.io.IOException;
import java.io.OutputStream;

import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.log.Log;

public class OutputStreamHelper {

	private static final Log log = Log.getLog(OutputStreamHelper.class);

	public static void close( OutputStream outputStream, Object caller ) {
		
		if( null == outputStream ) {
			return;
		}
		
		try {
			outputStream.close();
		} catch (IOException e) {
			throw new BaseException( caller, e);
		}
	}

	public static void close( OutputStream outputStream, boolean swallowException, Object caller  ) {
		
		if( null == outputStream ) {
			return;
		}
		
		try {
			outputStream.close();
		} catch (IOException e) {
			if( swallowException ) {
				log.warn( e.getMessage(), "e.getMessage()" );			
			} else {
				throw new BaseException( caller, e);
			}			
		}
	}

	public static void write( byte b, OutputStream outputStream, Object caller ) {
		
		try {
			outputStream.write( b);
		} catch (IOException e) {
			throw new BaseException( caller, e);
		}
	
	}

	public static void write( byte[] bytes, OutputStream outputStream, Object caller ) {
		
		try {
			outputStream.write( bytes);
		} catch (IOException e) {
			throw new BaseException( caller, e);
		}
	
	}

	public static void flush( OutputStream outputStream, boolean swallowException, Object caller ) {
		
		if( null == outputStream ) {
			return;
		}
		
		try {
			outputStream.flush();
		} catch (IOException e) {
			if( swallowException ) {
				log.warn( e.getMessage(), "e.getMessage()" );			
			} else {
				throw new BaseException( caller, e);
			}			
		}
	}

}
