// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json.handlers;

import jsonbroker.library.common.auxiliary.NullObject;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.json.JsonArray;
import jsonbroker.library.common.json.JsonObject;
import jsonbroker.library.common.json.input.JsonInput;
import jsonbroker.library.common.json.output.JsonOutput;



public abstract class JsonHandler {
	
	
	private static JsonStringHandler JSON_STRING_HANDLER = new JsonStringHandler(); 


	
	// based on object type
	public static JsonHandler getHandler( Object value ) {
		
		
		if( null == value ) {
			return JsonNullHandler.getInstance();
		}

		////////////////////////////////////////////////////////////////////////
		
		
		if( value instanceof Boolean )  {
			return JsonBooleanHandler.getInstance();
		}
		
		if( value instanceof JsonArray ) {
			return JsonArrayHandler.getInstance();
		}
		
		if( NullObject.INSTANCE == value ) {
			return JsonNullHandler.getInstance();
		}
		
		
		if( value instanceof Number ) {
			return JsonNumberHandler.getInstance();
		}
		
		
		if( value instanceof JsonObject ) {
			return JsonObjectHandler.getInstance();
		}
		
		if( value instanceof String ) {
			return JSON_STRING_HANDLER;
		}
		
		if( value instanceof NullObject ) {
			return JsonNullHandler.getInstance();
		}

		////////////////////////////////////////////////////////////////////////

        Class<?> clazz = value.getClass();
        String technicalError = String.format( "unsupported type; clazz.getName() = %s", clazz.getName());        
        throw new BaseException(JsonHandler.class, technicalError);
		
	}

	// based on object type
	public static JsonHandler getHandler( byte tokenBeginning ) {
		
		if( '"' == tokenBeginning ) {
			return JSON_STRING_HANDLER;
		}
		
		if( '0' <= tokenBeginning && tokenBeginning <= '9' ) {
			return JsonNumberHandler.getInstance();
		}
		
		if( '[' == tokenBeginning ) {
			return JsonArrayHandler.getInstance();
		}
		
		if( '{' == tokenBeginning ) {
			return JsonObjectHandler.getInstance();
		}
		
		if( '+' == tokenBeginning ) {
			return JsonNumberHandler.getInstance();
		}
		if( '-' == tokenBeginning ) {
			return JsonNumberHandler.getInstance();
		}
		
		if( 't' == tokenBeginning || 'T' == tokenBeginning) {
			return JsonBooleanHandler.getInstance();
		}
		
		if( 'f' == tokenBeginning || 'F' == tokenBeginning) {
			return JsonBooleanHandler.getInstance();
		}
		
		if( 'n' == tokenBeginning || 'N' == tokenBeginning) {
			return JsonNullHandler.getInstance();
		}

		String technicalError = String.format( "bad tokenBeginning; tokenBeginning = %d (%c)", tokenBeginning, (char)tokenBeginning);
		throw new BaseException( JsonHandler.class, technicalError);

	}

	public abstract void writeValue( Object value, JsonOutput output );

	public abstract Object readValue( JsonInput input );

}
