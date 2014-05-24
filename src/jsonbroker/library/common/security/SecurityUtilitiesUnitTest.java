// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.security;

import jsonbroker.library.common.auxiliary.StringHelper;
import jsonbroker.library.common.log.Log;
import junit.framework.TestCase;

public class SecurityUtilitiesUnitTest extends TestCase {

	
	
	private static final Log log = Log.getLog(SecurityUtilitiesUnitTest.class);

	public void test1() {
		log.enteredMethod();
	}
	
	public void testGenerateNumericUserPassword() {
		
		byte[] nonce = new byte[8];
		
		String userPassword = SecurityUtilities.generateNumericUserPassword( nonce );
		assertEquals( "00000000" , userPassword );
		
		nonce[1] = 11;
		userPassword = SecurityUtilities.generateNumericUserPassword( nonce );
		assertEquals( "01000000" , userPassword );


		nonce[2] = 22;
		userPassword = SecurityUtilities.generateNumericUserPassword( nonce );
		assertEquals( "01200000" , userPassword );

		nonce[3] = 33;
		userPassword = SecurityUtilities.generateNumericUserPassword( nonce );
		assertEquals( "01230000" , userPassword );

		nonce[4] = 44;
		userPassword = SecurityUtilities.generateNumericUserPassword( nonce );
		assertEquals( "01234000" , userPassword );

	}
	public void testGenerateNumericUserPasswordWithNegativeNonce() {
		
		byte[] nonce = new byte[8];
		
		String userPassword = SecurityUtilities.generateNumericUserPassword( nonce );
		assertEquals( "00000000" , userPassword );

		nonce[1] = -1;
		userPassword = SecurityUtilities.generateNumericUserPassword( nonce );
		assertEquals( "01000000" , userPassword );

	}
	
	
	public void testBase64Encode() {
		
		// vvv http://javarevisited.blogspot.com.au/2012/02/how-to-encode-decode-string-in-java.html
		
		byte[] bytes = StringHelper.toUtfBytes("original String before base64 encoding in Java");
		String actual = SecurityUtilities.base64Encode( bytes );

		String expected = "b3JpZ2luYWwgU3RyaW5nIGJlZm9yZSBiYXNlNjQgZW5jb2RpbmcgaW4gSmF2YQ==";

		// ^^^ http://javarevisited.blogspot.com.au/2012/02/how-to-encode-decode-string-in-java.html

		assertEquals( expected, actual );

		
	}

	
}
