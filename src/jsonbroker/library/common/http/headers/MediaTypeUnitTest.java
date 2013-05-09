package jsonbroker.library.common.http.headers;

import jsonbroker.library.common.log.Log;
import junit.framework.TestCase;

public class MediaTypeUnitTest extends TestCase {
	

	private static final Log log = Log.getLog(MediaTypeUnitTest.class);
	
	
    public void test1()
    {
        log.enteredMethod();
    }

    public void testMultiPartFormData()
    {
        MediaType mediaType = MediaType.buildFromString("multipart/form-data; boundary=---------------------------114782935826962");
        assertEquals( "multipart", mediaType.getType());
        assertEquals( "form-data", mediaType.getSubtype() );
        assertEquals( "---------------------------114782935826962", mediaType.getParamaterValue( "boundary", null));
    }

    public void testTextPlain()
    {
        MediaType mediaType = MediaType.buildFromString("text/plain");
        assertEquals("text", mediaType.getType());
        assertEquals("plain", mediaType.getSubtype() );
    }
    
    public void testApplicationXZipCompresssed()
    {
        MediaType mediaType = MediaType.buildFromString("application/x-zip-compressed");
        assertEquals("application", mediaType.getType());
        assertEquals("x-zip-compressed", mediaType.getSubtype() );
    }

}
