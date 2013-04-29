// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json.input;

public class JsonInputHelper {

	
	private static boolean doesByteBeginToken( byte candidateByte ) {
		
		if( ' ' == candidateByte) {
			return false;
		}
		
		if( '\t' == candidateByte) {
			return false;
		}

		
		if( '\n' == candidateByte) {
			return false;
		}

		if( '\r' == candidateByte) {
			return false;
		}

		if( ',' == candidateByte) {
			return false;
		}

		if( ':' == candidateByte) {
			return false;
		}
		
		return true;
	}

	public static byte scanToNextToken(JsonInput jsonInput ) {
		
		byte currentByte = jsonInput.currentByte();
		
		if( doesByteBeginToken(currentByte) ) { 
			return currentByte;
		}
		
		do { 
			currentByte = jsonInput.nextByte();
		} while( !doesByteBeginToken(currentByte) );
		
		return currentByte;

	}

}
