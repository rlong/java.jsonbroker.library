// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.broker;

import java.util.Map;

import jsonbroker.library.common.auxiliary.ExceptionHelper;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.json.JsonArray;
import jsonbroker.library.common.json.JsonObject;

public class FaultSerializer {
	
	private static JsonObject toJsonObject( BaseException baseException ) {
		
		JsonObject answer = new JsonObject();
		
		answer.put("errorDomain", baseException.getErrorDomain()); // null is ok
		
		answer.put( "faultCode", baseException.getFaultCode() );
		answer.put( "faultMessage", baseException.getMessage() );
		answer.put( "underlyingFaultMessage", baseException.getUnderlyingFaultMessage());
			
		answer.put( "originator", baseException.getOriginator() );
		
		String stackTrace[] = ExceptionHelper.getStackTrace(baseException, true);
		JsonArray jsonStackTrace = new JsonArray(stackTrace.length);
		
		for( int i = 0, count = stackTrace.length; i < count; i++ ) {
			jsonStackTrace.add( stackTrace[i] );
		}
		answer.put( "stackTrace", jsonStackTrace);
		
		JsonObject faultContext = new JsonObject();
		answer.put( "faultContext", faultContext);
		
		return answer;
		
	}
	
	
	private static String removePackagesFromClassName( String className ) {
		
		int lastIndexOfDot = className.lastIndexOf( '.' );
		if( -1 == lastIndexOfDot ) {
			return className;
		}

		String answer = className.substring( lastIndexOfDot + 1 ); // +1 to skip over the '.'
		
		return answer;
		
	}
	
	public static JsonObject toJsonObject( Throwable t  ) {
		if( t instanceof BaseException ) {
			return toJsonObject( (BaseException)t );
		}
		
		JsonObject answer = new JsonObject();
		
		answer.put("errorDomain", null); // null is ok
		
		answer.put( "faultCode", BaseException.DEFAULT_FAULT_CODE ); // 				
		answer.put( "faultMessage", t.getMessage() );
		answer.put( "underlyingFaultMessage", ExceptionHelper.getUnderlyingFaultMessage( t ));
		String stackTrace[] = ExceptionHelper.getStackTrace( t, true );
		JsonArray jsonStackTrace = new JsonArray(stackTrace.length);
		
		for( int i = 0, count = stackTrace.length; i < count; i++ ) {
			jsonStackTrace.add( stackTrace[i] );
		}
		answer.put( "stackTrace", jsonStackTrace);
		
		String originator;
		{
			StackTraceElement frame0 = t.getStackTrace()[0];
			String className = frame0.getClassName();  
			className = removePackagesFromClassName(className);
			int lineNumber = frame0.getLineNumber();
			String methodName = frame0.getMethodName();
			if( 0 < lineNumber ) {
				originator = String.format( "%s:%x", className, lineNumber);
			} else if( null != methodName ) {
				originator = String.format( "%s:%s", className,methodName);
			} else {
				originator = String.format( "%s:-1", className);
			}			 
		}
		answer.put( "originator", originator );
		
		JsonObject faultContext = new JsonObject();
		
		answer.put( "faultContext", faultContext);
		
		return answer;
		
	}
	
	public static BaseException toBaseException( JsonObject jsonObject ) {
		
		String originator = jsonObject.getString( "originator", "NULL");
		String faultMessage = jsonObject.getString( "faultMessage", "NULL");
		
		BaseException answer = new BaseException(originator, faultMessage);
		
		int faultCode = jsonObject.getInt( "faultCode", BaseException.DEFAULT_FAULT_CODE );
		answer.setFaultCode( faultCode );
		
		String underlyingFaultMessage = jsonObject.getString( "underlyingFaultMessage", null);
		answer.setUnderlyingFaultMessage( underlyingFaultMessage );
		
		
		JsonArray stack_trace = jsonObject.getJsonArray( "stackTrace", null );
		if( null != stack_trace ) {
			for( int i = 0, count = stack_trace.size(); i < count; i++ ) {
				String key = "cause[" + i + "]";
				String value = stack_trace.getString( i, "NULL" );
				answer.addContext( key, value);
			}
		}
		JsonObject fault_context = jsonObject.getJsonObject( "faultContext", null );
		if( null != fault_context ) {
			for( Map.Entry<String, Object> item : fault_context.entrySet() ) {
				
				Object value = item.getValue();
				if( null != value && value instanceof String ) { 
					String key = item.getKey();
					answer.addContext( key, (String)value);
				}
			}
		}

		return answer;
	}

}
