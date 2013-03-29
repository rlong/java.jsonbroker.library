// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json.handlers;

import jsonbroker.library.common.json.JsonArray;
import jsonbroker.library.common.json.input.JsonInput;
import jsonbroker.library.common.json.output.JsonOutput;
import jsonbroker.library.common.log.Log;




public final class JsonArrayHandler extends JsonHandler {

	private static final Log log = Log.getLog(JsonArrayHandler.class );
	
	private static final JsonArrayHandler _instance = new JsonArrayHandler();
	
	public static final JsonArrayHandler getInstance() {
		return _instance;
	}

	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	
	private JsonArrayHandler() {
	}

	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////

	
	public static void writeJsonArray( JsonArray array, JsonOutput output) {
		
		output.append( '[');

		int count = array.size();
		if( count > 0 ) {
			
			Object object = array.getObject( 0 );
			
			JsonHandler valueHandler =  JsonHandler.getHandler( object );
			
			valueHandler.writeValue( object , output);
			
		}
		for( int i = 1; i < count; i++ ) {
			
			output.append( ',');
			
			Object object = array.getObject( i );
			
			JsonHandler valueHandler =  JsonHandler.getHandler( object );
			
			valueHandler.writeValue( object , output);
			
		}
		
		output.append( ']');

	}
	
	@Override
	public final void writeValue(Object value, JsonOutput output) {
		
		JsonArrayHandler.writeJsonArray( (JsonArray)value, output);
		
	}

	public static JsonArray readJsonArray( JsonInput input) {
		
		JsonArray answer = new JsonArray();
		
		input.nextByte(); // move past the '['
		
		byte b = input.scanToNextToken();
		while( ']' != b ) {
			
			JsonHandler valueHandler = JsonHandler.getHandler( b );
			Object object = valueHandler.readValue( input);
			
			//log.debug( object.toString(), "object.toString()");
			
			answer.add( object );
						
			b = input.scanToNextToken();			
		}
		
		// move past the ']' if we can
		if( input.hasNextByte() ) {
			input.nextByte();
		}
		
		
		return answer;
	}

	
	@Override
	public final Object readValue( JsonInput reader) {
		
		return JsonArrayHandler.readJsonArray( reader); 
	}


}
