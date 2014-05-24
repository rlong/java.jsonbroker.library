// Copyright (c) 2014 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.http.multi_part;

import jsonbroker.library.common.log.Log;
import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

public class ContentDispositionUnitTest extends TestCase {

	private static final Log log = Log.getLog( ContentDispositionUnitTest.class );

	@Test
    public void test1()
    {
        log.enteredMethod();
    }

    public void testMultiPartFormData()
    {
        ContentDisposition contentDisposition = ContentDisposition.buildFromString("form-data; name=\"datafile\"; filename=\"test.txt\"");
        
        Assert.assertEquals("form-data", contentDisposition.getDispExtensionToken());
        Assert.assertEquals("datafile", contentDisposition.getDispositionParameter("name", null));
        Assert.assertEquals("test.txt", contentDisposition.getDispositionParameter("filename", null));
    }

}
