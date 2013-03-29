// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

import jsonbroker.library.common.exception.BaseException;




public class NetUtil {


	private static String[] escapedChars = new String[256];
	
	protected static final String _allowedChars = ":/=,!$&'()*+;@?";

	static {
		
		String dontEscape = "dontEscape";
		
		for( int i = '0'; i <= '9'; i++ ){ 
			escapedChars[ i ] = dontEscape;
		}

		for( int i = 'a'; i <= 'z'; i++ ){ 
			escapedChars[ i ] = dontEscape;
		}
		
		for( int i = 'A'; i <= 'Z'; i++ ){ 
			escapedChars[ i ] = dontEscape;
		}

		for( int i = 0; i < _allowedChars.length(); i++ ) {
			char allowedChar = _allowedChars.charAt( i );
			escapedChars[ allowedChar ] = dontEscape;
		}
		

		// from section 2.3 of RFC 2396
		char[] unreservedChars = { '-', '_', '.', '!', '~', '*', '\'', '(', ')' };
		
		for( int i = 0; i < unreservedChars.length; i++ ) {
			char unreservedChar = unreservedChars[i];
			escapedChars[ unreservedChar ] = dontEscape;
		}
		
		
		char[] nibble = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		
		for( int i = 0; i < escapedChars.length; i++ ) {
			
			if( dontEscape ==  escapedChars[ i ] ) {
				escapedChars[ i ] = null;
			} else {
				char upperNibble = nibble[ i >> 4];
				char lowerNibble = nibble[ i & 0xF ];
				escapedChars[ i ] = "%" + upperNibble + lowerNibble;
			}
		}		

	}

	////////////////////////////////////////////////////////////////////////////
	private static NetUtil _instance = new NetUtil();
	
	public static NetUtil getInstance() {
		
		return _instance;
		
	}
	
	public static void setInstance( NetUtil instance ) {
		_instance = instance;		
	}

	////////////////////////////////////////////////////////////////////////////

	
	
	// see also RFC-2396
	public String escapeString( String input ) {
		
		StringBuffer answer = new StringBuffer();
		for( int i = 0; i < input.length(); i++ ) {
			char c = input.charAt( i );
			String escapedChar = escapedChars[ c ];
			if( null == escapedChar ) {
				answer.append( c );
			} else {
				answer.append( escapedChar );
			}
		}
		
		return answer.toString();
	}

	public InetAddress getWifiIpAddress() {
		
		try {
			return InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			throw new BaseException( this, e );
		}
	}


}
