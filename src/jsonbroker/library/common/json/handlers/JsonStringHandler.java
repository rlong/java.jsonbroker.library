// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json.handlers;

import jsonbroker.library.common.auxiliary.MutableData;
import jsonbroker.library.common.json.input.JsonInput;
import jsonbroker.library.common.json.output.JsonOutput;


public final class JsonStringHandler extends JsonHandler {
	

	
	public static final JsonStringHandler INSTANCE = new JsonStringHandler();
	
	
	////////////////////////////////////////////////////////////////////////////
	
	private JsonStringHandler() {
	}

	////////////////////////////////////////////////////////////////////////////

	


	public static String readString( JsonInput input) {
		
		MutableData data = input.getMutableDataPool().reserveMutableData();

		try {
			byte b;
			
			while( true ) {
				
				b = input.nextByte();
				if('"' == b ) {
					String answer = data.getUtf8String( 0, data.getCount());
					
					input.nextByte(); // move past the '"'
					
					return answer;
				}
				
				if( '\\' != b ) {
					data.append( b );
					
					continue;
				}
				
				b = input.nextByte();
				if ('"' == b || '\\' == b || '/' == b ) {
					data.append( b );
					continue;
				}
				if ('n' == b ) {
					data.append( (byte)'\n' );
					continue;
				}
				if ('t' == b ) {
					data.append( (byte)'\t' );
					continue;
				}
				if ('r' == b ) {
					data.append( (byte)'\r' );
					continue;
				}
			}
		} finally {
			input.getMutableDataPool().releaseMutableData( data);
		}

	}

	@Override
	public final Object readValue(JsonInput reader) {
		
		return JsonStringHandler.readString(reader);
		
	}
	
	
	public static void writeString( String value, JsonOutput output) {
		
		output.append( '"' );
		
		for( int i = 0, count = value.length(); i < count; i++ ) {
			char c = value.charAt(i);

			if( '"' == c ) {
				output.append( "\\\"" );
				continue;
			}
			
			if( '\\' == c ) {
				output.append( "\\\\" );
				continue;
			}
			
			if( '/' == c ) { 
				output.append( "\\/" );
				continue;
			}
			
			if( '\n' == c ) {
				output.append( "\\n" );
				continue;				
			}

			if( '\r' == c ) {
				output.append( "\\r" );
				continue;				
			}

			if( '\t' == c ) {
				
				output.append( "\\t" );
				continue;
			}

			output.append(c);
		}
		
		output.append( '"' );

	}
	
	@Override
	public final void writeValue(Object value, JsonOutput output) {
		
		JsonStringHandler.writeString( (String)value, output);
	}

}
