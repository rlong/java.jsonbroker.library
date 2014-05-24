// Copyright (c) 2014 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.http.multi_part;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import jsonbroker.library.common.auxiliary.DataHelper;
import jsonbroker.library.common.auxiliary.MutableData;
import jsonbroker.library.common.auxiliary.StringHelper;
import jsonbroker.library.common.http.Entity;
import jsonbroker.library.common.http.StreamEntity;
import jsonbroker.library.common.log.Log;
import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

public class MultiPartReaderUnitTest extends TestCase {

	private static final Log log = Log.getLog( MultiPartReaderUnitTest.class );

	@Test
    public void test1()
    {
        log.enteredMethod();
    }

	
    private static Entity buildEntity(String multipartString)
    {
        byte[] multiPartData = StringHelper.toUtfBytes(multipartString);

        ByteArrayInputStream multiPartStream = new ByteArrayInputStream( multiPartData );
         
        StreamEntity answer = new StreamEntity(multiPartStream, multiPartData.length);
        return answer;

    }
    
    
    private static Entity buildEntity(String[] multipartLines) {
    	
    	StringBuffer multipartString = new StringBuffer();
    	
    	for( String line : multipartLines ) {
    		
    		multipartString.append( line );
    		multipartString.append( "\r\n" );
    		
    	}
    	
    	return buildEntity(multipartString.toString());
    	
    }

    
    public void testDelimiterDetection() {
    	
    	
    	log.enteredMethod();
    	
        String[] multipartLines = {
                "",
                "-----------------------------114782935826962",
               "Content-Disposition: form-data; name=\"datafile\"; filename=\"test.txt\"",
               "Content-Type: text/plain",
               "",
               "0123",
               "4567",
               "89ab",
               "cdef",
               "-----------------------------114782935826962--",
           };

        Entity entity = buildEntity(multipartLines);
        MultiPartReader reader = new MultiPartReader("---------------------------114782935826962", entity);

        DelimiterIndicator indicator = reader.skipToNextDelimiterIndicator();
        Assert.assertNotNull(indicator);
        Assert.assertTrue( indicator instanceof DelimiterFound); // Assert.IsInstanceOf<DelimiterFound>(indicator);        
        DelimiterFound delimiterFound = (DelimiterFound)indicator;
        
        Assert.assertEquals(0, delimiterFound.getStartOfDelimiter());
        Assert.assertFalse(delimiterFound.isCloseDelimiter());
        
        
        // Content-Disposition
        MutableData stringBuffer = new MutableData();
        String contentDisposition = reader.readLine(stringBuffer);
        Assert.assertEquals(multipartLines[2], contentDisposition);


        // Content-Type
        String contentType = reader.readLine(stringBuffer);
        Assert.assertEquals(multipartLines[3], contentType);

        
        // empty line
        String emptyLine = reader.readLine(stringBuffer);
        Assert.assertEquals("", emptyLine);

        // ending indicator
        indicator = reader.skipToNextDelimiterIndicator();
        Assert.assertNotNull(indicator);
        Assert.assertTrue( indicator instanceof DelimiterFound); // Assert.IsInstanceOf<DelimiterFound>(indicator);
        delimiterFound = (DelimiterFound)indicator;
        Assert.assertTrue(delimiterFound.isCloseDelimiter());
    	
    }
    
    
    static class TestPartHandler implements PartHandler {
    	
    	private static final Log log = Log.getLog( TestPartHandler.class );

    	MutableData _data = new MutableData();
    	
    	public void handleHeader(String name, String value) {
    		
    	}
    	
    	public void handleBytes(byte[] bytes, int offset, int length) {
    		
    		_data.append(bytes, offset, length);
    	}
    	
    	
    	public void handleException(Exception e) {
    		log.error(e);
    	}
    	
    	public void partCompleted() {
    	}
    	
    }

