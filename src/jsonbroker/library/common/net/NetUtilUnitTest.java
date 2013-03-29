// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.net;

import junit.framework.TestCase;

public class NetUtilUnitTest extends TestCase {
	
	public void test1() {
		
		NetUtil netUtil = new NetUtil();
		
		// !$&'()*+,-./:;=?@_~
	
		assertEquals( "x%20x", netUtil.escapeString( "x x") );
		assertEquals( "x/x", netUtil.escapeString( "x/x") );		
		assertEquals( "~", netUtil.escapeString( "~") );
		assertEquals( "x+x", netUtil.escapeString( "x+x") );
	}

}
