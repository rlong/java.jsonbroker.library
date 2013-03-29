// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json.handlers;

import jsonbroker.library.common.json.input.JsonInput;
import jsonbroker.library.common.json.output.JsonOutput;

public class JsonBooleanHandler extends JsonHandler {

	
	private static final JsonBooleanHandler _instance = new JsonBooleanHandler();
	
	public static final JsonBooleanHandler getInstance() {
		return _instance;
	}
	
	
	public static Boolean readBoolean( JsonInput input) {
		
		byte currentByte = input.currentByte();
		
		if( 't' == currentByte || 'T' == currentByte) {

			//'t' is the current character
			input.nextByte(); // 'r' is the current character
			input.nextByte(); // 'u' is the current character
			input.nextByte(); // 'e' is the current character
			input.nextByte(); // after true
			
			return Boolean.TRUE;
		} else {
			
			//'f' is the current character
			input.nextByte(); // 'a' is the current character
			input.nextByte(); // 'l' is the current character
			input.nextByte(); // 's' is the current character
			input.nextByte(); // 'e' is the current character
			input.nextByte(); // after false

			return Boolean.FALSE;
		}		
		
	}
	
	@Override
	public Object readValue(JsonInput input) {

		return JsonBooleanHandler.readBoolean(input);
	}
	
	public static void writeBoolean( Boolean booleanValue, JsonOutput output) {

		if( booleanValue.booleanValue() ) {
			output.append("true");
		} else {
			output.append("false");
		}		

	}

	@Override
	public void writeValue(Object value, JsonOutput output) {
		
		JsonBooleanHandler.writeBoolean( (Boolean)value, output);
		
	}
	
	

}
