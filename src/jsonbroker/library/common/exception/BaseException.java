// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.exception;

import java.util.ArrayList;
import java.util.HashMap;

import jsonbroker.library.common.auxiliary.ExceptionHelper;
import jsonbroker.library.common.log.Loggable;


public class BaseException extends RuntimeException implements Loggable {
		
	
	
	private static final int BASE = ErrorCodeUtilities.getBaseErrorCode("jsonbroker.BaseException");
	
	public static final int DEFAULT_FAULT_CODE = BASE | 0x01;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -6632414932623607359L;

	////////////////////////////////////////////////////////////////////////////
	
	Object _originator;
	
	public Object getOriginatingObject() {
		return _originator;
	}
	
	
    ///////////////////////////////////////////////////////////////////////
	//
    private HashMap<String, String> _context;
    
	///////////////////////////////////////////////////////////////////////
    // from XML-RPC
    private int _faultCode;

    
    public int getFaultCode() {
		return _faultCode;
	}

	public void setFaultCode(int faultCode) {
		_faultCode = faultCode;
	}
    
	////////////////////////////////////////////////////////////////////////////
	//
	String _underlyingFaultMessage;
	
	public String getUnderlyingFaultMessage() {
		return _underlyingFaultMessage;
	}

	public void setUnderlyingFaultMessage(String underlyingFaultMessage) {
		_underlyingFaultMessage = underlyingFaultMessage;
	}

	
	////////////////////////////////////////////////////////////////////////////
	// errorDomain	
	private String _errorDomain;

	public String getErrorDomain() {
		return _errorDomain;
	}

	public void setErrorDomain(String errorDomain) {
		_errorDomain = errorDomain;
	}

	////////////////////////////////////////////////////////////////////////////
	//
	public BaseException( Object originator, String technicalError ) {
		super( technicalError );
		_originator = originator;
        _faultCode = DEFAULT_FAULT_CODE;
        _context = new HashMap<String, String>();
	}
	
	public BaseException( Object originator, String format, Object ... values ) {
		super( String.format( format, values ) );
		_originator = originator;
        _faultCode = DEFAULT_FAULT_CODE;
        _context = new HashMap<String, String>();

	}

	
	public BaseException( Object originator, Throwable cause ) {
		super( cause );
		_originator = originator;
        _faultCode = DEFAULT_FAULT_CODE;
        _context = new HashMap<String, String>();
        _underlyingFaultMessage = ExceptionHelper.getUnderlyingFaultMessage( cause );

	}
	
	

	
	public String getOriginator() {
		
		if( _originator instanceof String ) {
			return (String)_originator;
		}
		
		Class<?> originatingClass;
		
		if( _originator instanceof Class<?> ) {
			originatingClass = (Class<?>)_originator;
		} else {
			originatingClass = _originator.getClass();
		}
		
		String packagedClassName = originatingClass.getName();
		
		int lineNumber = 0;
		String methodName = null;
		{
			StackTraceElement[] stackTrace = this.getStackTrace();
			
			for( int i = 0, count = stackTrace.length; i < count; i++ ) {

				if( packagedClassName.equals(stackTrace[i].getClassName()) ) {
					lineNumber = stackTrace[i].getLineNumber(); // can return '-1'
					methodName = stackTrace[i].getMethodName();
					break;
				}
			}
		}

		String className = null; 
		{
			int lastIndexOfDot = packagedClassName.lastIndexOf( '.' );
			if( -1 == lastIndexOfDot ) {
				className = packagedClassName;
			} else {
				className = packagedClassName.substring( lastIndexOfDot + 1 ); // +1 to skip over the '.'
			}
		}

		if( 0 < lineNumber ) {
			return String.format( "%s:%x", className, lineNumber);
		}
		if( null != methodName ) {
			return String.format( "%s:%s", className, methodName);
		} 
		
		return String.format( "%s:-1", className);
	}

	public void addContext( String key, long value ) {
		_context.put( key, Long.toString( value ));
	}

	public void addContext( String key, String value ) {
		_context.put( key, value);
	}

	
	@Override
	public String[] getLogMessages() {
		
		
		ArrayList<String> arrayList = new ArrayList<String>();
		
		arrayList.add( "faultCode = " +  this.getFaultCode() );
		arrayList.add( "faultMessage = " + this.getMessage() );
		arrayList.add( "underlyingFaultMessage = " + _underlyingFaultMessage);
		arrayList.add( "className = " + this.getClass().getName() );
		arrayList.add( "originator = " +  this.getOriginator() );
		
		arrayList.add( "stack = {"  );
		
		String[] stackTrace = ExceptionHelper.getStackTrace(this, true);
		for( int i = 0, count = stackTrace.length; i < count; i++ ) {
			arrayList.add( "    " + stackTrace[i] );
		}
		
		arrayList.add( "}"  );
				
        String[] answer = new String[arrayList.size()];
        arrayList.toArray(answer);
		return answer;

	}
	
}