    static class TestMultiPartHandler implements MultiPartHandler {
    	
    	
    	private static final Log log = Log.getLog( TestMultiPartHandler.class );

    	
    	ArrayList<TestPartHandler> _partHandlers = new ArrayList<TestPartHandler>();    	
    	boolean _foundCloseDelimiter = false;
    	
    	public PartHandler foundPartDelimiter() {
    		
    		TestPartHandler answer = new TestPartHandler();
    		_partHandlers.add(answer);
    		return answer;
    	}
    	
        public void handleException(Exception e) {
            log.error(e);
        }
        
        public void foundCloseDelimiter() {
            _foundCloseDelimiter = true;
        }
    	
    }
    
    public void testSingleMultiPartForm()
    {

        log.enteredMethod();

        String[] multipartLines = {
                                      "",
                                      "-----------------------------114782935826962",
                                     "Content-Disposition: form-data; name=\"datafile\"; filename=\"test.txt\"",
                                     "Content-Type: text/plain",
                                     "",
                                     "0123",
                                     "4567",
                                     "89ab",
                                     "cdef",
                                     "-----------------------------114782935826962--",
                                 };

        Entity entity = buildEntity(multipartLines);
        MultiPartReader reader = new MultiPartReader("---------------------------114782935826962", entity);
        
        
        TestMultiPartHandler testMultiPartHandler = new TestMultiPartHandler();
        reader.Process(testMultiPartHandler);
        Assert.assertTrue(testMultiPartHandler._foundCloseDelimiter);
        
        
        Assert.assertEquals(1, testMultiPartHandler._partHandlers.size());
        
        TestPartHandler firstTestPartHandler = testMultiPartHandler._partHandlers.get(0);
        Assert.assertEquals(16 + 6, firstTestPartHandler._data.getCount() );
        String expectedContent = "0123\r\n4567\r\n89ab\r\ncdef";
        String actualContent = DataHelper.toUtf8String( firstTestPartHandler._data );
        Assert.assertEquals( expectedContent, actualContent );

        
    }
    
    
    public void testDoubleMultiPartForm() {
    	
    	
        log.enteredMethod();

        String[] multipartLines = {
                                      "",
                                      "-----------------------------114782935826962",
                                     "Content-Disposition: form-data; name=\"datafile\"; filename=\"test1.txt\"",
                                     "Content-Type: text/plain",
                                     "",
                                     "0123",
                                     "4567",
                                     "89ab",
                                     "cdef",
                                     "-----------------------------114782935826962",
                                     "Content-Disposition: form-data; name=\"datafile\"; filename=\"test2.txt\"",
                                     "Content-Type: text/plain",
                                     "",
                                     "cdef",
                                     "89ab",
                                     "4567",
                                     "0123",
                                     "-----------------------------114782935826962--",
                                 };

        
        Entity entity = buildEntity(multipartLines);
        MultiPartReader reader = new MultiPartReader("---------------------------114782935826962", entity);


        TestMultiPartHandler testMultiPartHandler = new TestMultiPartHandler();
        reader.Process(testMultiPartHandler);
        Assert.assertTrue(testMultiPartHandler._foundCloseDelimiter);

        Assert.assertEquals(2, testMultiPartHandler._partHandlers.size());
        
        {
            TestPartHandler firstTestPartHandler = testMultiPartHandler._partHandlers.get(0);
            Assert.assertEquals(16 + 6, firstTestPartHandler._data.getCount() );
            String expectedContent = "0123\r\n4567\r\n89ab\r\ncdef";
            String actualContent = DataHelper.toUtf8String( firstTestPartHandler._data );
            Assert.assertEquals( expectedContent, actualContent );
        }
    	

        {
            TestPartHandler secondTestPartHandler = testMultiPartHandler._partHandlers.get(1);
            Assert.assertEquals(16 + 6, secondTestPartHandler._data.getCount() );
            String expectedContent = "cdef\r\n89ab\r\n4567\r\n0123";
            String actualContent = DataHelper.toUtf8String( secondTestPartHandler._data );
            Assert.assertEquals( expectedContent, actualContent );
        }

    }
    
    
    static String buildAttachment( String contentSource, int length)
    {

        StringBuilder answer = new StringBuilder();

        int remaining = length;
        while (remaining >= contentSource.length()) {
            answer.append(contentSource);
            remaining -= contentSource.length();
        }

        int modulus = length % contentSource.length();

        for (int j = 0; j < modulus; j++)
        {
            answer.append(contentSource.charAt(j));
        }

        return answer.toString();
    }

    
    static void testSingleAttachment(String boundary, String contentSource, int length) {
    	
    	
        if (0 == length) {
            log.debug(contentSource, "contentSource");
        }
        log.debug(length, "length");
        
        
        String attachment = buildAttachment(contentSource, length);
        Assert.assertEquals(length, attachment.length());
        
        
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\r\n--");
        stringBuilder.append(boundary);
        stringBuilder.append("\r\n");
        
        
        stringBuilder.append("Content-Disposition: form-data; name=\"datafile\"; filename=\"test1.txt\"\r\n");
        stringBuilder.append("Content-Type: text/plain\r\n");
        stringBuilder.append("\r\n");

        

        stringBuilder.append(attachment);

        stringBuilder.append("\r\n--");
        stringBuilder.append(boundary);
        stringBuilder.append("--\r\n");

        Entity entity = buildEntity(stringBuilder.toString());
        MultiPartReader reader = new MultiPartReader(boundary, entity);

        TestMultiPartHandler testMultiPartHandler = new TestMultiPartHandler();
        reader.Process(testMultiPartHandler);
        Assert.assertTrue(testMultiPartHandler._foundCloseDelimiter);
        
        
        Assert.assertEquals(1, testMultiPartHandler._partHandlers.size());
        
        TestPartHandler firstTestPartHandler = testMultiPartHandler._partHandlers.get(0);
        Assert.assertEquals(attachment.length(), firstTestPartHandler._data.getCount() );
        String actualContent = DataHelper.toUtf8String( firstTestPartHandler._data );
        Assert.assertEquals( attachment, actualContent );
    	
    }
    
