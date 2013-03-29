// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.http.headers.request;

import jsonbroker.library.common.log.Log;
import junit.framework.TestCase;

public class AuthorizationUnitTest  extends TestCase {
	
	
	private static final Log log = Log.getLog(AuthorizationUnitTest.class);
	
    //[Test]
     public void test1()
     {
         log.enteredMethod();
     }

     public void testBuildFromString()
     {
         String input = "Digest username=\"Mufasa\", realm=\"testrealm@host.com\", nonce=\"dcd98b7102dd2f0e8b11d0f600bfb0c093\", uri=\"/dir/index.html\", qop=auth, nc=00000001, cnonce=\"0a4f113b\", response=\"6629fae49393a05397450978507c4ef1\", opaque=\"5ccc069c403ebaf9f0171e9517f40e41\"";

         Authorization authorizationRequestHeader = Authorization.buildFromString(input);

         assertEquals("Mufasa", authorizationRequestHeader.getUsername());
         assertEquals("testrealm@host.com", authorizationRequestHeader.getRealm());
         assertEquals("dcd98b7102dd2f0e8b11d0f600bfb0c093", authorizationRequestHeader.getNonce());
         assertEquals("/dir/index.html", authorizationRequestHeader.getUri());
         assertEquals("auth", authorizationRequestHeader.getQop());
         assertEquals(1, authorizationRequestHeader.getNc());
         assertEquals("0a4f113b", authorizationRequestHeader.getCnonce());
         assertEquals("6629fae49393a05397450978507c4ef1", authorizationRequestHeader.getResponse());

         assertEquals("5ccc069c403ebaf9f0171e9517f40e41", authorizationRequestHeader.getOpaque());
         log.debug(authorizationRequestHeader.toString(), "authorizationRequestHeader.toString()");

     }
     
     public void testBuildFromStringWithUnkownEntries()
     {
         String input = "Digest realm=\"testrealm@host.com\", nonce=\"dcd98b7102dd2f0e8b11d0f600bfb0c093\", bad-field=\"bad\", opaque=\"5ccc069c403ebaf9f0171e9517f40e41\"";

         try
         {
             Authorization.buildFromString(input);
             // good 
         }
         catch (Exception e)
         {
             fail();
         }

     }


}
