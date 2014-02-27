// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.log;

import java.util.ArrayList;


public class Log {

	////////////////////////////////////////////////////////////////////////////
	//
	private static LogDelegate _delegate = new ConsoleLogDelegate(true); 

	public static LogDelegate getDelegate() {
		return _delegate;
	}
	
	public static void setDelegate(LogDelegate delegate) {
		_delegate = delegate;
	}

	////////////////////////////////////////////////////////////////////////////
	//
	Class<?> _targetClass;
	
	public Class<?> getTargetClass() {
		return _targetClass;
	}

	////////////////////////////////////////////////////////////////////////////
	//
	protected String _callerClassName;
	
	
	public String getCallerClassName() {
		return _callerClassName;
	}


	////////////////////////////////////////////////////////////////////////////
	

	protected Log( Class<?> c ) {
		_targetClass = c;
		
		int lastIndexOfDot = _targetClass.getName().lastIndexOf( '.' );
		if( -1 == lastIndexOfDot ) {
			_callerClassName = _targetClass.getName();
		} else {
			_callerClassName = _targetClass.getName().substring( lastIndexOfDot + 1 ); // +1 to skip over the '.'
		}
	}

	public static Log getLog( Class<?> c ) {
		return new Log( c );
		
	}

	
	private static String[] toLogMessages(Throwable t) {
		
		ArrayList<String> arrayList = new ArrayList<String>();
		
		arrayList.add( "message = " + t.getMessage() );
		arrayList.add( "className = " + t.getClass().getName() );
		arrayList.add( "stack = {"  );

		StackTraceElement[] stackTrace = t.getStackTrace();
		
		for( int i = 0, count = stackTrace.length; i < count; i++ ) {
			arrayList.add( "    " + stackTrace[i].toString() );
		}
		
		arrayList.add( "}"  );
				
        String[] answer = new String[arrayList.size()];
        arrayList.toArray(answer);
		return answer;

		///////////////////////////////////////
	}


	public static boolean isDebugEnabled() { 
		return _delegate.isDebugEnabled();
	}
	

	////////////////////////////////////////////////////////////////////////////

	public void debug( String message ) {

		_delegate.debug( this, message);
	}

	
	

	public void debug( boolean value, String name ) {		
		_delegate.debug( this, value, name);
	}
	
	public void debug( int value, String name  ) {
		_delegate.debug( this, value, name);		
	}
		
	public void debug( Loggable value, String name  ) {
		
		_delegate.debug( this, value, name);
		
	}

	public void debug( long value, String name  ) {
		_delegate.debug( this, value, name);
	}

	public void debug( Object value, String name ) {
		_delegate.debug( this, value, name);
	}
	
	public void debug( String value, String name ) {
		
		_delegate.debug( this, value, name);
	}
	
	public void debug( String value, String name, int index ) {
		
		name = name + "[" + index + "]";
		_delegate.debug( this, value, name);
		
	}
	

	
	public void debugIp4Address( int value, String name ) {
		
		_delegate.debugIp4Address( this, value, name);
	
	}
	
	public void debugFormat( String format, Object ... args ) {
		_delegate.debugFormat( this, format, args);
	}

	/////////////////////////////////////////////////////////////////////////////////////
	
	public void enteredMethod() {
		_delegate.enteredMethod( this);
	}

	/////////////////////////////////////////////////////////////////////////////////////
	
	public void info( String message ) {
		_delegate.info( this, message);
	}
	
	
	public void info( boolean value, String name ) {		
		info( LogDelegateHelper.toString( value, name) );
	}


	public void info( String value, String name ) {
		info( LogDelegateHelper.toString( value, name) );
	}
	
	
	public void info( Loggable value, String name  ) {
		
		info( LogDelegateHelper.toString( value, name) );
	}

	
	public void info( long value, String name ) {
		info( LogDelegateHelper.toString( value, name) );
	}

	public void infoFormat( String format, Object ... args ) {
		info( LogDelegateHelper.formatString( format, args) );
	}

	
	////////////////////////////////////////////////////////////////////////////
	
	public void warn( String message ) {
		_delegate.warn( this, message);
	}
	
	
	public void warn( Loggable value, String name  ) {

		warn( LogDelegateHelper.toString( value, name) );

	}

	
	public void warn( long value, String name  ) {
		warn( LogDelegateHelper.toString( value, name) );
	}

	public void warn( Object value, String name ) {
		warn( LogDelegateHelper.toString( value, name) );
	}

	public void warn( String value, String name ) {
		warn( LogDelegateHelper.toString( value, name) );
	}
	
	
	public synchronized void warn( Throwable t ) {
		
		String[] messages = null;
		if( t instanceof Loggable ) {
			Loggable loggable = (Loggable)t;
			messages = loggable.getLogMessages();
		} else { 
			messages = toLogMessages( t );
		}
		
		for( int i = 0, count = messages.length; i < count; i++ ) {
			warn( messages[i] );
		}
		
	}

	public void warnFormat( String format, Object ... args ) {
		warn( LogDelegateHelper.formatString(format, args) );
	}

	public void warnUnexpectedObjectType( Object object, Class<?> expectedType ) {
		String message = String.format( "unexpected type %s, expected %s type", object.getClass().getName(), expectedType.getName() );
		warn( message );
		
	}

	////////////////////////////////////////////////////////////////////////////

	public void error( String message ) {
		_delegate.error(this, message);
	}
	
	
	public void error( Loggable value, String name  ) {
		
		error( LogDelegateHelper.toString( value, name) );
	}

	public void error( long value, String name  ) {
		error( LogDelegateHelper.toString( value, name) );
	}

	public void error( String value, String name ) {
		error( LogDelegateHelper.toString( value, name) );
	}
	
	
	public synchronized void error( Throwable t ) {
		
		String[] messages = null;
		if( t instanceof Loggable ) {
			Loggable loggable = (Loggable)t;
			messages = loggable.getLogMessages();
		} else { 
			messages = toLogMessages( t );
		}
		
		for( int i = 0, count = messages.length; i < count; i++ ) {
			error( messages[i] );
		}
	}
	
	public void errorFormat( String format, Object ... args ) {
		error( LogDelegateHelper.formatString(format, args) );
	}

}
