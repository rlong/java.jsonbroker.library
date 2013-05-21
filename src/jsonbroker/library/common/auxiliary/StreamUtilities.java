// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.auxiliary;


import jsonbroker.library.common.exception.ErrorCodeUtilities;


public class StreamUtilities {
	

	private static final int BASE_ERROR_CODE = ErrorCodeUtilities.getBaseErrorCode("jsonbroker.StreamUtilities");
	public static final int IOEXCEPTION_ON_STREAM_WRITE = BASE_ERROR_CODE | 0x01;

}
