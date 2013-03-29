// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.http.headers.response;

import jsonbroker.library.common.log.Log;
import junit.framework.TestCase;

public class WwwAuthenticateUnitTest extends TestCase {
	
	
	private static final Log log = Log.getLog(WwwAuthenticateUnitTest.class);
	
	
     public void test1()
     {
         log.enteredMethod();
     }

     public void testBuildFromString()
     {
         String input = "Digest realm=\"testrealm@host.com\", qop=\"auth,auth-int\", nonce=\"dcd98b7102dd2f0e8b11d0f600bfb0c093\", opaque=\"5ccc069c403ebaf9f0171e9517f40e41\"";

         WwwAuthenticate authenticateResponseHeader = WwwAuthenticate.buildFromString(input);
         assertEquals( "dcd98b7102dd2f0e8b11d0f600bfb0c093", authenticateResponseHeader.getNonce());
         assertEquals("5ccc069c403ebaf9f0171e9517f40e41", authenticateResponseHeader.getOpaque());
         assertEquals("auth,auth-int", authenticateResponseHeader.getQop());
         assertEquals("testrealm@host.com", authenticateResponseHeader.getRealm());

     }


     public void testBuildFromStringWithUnkownEntries()
     {
         String input = "Digest realm=\"testrealm@host.com\", nonce=\"dcd98b7102dd2f0e8b11d0f600bfb0c093\", bad-field=\"bad\", opaque=\"5ccc069c403ebaf9f0171e9517f40e41\"";

         try
         {
             WwwAuthenticate authenticateResponseHeader = WwwAuthenticate.buildFromString(input);
             // good
         }
         catch (Exception e)
         {
        	 fail();
         }

     }

	
}
