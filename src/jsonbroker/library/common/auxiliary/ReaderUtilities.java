// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.auxiliary;

import java.io.IOException;
import java.io.Reader;

import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.log.Log;

public class ReaderUtilities {

	private static final Log log = Log.getLog(ReaderUtilities.class);
	
	public static void close( Reader reader, boolean swallowException, Object caller ) {
		
		if( null == reader ) {
			return;
		}
		
		try {
			reader.close();
		} catch (IOException e) {
			if( swallowException ) {
				log.warn( e.getMessage(), "e.getMessage()" );			
			} else {
				throw new BaseException( caller, e);
			}
		}
	}

}
