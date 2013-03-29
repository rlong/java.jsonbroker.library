// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json.handlers;

import jsonbroker.library.common.auxiliary.MutableData;
import jsonbroker.library.common.auxiliary.NumericUtilities;
import jsonbroker.library.common.json.input.JsonInput;
import jsonbroker.library.common.json.output.JsonOutput;


public final class JsonNumberHandler extends JsonHandler {

	
	private static final JsonNumberHandler _instance = new JsonNumberHandler();
	
	public static final JsonNumberHandler getInstance() {
		return _instance;
	}

	////////////////////////////////////////////////////////////////////////////
	
	private JsonNumberHandler() {
	}

	////////////////////////////////////////////////////////////////////////////

	
	public static void writeNumber( Number numberValue, JsonOutput output) {
		
		output.append( numberValue.toString());
		
	}
	

	@Override
	public final void writeValue(Object value, JsonOutput output) {

		JsonNumberHandler.writeNumber( (Number)value, output);
	}

	
	public static Number readNumber( JsonInput input) {
		
		MutableData data = input.reserveMutableData();
		
		try {
			boolean isFloat = false;
			
			byte b = input.currentByte();
			
			do { 

				if( '0' <= b && b <= '9' ) {
					data.append( b );
					b = input.nextByte();
					continue;
				}

				if( '.' == b || 'e' == b || 'E' == b ) {
					
					isFloat = true;
					data.append( b );
					b = input.nextByte();
					continue;
				}
				
				if( '+' == b || '-' == b ) {
					data.append( b );
					b = input.nextByte();
					continue;
				}
				
				// fallen through ... none of the chars above matched
				String stringValue = data.getUtf8String( 0, data.getCount()); 

				if( isFloat ) {
					return NumericUtilities.parseDoubleObject( stringValue );
				} else {
					long longValue = NumericUtilities.parseLong( stringValue );
					if( longValue > Integer.MAX_VALUE || longValue < Integer.MIN_VALUE ) {
						return new Long(longValue); 
					} else {
						return new Integer((int)longValue);
					}
				}
			} while( true );
		} finally  {
			input.releaseMutableData( data);
		}

	}
	
	@Override
	public final Object readValue(JsonInput input) {
		
		return JsonNumberHandler.readNumber( input );
		
	}


}
