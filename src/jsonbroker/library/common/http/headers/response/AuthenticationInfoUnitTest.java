// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.http.headers.response;

import jsonbroker.library.common.log.Log;
import junit.framework.TestCase;

public class AuthenticationInfoUnitTest  extends TestCase {
	
	
	private static final Log log = Log.getLog(AuthenticationInfoUnitTest.class);
	
     public void test1()
     {
         log.enteredMethod();
     }

     
      public void testBuildFromString()
      {
          String input = "cnonce=\"0a4f113b\", nc=000000FF, nextnonce=\"dcd98b7102dd2f0e8b11d0f600bfb0c093\", qop=auth-int, rspauth=\"6629fae49393a05397450978507c4ef1\"";

          AuthenticationInfo authenticationInfoHeader = AuthenticationInfo.buildFromString(input);

          assertEquals("0a4f113b", authenticationInfoHeader.getCnonce());
          assertEquals(255, authenticationInfoHeader.getNc());
          assertEquals("dcd98b7102dd2f0e8b11d0f600bfb0c093", authenticationInfoHeader.getNextnonce());
          assertEquals("auth-int", authenticationInfoHeader.getQop());
          assertEquals("6629fae49393a05397450978507c4ef1", authenticationInfoHeader.getRspauth());
          log.debug(authenticationInfoHeader.toString(), "authenticationInfoHeader.toString()");

      }
	
      public void testBuildFromStringWithUnkownEntries()
      {
          String input = "nextnonce=\"dcd98b7102dd2f0e8b11d0f600bfb0c093\", bad-field=\"bad\"";

          try
          {
              AuthenticationInfo.buildFromString(input);
              // good
          }
          catch (Exception e)
          {
        	  fail();
          }

      }

}