    private void testSingleAttachments(String boundary, String contentSource)
    {

        for (int i = 0; i < 10; i++) {
            testSingleAttachment(boundary, contentSource, i);
        }

        int lowerBound = (MultiPartReader.BUFFER_SIZE / 2) - 10;
        int upperBound = (MultiPartReader.BUFFER_SIZE / 2) + 10;

        
        for (int i = lowerBound; i < upperBound; i++) {
            testSingleAttachment(boundary, contentSource, i);
        }
        
        lowerBound = MultiPartReader.BUFFER_SIZE - 200;
        upperBound = MultiPartReader.BUFFER_SIZE + 10;

        for (int i = lowerBound; i < upperBound; i++) {
            testSingleAttachment(boundary, contentSource, i);
        }

        lowerBound = (MultiPartReader.BUFFER_SIZE * 2) - 200;
        upperBound = (MultiPartReader.BUFFER_SIZE * 2) + 10;

        for (int i = lowerBound; i < upperBound; i++) {
            testSingleAttachment(boundary, contentSource, i);
        }


    }

    public void testSingleAttachments() {
    	
        log.enteredMethod();
        
        String boundary = "---------------------------114782935826962";

        testSingleAttachments(boundary, "------");
        testSingleAttachments(boundary, "------\r\n");
        testSingleAttachments(boundary, "-----\r\n-");
        testSingleAttachments(boundary, "----\r\n--");
        testSingleAttachments(boundary, "---\r\n---");
        testSingleAttachments(boundary, "--\r\n----");
        testSingleAttachments(boundary, "-\r\n-----");
        testSingleAttachments(boundary, "\r\n------");
        testSingleAttachments(boundary, "\r\n\r\n\r\n");
        
        
    }
    
    
    static void testDoubleAttachments(String boundary, String contentSource, int length) {
    	
        if (0 == length) {
            log.debug(contentSource, "contentSource");
        }
        log.debug(length, "length");
        
        String attachment = buildAttachment(contentSource, length);
        Assert.assertEquals(length, attachment.length());

        
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\r\n--");
        stringBuilder.append(boundary);
        stringBuilder.append("\r\n");


        stringBuilder.append("Content-Disposition: form-data; name=\"datafile\"; filename=\"test1.txt\"\r\n");
        stringBuilder.append("Content-Type: text/plain\r\n");
        stringBuilder.append("\r\n");
        stringBuilder.append(attachment);

        stringBuilder.append("\r\n--");
        stringBuilder.append(boundary);
        stringBuilder.append("\r\n");

        stringBuilder.append("Content-Disposition: form-data; name=\"datafile\"; filename=\"test2.txt\"\r\n");
        stringBuilder.append("Content-Type: text/plain\r\n");
        stringBuilder.append("\r\n");
        stringBuilder.append(attachment);

        stringBuilder.append("\r\n--");
        stringBuilder.append(boundary);
        stringBuilder.append("--\r\n");

        Entity entity = buildEntity(stringBuilder.toString());
        MultiPartReader reader = new MultiPartReader(boundary, entity);

        TestMultiPartHandler testMultiPartHandler = new TestMultiPartHandler();
        reader.Process(testMultiPartHandler);
        Assert.assertTrue(testMultiPartHandler._foundCloseDelimiter);
        
        
        Assert.assertEquals(2, testMultiPartHandler._partHandlers.size());

        {
            TestPartHandler firstTestPartHandler = testMultiPartHandler._partHandlers.get(0);
            Assert.assertEquals(attachment.length(), firstTestPartHandler._data.getCount() );
            String actualContent = DataHelper.toUtf8String( firstTestPartHandler._data );
            Assert.assertEquals( attachment, actualContent );
        }
        
        {
            TestPartHandler secondTestPartHandler = testMultiPartHandler._partHandlers.get(1);
            Assert.assertEquals(attachment.length(), secondTestPartHandler._data.getCount() );
            String actualContent = DataHelper.toUtf8String( secondTestPartHandler._data );
            Assert.assertEquals( attachment, actualContent );
        }

    }
    
    
    private void testDoubleAttachments(String boundary, String contentSource) {
    	
        for (int i = 0; i < 10; i++) {
            testDoubleAttachments(boundary, contentSource, i);
        }

        int lowerBound = (MultiPartReader.BUFFER_SIZE / 2) - 10;
        int upperBound = (MultiPartReader.BUFFER_SIZE / 2) + 10;

        for (int i = lowerBound; i < upperBound; i++) {
            testDoubleAttachments(boundary, contentSource, i);
        }


        lowerBound = MultiPartReader.BUFFER_SIZE - 200;
        upperBound = MultiPartReader.BUFFER_SIZE + 10;

        for (int i = lowerBound; i < upperBound; i++) {
            testDoubleAttachments(boundary, contentSource, i);
        }

        lowerBound = (MultiPartReader.BUFFER_SIZE * 2) - 200;
        upperBound = (MultiPartReader.BUFFER_SIZE * 2) + 10;

        for (int i = lowerBound; i < upperBound; i++) {
            testDoubleAttachments(boundary, contentSource, i);
        }

    }
    
    public void testDoubleAttachments()
    {
        log.enteredMethod();

        String boundary = "---------------------------114782935826962";

        testDoubleAttachments(boundary, "------");
        testDoubleAttachments(boundary, "------\r\n");
        testDoubleAttachments(boundary, "-----\r\n-");
        testDoubleAttachments(boundary, "----\r\n--");
        testDoubleAttachments(boundary, "---\r\n---");
        testDoubleAttachments(boundary, "--\r\n----");
        testDoubleAttachments(boundary, "-\r\n-----");
        testDoubleAttachments(boundary, "\r\n------");
        testDoubleAttachments(boundary, "\r\n\r\n\r\n");
    }


}
