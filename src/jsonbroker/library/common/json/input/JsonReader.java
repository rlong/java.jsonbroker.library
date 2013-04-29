// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json.input;

import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.json.JsonDocumentHandler;
import jsonbroker.library.common.json.handlers.JsonBooleanHandler;
import jsonbroker.library.common.json.handlers.JsonHandler;
import jsonbroker.library.common.json.handlers.JsonNullHandler;
import jsonbroker.library.common.json.handlers.JsonNumberHandler;
import jsonbroker.library.common.json.handlers.JsonStringHandler;

public class JsonReader {

	
	private static void readArray( JsonInput input, JsonDocumentHandler handler ) {
		
		int index = -1;
		
		for( byte nextTokenStart = JsonInputHelper.scanToNextToken( input ); ']' != nextTokenStart; nextTokenStart = JsonInputHelper.scanToNextToken( input )) {
			
			index++;
			
			if( '"' == nextTokenStart ) {
				String value = JsonStringHandler.readString( input );
				handler.onString( index, value);
				continue;
			}
			
			if( '0' <= nextTokenStart && nextTokenStart <= '9' ) {
				Number value = JsonNumberHandler.readNumber( input );
				handler.onNumber( index, value);
				continue;
			}
			
			if( '[' == nextTokenStart ) {
				handler.onArrayStart( index );
				input.nextByte(); // skip past the '['
				readArray( input, handler);
				input.nextByte(); // skip past the ']'
				handler.onArrayEnd( index );
				continue;
			}
			
			if( '{' == nextTokenStart ) {
				handler.onObjectStart( index );
				input.nextByte(); // skip past the '{'
				readObject( input, handler);
				input.nextByte(); // skip past the '}'
				handler.onObjectEnd( index );
				continue;
			}
			
			if( '+' == nextTokenStart ) {
				Number value = JsonNumberHandler.readNumber( input );
				handler.onNumber( index, value);
				continue;
			}
			if( '-' == nextTokenStart ) {
				Number value = JsonNumberHandler.readNumber( input );
				handler.onNumber( index, value);
				continue;
			}
			
			if( 't' == nextTokenStart || 'T' == nextTokenStart) {
				Boolean value = JsonBooleanHandler.readBoolean( input );
				handler.onBoolean( index, value);
				continue;
			}
			
			if( 'f' == nextTokenStart || 'F' == nextTokenStart) {
				Boolean value = JsonBooleanHandler.readBoolean( input );
				handler.onBoolean( index, value);
				continue;
			}
			
			if( 'n' == nextTokenStart || 'N' == nextTokenStart) {
				JsonNullHandler.readNull( input );
				handler.onNull( index );
				continue;
			}

			String technicalError = String.format( "bad tokenBeginning; nextTokenStart = %d (%c)", nextTokenStart, (char)nextTokenStart);
			throw new BaseException( JsonHandler.class, technicalError);

		}

	}

	
	private static void readObject( JsonInput input, JsonDocumentHandler handler ) {
		
		for( byte nextTokenStart = JsonInputHelper.scanToNextToken( input ); '}' != nextTokenStart; nextTokenStart = JsonInputHelper.scanToNextToken( input )) {
			
			String key = JsonStringHandler.readString( input );
			
			nextTokenStart = JsonInputHelper.scanToNextToken( input );
					
			if( '"' == nextTokenStart ) {
				String value = JsonStringHandler.readString( input );
				handler.onString( key, value);
				continue;
			}
			
			if( '0' <= nextTokenStart && nextTokenStart <= '9' ) {
				Number value = JsonNumberHandler.readNumber( input );
				handler.onNumber( key, value);
				continue;
			}
			
			if( '[' == nextTokenStart ) {
				handler.onArrayStart( key );
				input.nextByte(); // skip past the '['
				readArray( input, handler);
				input.nextByte(); // skip past the ']'
				handler.onArrayEnd( key );
				continue;
			}
			
			if( '{' == nextTokenStart ) {
				handler.onObjectStart( key );
				input.nextByte(); // skip past the '{'
				readObject( input, handler);
				input.nextByte(); // skip past the '}'
				handler.onObjectEnd( key );
				continue;
			}
			
			if( '+' == nextTokenStart ) {
				Number value = JsonNumberHandler.readNumber( input );
				handler.onNumber( key, value);
				continue;
			}
			if( '-' == nextTokenStart ) {
				Number value = JsonNumberHandler.readNumber( input );
				handler.onNumber( key, value);
				continue;
			}
			
			if( 't' == nextTokenStart || 'T' == nextTokenStart) {
				Boolean value = JsonBooleanHandler.readBoolean( input );
				handler.onBoolean( key, value);
				continue;
			}
			
			if( 'f' == nextTokenStart || 'F' == nextTokenStart) {
				Boolean value = JsonBooleanHandler.readBoolean( input );
				handler.onBoolean( key, value);
				continue;
			}
			
			if( 'n' == nextTokenStart || 'N' == nextTokenStart) {
				JsonNullHandler.readNull( input );
				handler.onNull( key );
				continue;
			}

			String technicalError = String.format( "bad tokenBeginning; nextTokenStart = %d (%c)", nextTokenStart, (char)nextTokenStart);
			throw new BaseException( JsonHandler.class, technicalError);
			
		}
		
	}
	
	public static void read( JsonInput input, JsonDocumentHandler handler ) {
		
		byte nextByte = input.currentByte();
		input.nextByte(); // skip past the '{' / '['
		if( '{' == nextByte ) {
			
			handler.onObjectDocumentStart();
			readObject( input, handler);
			handler.onObjectDocumentEnd();
			
		} else {
			
			handler.onArrayDocumentStart();
			readArray( input, handler);
			handler.onArrayDocumentEnd();
			
		}
		
	}

}
