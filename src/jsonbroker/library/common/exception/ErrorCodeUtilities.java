// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.exception;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import jsonbroker.library.common.log.Log;
import jsonbroker.library.common.security.SecurityUtilities;

public class ErrorCodeUtilities {
	
	
	private static final Log log = Log.getLog(ErrorCodeUtilities.class);
	
	public static int getBaseErrorCode( String errorKey ) {
		
		MessageDigest digester = null;
		
		try {
			digester = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return 0;
		}
    	
		digester.update( errorKey.getBytes() );
		byte[] hash = digester.digest();
		
		
		int answer = 0xFF & hash[0];
		answer <<= 8;
		answer |= 0xFF & hash[1];
		answer <<= 4;
		answer |= (0xF0 & hash[2])>>4;
		answer <<= 8;
		
		log.infoFormat( "'%s' -> %x", errorKey, answer);
		
		return answer;
	}
	
	
	public static void main( String args[] ) {
		
		int baseErrorCode = getBaseErrorCode( "test2" );
		System.out.println( String.format("baseErrorCode = %x", baseErrorCode ));
		System.out.println( SecurityUtilities.md5HashOfString( "test2" ) );
	}

}
