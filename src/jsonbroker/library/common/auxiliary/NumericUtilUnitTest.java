// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.auxiliary;



import jsonbroker.library.common.exception.BaseException;
import junit.framework.TestCase;

public class NumericUtilUnitTest extends TestCase {

	
	public void testParseIntegerGood() {
		
		int actual = NumericUtilities.parseInteger( "12");
		assertEquals( 12, actual);
	}
	
	
	
	public void testParseIntegerBadData() {
		
		try {
			NumericUtilities.parseInteger( "blah blah");
			fail("exception should have been thrown");
		} catch( BaseException e ) {
			// good 
		}
		
	}
	
	public void testParseIntegerEmptyData() {
		try {
			NumericUtilities.parseInteger( "");
			fail("exception should have been thrown");
		} catch( BaseException e ) {
			// good 
		}

	}
}
