package jsonbroker.library.common.jsonml;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.HandlerBase;

import jsonbroker.library.common.json.JsonArray;
import jsonbroker.library.common.json.JsonBuilder;
import jsonbroker.library.common.json.JsonBuilderUnitTest;
import jsonbroker.library.common.log.Log;
import junit.framework.TestCase;

public class SaxHandlerIntegrationTest extends TestCase {
	
	
	private static final Log log = Log.getLog(SaxHandlerIntegrationTest.class );
	
	public void test1() { 
		log.enteredMethod();
	}


	public void testSimple() throws Exception {
		
		
		InputStream xmlStream = this.getClass().getResourceAsStream( "/jsonbroker/library/common/jsonml/SaxHandlerIntegrationTest.testSimple.xml" );
		assertNotNull( xmlStream );

		
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		
		JsonBuilder jsonBuilder = new JsonBuilder();
		
		SaxHandler handler = new SaxHandler(jsonBuilder);
		
		saxParser.parse( xmlStream, handler );
		
		JsonArray arrayDocument = jsonBuilder.getArrayDocument();
		assertNotNull( arrayDocument );
		
		log.debug( arrayDocument.toString(), "arrayDocument.toString()" );
		
		String rootElementName = arrayDocument.getString(0);
		assertEquals( "company", rootElementName );
		
	}

	public void testRss() throws Exception {
		
		
		InputStream xmlStream = this.getClass().getResourceAsStream( "/jsonbroker/library/common/jsonml/SaxHandlerIntegrationTest.testRss.xml" );
		assertNotNull( xmlStream );

		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		
		JsonBuilder jsonBuilder = new JsonBuilder();
		
		SaxHandler handler = new SaxHandler(jsonBuilder);
		
		saxParser.parse( xmlStream, handler );
		
		JsonArray arrayDocument = jsonBuilder.getArrayDocument();
		assertNotNull( arrayDocument );
//		log.debug( arrayDocument.toString(), "arrayDocument.toString()" );
		
	}

	
}
