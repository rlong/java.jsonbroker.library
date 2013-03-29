// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.auxiliary;

import jsonbroker.library.common.exception.BaseException;

public class ExceptionHelper {
	
	
	
	public static Throwable getRootCause( Throwable t ) {

		// get to the root cause of the problem
		while( null != t.getCause() ) {
			t = t.getCause();
		}		
		return t;
	}
	
	// will return null if there is not underlying cause
	public static String getUnderlyingFaultMessage( Throwable t ) {
		
		Throwable underlyingFault = getRootCause( t );
			
		// guard against chaining of 'BaseException's 
		if( t instanceof BaseException ) {
			return null;
		}
		
		return underlyingFault.getMessage();
	}
	
	
	public static String[] getStackTrace(Throwable t, boolean rootCause ) {
		
		if( rootCause ) {
			t = getRootCause( t );
		}
		
		StackTraceElement[] stackTrace = t.getStackTrace();
		
		String[] answer = new String[stackTrace.length];
		
		for( int i = 0, count = stackTrace.length; i < count; i++ ) {
			
			answer[i] = stackTrace[i].toString().trim();
			
		}
		
		return answer;
		
	}
	

}
