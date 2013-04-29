// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.auxiliary;

public class ByteHelper {

    static final char[] _hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String toHexString(byte[] bytes)
	{
		int count = bytes.length;
	
	    StringBuilder answer = new StringBuilder(count * 2);
	
	    for (int i = 0; i < count; i++)
	    {
	        byte b = bytes[i];
	
	        answer.append(_hexDigits[(b >> 4)& 0xf]);
	        answer.append(_hexDigits[b & 0xf]);
	    }
	    return answer.toString();
	}

}
