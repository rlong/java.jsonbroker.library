// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json.input;

import java.io.InputStream;

import jsonbroker.library.common.auxiliary.Data;
import jsonbroker.library.common.auxiliary.StringHelper;
import jsonbroker.library.common.json.DefaultJsonHandler;
import jsonbroker.library.common.log.Log;
import junit.framework.TestCase;

public class JsonReaderUnitTest extends TestCase{

	private static final Log log = Log.getLog(JsonReaderUnitTest.class );
	
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
		JsonReader.read(input, new DefaultJsonHandler());
	}

	public void testEmptyArray() {
		JsonInput input = toJsonInput("[]");
		JsonReader.read(input, new DefaultJsonHandler());
	}
	
	public void testSimpleObject() {
		JsonInput input = toJsonInput("{\"nullKey\":null,\"booleanKey\":true,\"integerKey\":314,\"stringKey\":\"value\"}");
		JsonReader.read(input, new DefaultJsonHandler());
	}
	
	public void testSimpleArray() {
		JsonInput input = toJsonInput("[null,true,314,\"value\"]");
		JsonReader.read(input, new DefaultJsonHandler());
		
	}
	
	public void testStatusStopped() {
		
		InputStream statusStopped = JsonReaderUnitTest.class.getResourceAsStream( "/jsonbroker/library/common/json/status.stopped.osx.vlc-2-0-4.json" );
		assertNotNull( statusStopped );
		JsonInput input = new JsonStreamInput( statusStopped );
		JsonReader.read(input, new DefaultJsonHandler());
		
	}
	
	public void testStatusPaused() {
		
		InputStream statusStopped = JsonReaderUnitTest.class.getResourceAsStream( "/jsonbroker/library/common/json/status.paused.osx.vlc-2-0-4.json" );
		assertNotNull( statusStopped );
		JsonInput input = new JsonStreamInput( statusStopped );
		JsonReader.read(input, new DefaultJsonHandler());

	}
	
	public void testStatusPlaying() {
		
		InputStream statusStopped = JsonReaderUnitTest.class.getResourceAsStream( "/jsonbroker/library/common/json/status.playing.osx.vlc-2-0-4.json" );
		assertNotNull( statusStopped );
		JsonInput input = new JsonStreamInput( statusStopped );
		JsonReader.read(input, new DefaultJsonHandler());
		
	}



}
