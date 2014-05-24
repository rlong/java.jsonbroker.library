// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.log;

public class LogDelegateHelper {

	
	
	
	public static String getMethodName(Log origin) {
		
		// 'origin._targetClass' can be null
		if( null == origin._targetClass ) {
			return "-";
		}
		
		Exception e = new Exception();
		StackTraceElement[] stackTrace = e.getStackTrace();
		
		String targetClassName = origin._targetClass.getName();
		
		for( int i = 0; i < stackTrace.length; i++ ) {
			
			if( targetClassName.equals(stackTrace[i].getClassName()) ) {
				return stackTrace[i].getMethodName();
			}
		}
		return "?";
		
	}
	
	public static String toString( boolean value, String name ) { 
		return name + " = " + value;
	}
	
	public static String toString( int value, String name ) { 
		return name + " = " + value;
	}
	

	public static String toString( Loggable value, String name ) {
		
		
        if (null == value)
        {
        	return name + " = NULL";
        }

		String[] messages = value.getLogMessages();
		if( 0 == messages.length ) {
			return name + " = {}";
		}
		
		if( 1 == messages.length ) {
			return name + " = " + messages[0];
		}
		
		StringBuilder answer = new StringBuilder( name );
		answer.append( " = {" );
		
		for( int i = 0, count = messages.length; i < count; i++ ) {
			answer.append( "    " );
			answer.append( messages[i] );
			answer.append( "\n" );
		}
		
		answer.append( "}" );
		
		return answer.toString();
		
	}
	
	public static String toString( long value, String name ) { 
		return name + " = " + value;
	}

	public static String toString( Object value, String name ) { 
		if( null == value ) {
			return name + " = NULL";
		}			
		return name + " = " + value.toString();
	}

	
	public static String toString( String value, String name ) { 
		if( null == value ) {
			return name + " = NULL";
		}			
		return name + " = '" + value + "'";
	}

	public static String toIp4AddressString( int value, String name ) {
		
		int ip1 = value & 0xFF;
		int ip2 = ( value >> 8 ) & 0xFF;
		int ip3 = ( value >> 16 ) & 0xFF;
		int ip4 = ( value >> 24 ) & 0xFF;
		
		String answer = String.format( "%s = %d.%d.%d.%d", name, ip1, ip2, ip3, ip4);
		return answer;
	}
	
	public static String formatString( String format, Object ... args ) {
		String answer = String.format( format, args );
		return answer;
	}

	
}
