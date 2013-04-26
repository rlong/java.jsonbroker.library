// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json.handlers;

import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import jsonbroker.library.common.json.JsonObject;
import jsonbroker.library.common.json.input.JsonInput;
import jsonbroker.library.common.json.output.JsonOutput;
import jsonbroker.library.common.log.Log;




public final class JsonObjectHandler extends JsonHandler {

	private static final Log log = Log.getLog(JsonObjectHandler.class );
	
	private static final JsonObjectHandler _instance = new JsonObjectHandler();
	
	public static final JsonObjectHandler getInstance() {
		return _instance;
	}

	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	
	private JsonObjectHandler() {
	}

	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////

	

	
	
	public static JsonObject readJsonObject( JsonInput input) {
		
		JsonObject answer = new JsonObject();
		
		input.nextByte(); // move past the '{'
		
		byte b = input.scanToNextToken();
		while( '}' != b ) {
			
			String key = JsonStringHandler.readString( input );
			
			b = input.scanToNextToken();
			
			JsonHandler valueHandler =  JsonHandler.getHandler( b );
			Object value = valueHandler.readValue( input);
			
			answer.put( key , value);
			
			b = input.scanToNextToken();			
			
		}
		
		// move past the '}' if we can
		if( input.hasNextByte() ) {
			input.nextByte();
		}

		return answer;

	}

	
	@Override
	public final Object readValue(JsonInput input) {
		
		return JsonObjectHandler.readJsonObject( input );
		
	}
	
	
	public static void writeJsonObject( JsonObject value, JsonOutput output) {
		
		Set<Entry<String, Object>> entrySet = value.entrySet();
		
		output.append( '{');
		
		for( Iterator<Entry<String, Object>> i = entrySet.iterator(); i.hasNext(); ) {
			
			Entry<String, Object> e = i.next();
			
			
			JsonStringHandler.writeString(e.getKey(), output);
			
			output.append( ':');
			
			JsonHandler valueHander = JsonHandler.getHandler( e.getValue());
			
			valueHander.writeValue( e.getValue(), output);
			
			if( i.hasNext() ) { 
				output.append( ',');
			}
		}
		
		output.append( '}');

	}
	
	@Override
	public final void writeValue(Object value, JsonOutput output) {
		
		
		JsonObject jsonObject = (JsonObject)value;
		JsonObjectHandler.writeJsonObject( jsonObject, output);
		
	}
	


}
