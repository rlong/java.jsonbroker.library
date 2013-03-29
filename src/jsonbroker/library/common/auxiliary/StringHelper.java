// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.auxiliary;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import jsonbroker.library.common.exception.BaseException;




public class StringHelper {
	
    static final char[] _hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String getUtf8String( byte[] buffer, int offset, int length ) { 
		
		try {
			return new String( buffer, offset, length, "UTF-8" );
		} catch (UnsupportedEncodingException e) {
			throw new BaseException( StringHelper.class , e );
		}
	}
	
	public static String getUtf8String( Data data ) {
		byte[] buffer =  data.getBytes();
		int length = data.getCount();
		return getUtf8String( buffer, 0, length);
	}
	
	public static byte[] toUtfBytes( String string ) {
		
		try {
			return  string.getBytes( "UTF-8" );
		} catch (UnsupportedEncodingException e) {
			throw new BaseException( StringHelper.class, e );
		}

	}
	
	
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
    
    
    // as per javascript
    public static String decodeURIComponent(String encodedURIComponent) {
    	String answer;
    	try {
			answer = URLDecoder.decode( encodedURIComponent, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new BaseException( StringHelper.class, e);
		}
		return answer;
    }    
	
	

}
