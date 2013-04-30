// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.auxiliary;

public class DataHelper {

	public static String toUtf8String( Data data ) {
		
		byte[] buffer =  data.getBytes();
		int length = data.getCount();
		return ByteHelper.toUtf8String( buffer, 0, length);
	}
	
}
