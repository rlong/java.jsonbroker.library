// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json;

import java.io.InputStream;

import jsonbroker.library.common.auxiliary.Data;
import jsonbroker.library.common.auxiliary.StringHelper;
import jsonbroker.library.common.json.input.JsonDataInput;
import jsonbroker.library.common.json.input.JsonInput;
import jsonbroker.library.common.json.input.JsonReader;
import jsonbroker.library.common.json.input.JsonStreamInput;
import jsonbroker.library.common.log.Log;
import junit.framework.TestCase;

public class JsonBuilderUnitTest extends TestCase {
	
	private static final Log log = Log.getLog(JsonBuilderUnitTest.class );

	
	public void test1() { 
		log.enteredMethod();
	}
	
	
	private static JsonInput toJsonInput( String input ) {

		byte[] rawData = StringHelper.toUtfBytes(input);
		Data data = new Data(rawData);
		JsonInput answer = new JsonDataInput(data);
		return answer;

	}
	
	public void testEmptyObject() {
		JsonInput input = toJsonInput("{}");
		JsonBuilder builder = new JsonBuilder();
		JsonReader.read(input, builder);
		assertNotNull( builder.getObjectDocument() );
	}


	public void testEmptyArray() {
		JsonInput input = toJsonInput("[]");
		JsonBuilder builder = new JsonBuilder();
		JsonReader.read(input, builder);
		assertNotNull( builder.getArrayDocument() );
	}
	
	
	public void testSimpleObject() {
		JsonInput input = toJsonInput("{\"nullKey\":null,\"booleanKey\":true,\"integerKey\":314,\"stringKey\":\"value\"}");
		JsonBuilder builder = new JsonBuilder();
		JsonReader.read(input, builder);
		
		JsonObject jsonDocument = builder.getObjectDocument();		
		assertNotNull( jsonDocument );
		assertNull( jsonDocument.getObject( "nullKey", null));
		assertTrue( jsonDocument.getBoolean( "booleanKey" ));
		assertEquals( 314, jsonDocument.getInt( "integerKey"));
		assertEquals( "value", jsonDocument.getString( "stringKey"));
	}
	
	
	public void testStatusStopped() {
		
		InputStream statusStopped = JsonBuilderUnitTest.class.getResourceAsStream( "/jsonbroker/library/common/json/status.stopped.osx.vlc-2-0-4.json" );
		assertNotNull( statusStopped );
		JsonInput input = new JsonStreamInput( statusStopped );
		JsonBuilder builder = new JsonBuilder();
		JsonReader.read(input, builder);
		

		
	}
	
	public void testStatusPaused() {
		
		InputStream statusStopped = JsonBuilderUnitTest.class.getResourceAsStream( "/jsonbroker/library/common/json/status.paused.osx.vlc-2-0-4.json" );
		assertNotNull( statusStopped );
		JsonInput input = new JsonStreamInput( statusStopped );
		JsonBuilder builder = new JsonBuilder();
		JsonReader.read(input, builder);

		
	}
	
	public void testStatusPlaying() {
		
		InputStream statusStopped = JsonBuilderUnitTest.class.getResourceAsStream( "/jsonbroker/library/common/json/status.playing.osx.vlc-2-0-4.json" );
		assertNotNull( statusStopped );
		JsonInput input = new JsonStreamInput( statusStopped );
		JsonBuilder builder = new JsonBuilder();
		JsonReader.read(input, builder);
		
		JsonObject jsonDocument = builder.getObjectDocument();
		log.debug( jsonDocument.toString(),"jsonDocument.toString()"); 

		
	}


}
