// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.http.headers;

import jsonbroker.library.common.log.Log;
import junit.framework.TestCase;

public class AuthenticationHeaderScannerUnitTest extends TestCase {
	
	
	private static final Log log = Log.getLog(AuthenticationHeaderScannerUnitTest.class);
	
    public void test1()
    {
        log.enteredMethod();
    }

    public void testScanString()
    {
        String headerValue = "text";
        AuthenticationHeaderScanner scanner = new AuthenticationHeaderScanner(headerValue);
        scanner.scanString("text");
        
        assertTrue(scanner.isAtEnd());
        

        {
            headerValue = "text";
            scanner = new AuthenticationHeaderScanner(headerValue);
            scanner.scanString("text2");
            
            assertFalse(scanner.isAtEnd());
            
            scanner.scanString("text");
            assertTrue(scanner.isAtEnd());
        }

        {
            headerValue = "AB";
            scanner = new AuthenticationHeaderScanner(headerValue);
            scanner.scanString("A");
            scanner.scanString("B");
            assertTrue(scanner.isAtEnd());
        }

    }
    
    
     public void testScanUpToString()
     {
         log.enteredMethod();

         String headerValue = "A=B";
         AuthenticationHeaderScanner scanner = new AuthenticationHeaderScanner(headerValue);
         String a = scanner.scanUpToString("=");
         
         assertEquals("A", a);
         
         scanner.scanString("=B");
         
         assertTrue(scanner.isAtEnd());

     }


     public void testAuthenticateHeader()
     {
         String headerValue = "Digest realm=\"testrealm@host.com\", qop=\"auth,auth-int\", nonce=\"dcd98b7102dd2f0e8b11d0f600bfb0c093\", opaque=\"5ccc069c403ebaf9f0171e9517f40e41\"";

         AuthenticationHeaderScanner scanner = new AuthenticationHeaderScanner(headerValue);

         ////////////////////////////////////////////////////////////////////////////

         scanner.scanPastDigestString();

         ////////////////////////////////////////////////////////////////////////////

         String name = scanner.scanName();

         assertEquals("realm", name);


         String value = scanner.scanQuotedValue();

         assertEquals("testrealm@host.com", value);

         ////////////////////////////////////////////////////////////////////////////

         name = scanner.scanName();
         assertEquals("qop", name);


         value = scanner.scanQuotedValue();
         assertEquals("auth,auth-int", value);

         ////////////////////////////////////////////////////////////////////////////

         name = scanner.scanName();
         assertEquals("nonce", name);

         value = scanner.scanQuotedValue();
         assertEquals("dcd98b7102dd2f0e8b11d0f600bfb0c093", value);

         ////////////////////////////////////////////////////////////////////////////


         name = scanner.scanName();
         assertEquals("opaque", name);

         value = scanner.scanQuotedValue();
         assertEquals("5ccc069c403ebaf9f0171e9517f40e41", value);

         ////////////////////////////////////////////////////////////////////////////

         name = scanner.scanName();
         assertEquals(null, name);

     }
     
     public void testAuthorizationHeader()
     {
         String headerValue = "Digest username=\"Mufasa\", realm=\"testrealm@host.com\", nonce=\"dcd98b7102dd2f0e8b11d0f600bfb0c093\", uri=\"/dir/index.html\", qop=auth, nc=00000001, cnonce=\"0a4f113b\", response=\"6629fae49393a05397450978507c4ef1\", opaque=\"5ccc069c403ebaf9f0171e9517f40e41\"";

         AuthenticationHeaderScanner scanner = new AuthenticationHeaderScanner(headerValue);

         ////////////////////////////////////////////////////////////////////////////

         scanner.scanPastDigestString();

         ////////////////////////////////////////////////////////////////////////////

         String name = scanner.scanName();
         assertEquals("username", name);

         String value = scanner.scanQuotedValue();
         assertEquals("Mufasa", value);

         ////////////////////////////////////////////////////////////////////////////

         name = scanner.scanName();
         assertEquals("realm", name);

         value = scanner.scanQuotedValue();
         assertEquals("testrealm@host.com", value);

         ////////////////////////////////////////////////////////////////////////////

         name = scanner.scanName();
         assertEquals("nonce", name);

         value = scanner.scanQuotedValue();
         assertEquals("dcd98b7102dd2f0e8b11d0f600bfb0c093", value);

         ////////////////////////////////////////////////////////////////////////////

         name = scanner.scanName();
         assertEquals("uri", name);

         value = scanner.scanQuotedValue();
         assertEquals("/dir/index.html", value);

         ////////////////////////////////////////////////////////////////////////////

         name = scanner.scanName();
         assertEquals("qop", name);

         value = scanner.scanValue();
         assertEquals("auth", value);

         ////////////////////////////////////////////////////////////////////////////

         name = scanner.scanName();
         assertEquals("nc", name);

         long nc = scanner.scanHexUInt32();
         assertEquals(1, nc);

         ////////////////////////////////////////////////////////////////////////////

         name = scanner.scanName();
         assertEquals("cnonce", name);

         value = scanner.scanQuotedValue();
         assertEquals("0a4f113b", value);


         ////////////////////////////////////////////////////////////////////////////

         name = scanner.scanName();
         assertEquals("response", name);

         value = scanner.scanQuotedValue();
         assertEquals("6629fae49393a05397450978507c4ef1", value);

         ////////////////////////////////////////////////////////////////////////////

         name = scanner.scanName();
         assertEquals("opaque", name);

         value = scanner.scanQuotedValue();
         assertEquals("5ccc069c403ebaf9f0171e9517f40e41", value);

         ////////////////////////////////////////////////////////////////////////////

         name = scanner.scanName();
         assertEquals(null, name);


     }

