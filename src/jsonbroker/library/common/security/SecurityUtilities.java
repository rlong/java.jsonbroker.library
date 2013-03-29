// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import jsonbroker.library.common.auxiliary.Data;
import jsonbroker.library.common.auxiliary.RandomUtilities;
import jsonbroker.library.common.auxiliary.StringHelper;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.log.Log;

public class SecurityUtilities {
	
	private static final Log log = Log.getLog(SecurityUtilities.class);

    public static String generateNonce()
    {
        // TODO: FDB46896-A292-4F48-811C-88651F37B4F8
        // TODO: come up with a better nonce solution

    	return RandomUtilities.generateUuid();
    }
    
    static String generateNumericUserPassword( byte[] bytes ) {
    	
        StringBuffer answer = new StringBuffer(bytes.length);
        
        for( int i = 0, count = bytes.length; i < count; i++ ) {
        	int offset = bytes[i] % 10;
        	if( 0 > offset ) {
        		offset = -offset;
        	}
        	log.debug(offset, "offset");
        	answer.append( (char)('0' + offset) );
        }
        
        return answer.toString();

    }
    
    public static String generateNumericUserPassword(  ) {

    	byte[] bytes = new byte[8];
    	RandomUtilities.random( bytes );
    	return generateNumericUserPassword( bytes );

    }

    
    public static String md5HashOfBytes( byte[] input ) {

		MessageDigest digester = null;
		try {
			digester = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new BaseException( SecurityUtilities.class, e);
		}
    	
		digester.update( input );
		byte[] hash = digester.digest();
		
        //NSString* answer = [self bytesToString:hashBytes];
        String answer = StringHelper.toHexString( hash );

        return answer;

    }
    
    public static String md5HashOfData(Data input) {
    	
    	log.debug( input, "input" );
    	
    	byte[] bytes = input.getBytes();
    	return md5HashOfBytes( bytes );
    	
    }

    
	public static String md5HashOfString(String input) {
		
		byte[] utf8Value = StringHelper.toUtfBytes( input );
		
		return md5HashOfBytes( utf8Value );
		
	}
	


}
