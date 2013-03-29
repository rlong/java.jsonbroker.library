// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json.handlers;

import jsonbroker.library.common.json.input.JsonInput;
import jsonbroker.library.common.json.output.JsonOutput;

public class JsonNullHandler extends JsonHandler {

	private static final JsonNullHandler _instance = new JsonNullHandler();
	
	public static final JsonNullHandler getInstance() {
		return _instance;
	}

	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	
	private JsonNullHandler() {
	}

	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	
	public static void writeNull( JsonOutput output) {
		output.append( "null");
	}
	
	@Override
	public void writeValue(Object value, JsonOutput output) {
		JsonNullHandler.writeNull( output );
	}
	
	public static Object readNull( JsonInput input) {
		
		//'n' is the current character
		input.nextByte(); // 'u' is the current character
		input.nextByte(); // 'l' is the current character
		input.nextByte(); // 'l' is the current character
		input.nextByte(); // after null
		
		return null;
	}

	@Override
	public Object readValue(JsonInput input) {
		
		return JsonNullHandler.readNull( input );
		
	}


}