     public void testHexIntegers()
     {
         String headerValue = "value00000000=00000000, value0000000a=0000000a, valueFFFFFFFF=FFFFFFFF, valueFFFFFFFFFFFF=FFFFFFFFFFFF";

         AuthenticationHeaderScanner scanner = new AuthenticationHeaderScanner(headerValue);

         ////////////////////////////////////////////////////////////////////////////

         String name = scanner.scanName();
         assertEquals("value00000000", name);

         long value = scanner.scanHexUInt32();
         assertEquals(0, value);

         ////////////////////////////////////////////////////////////////////////////

         name = scanner.scanName();
         assertEquals("value0000000a", name);

         value = scanner.scanHexUInt32();
         assertEquals(10, value);


         ////////////////////////////////////////////////////////////////////////////

         name = scanner.scanName();
         assertEquals("valueFFFFFFFF", name);

         value = scanner.scanHexUInt32();
         assertEquals(0xFFFFFFFFl, value);


         ////////////////////////////////////////////////////////////////////////////

         name = scanner.scanName();
         assertEquals("valueFFFFFFFFFFFF", name);

         value = scanner.scanHexUInt32();
         assertEquals(0, value);

     }
     
     public void testHeaderWithNonQuotedValueAtEnd()
     {
         String headerValue = "Digest realm=\"testrealm@host.com\", qop=auth-int"; // not a valid RFC-2617, but thats not relevant here

         AuthenticationHeaderScanner scanner = new AuthenticationHeaderScanner(headerValue);

         ////////////////////////////////////////////////////////////////////////////

         scanner.scanPastDigestString();

         ////////////////////////////////////////////////////////////////////////////


         String name = scanner.scanName();
         assertEquals("realm", name);

         String value = scanner.scanQuotedValue();
         assertEquals("testrealm@host.com", value);

         ////////////////////////////////////////////////////////////////////////////

         name = scanner.scanName();
         assertEquals("qop", name);

         value = scanner.scanQuotedValue();
         assertEquals("auth-int", value);

     }

     public void testHeaderWithHexIntegerAtEnd()
     {
         String headerValue = "Digest realm=\"testrealm@host.com\", nc=0000000a"; // not a valid RFC-2617, but thats not relevant here

         AuthenticationHeaderScanner scanner = new AuthenticationHeaderScanner(headerValue);

         ////////////////////////////////////////////////////////////////////////////

         scanner.scanPastDigestString();

         ////////////////////////////////////////////////////////////////////////////

         String name = scanner.scanName();
         assertEquals("realm", name);

         String value = scanner.scanQuotedValue();
         assertEquals("testrealm@host.com", value);


         ////////////////////////////////////////////////////////////////////////////

         name = scanner.scanName();
         assertEquals("nc", name);

         long nc = scanner.scanHexUInt32();
         assertEquals(10, nc);

         ////////////////////////////////////////////////////////////////////////////


     }

     public void testRealm1()
     {
         String headerValue = "Digest realm=\"users@al's computer\", nonce=\"dcd98b7102dd2f0e8b11d0f600bfb0c093\"";

         AuthenticationHeaderScanner scanner = new AuthenticationHeaderScanner(headerValue);

         ////////////////////////////////////////////////////////////////////////////

         scanner.scanPastDigestString();

         ////////////////////////////////////////////////////////////////////////////


         String name = scanner.scanName();
         assertEquals("realm", name);

         String value = scanner.scanQuotedValue();
         assertEquals("users@al's computer", value);

         ////////////////////////////////////////////////////////////////////////////

         name = scanner.scanName();
         assertEquals("nonce", name);

         value = scanner.scanQuotedValue();
         assertEquals("dcd98b7102dd2f0e8b11d0f600bfb0c093", value);

     }

}
