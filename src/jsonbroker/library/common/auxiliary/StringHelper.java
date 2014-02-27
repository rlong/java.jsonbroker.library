// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.auxiliary;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import jsonbroker.library.common.exception.BaseException;




public class StringHelper {
	
	public static final String UTF_8 = "UTF-8";
	

	public static byte[] toUtfBytes( String string ) {
		
		try {
			return  string.getBytes( UTF_8 );
		} catch (UnsupportedEncodingException e) {
			throw new BaseException( StringHelper.class, e );
		}
	}
	
	
    // as per javascript
    public static String decodeURIComponent(String encodedURIComponent) {
    	String answer;
    	try {
			answer = URLDecoder.decode( encodedURIComponent, UTF_8 );
		} catch (UnsupportedEncodingException e) {
			throw new BaseException( StringHelper.class, e);
		}
		return answer;
    }    
	


}
