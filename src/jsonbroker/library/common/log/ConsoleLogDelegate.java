// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.log;

public class ConsoleLogDelegate implements LogDelegate {
	
	
	
	private boolean _isDebugEnabled; 
	
	public ConsoleLogDelegate( boolean isDebugEnabled ) {
		_isDebugEnabled = isDebugEnabled;
	}

	@Override
	public boolean isDebugEnabled() {
		return _isDebugEnabled;
	}

	////////////////////////////////////////////////////////////////////////////
	//
	private void debug( String message) {
		System.out.println( message );
	}

	

	@Override
	public void debug(Log origin, String message) {
		if( _isDebugEnabled ) {
			String threadName = Thread.currentThread().getName();
			String methodName = LogDelegateHelper.getMethodName(origin);
			String line = String.format( "DBG %s [%s %s] %s", threadName, origin.getCallerClassName(), methodName, message);
			debug( line );
		}
	}
	

	@Override
	public void debug( Log origin, boolean value, String name ) {
		if( _isDebugEnabled ) {
			debug( origin, LogDelegateHelper.toString( value, name));
		}
	}
	
	
	@Override
	public void debug( Log origin, int value, String name  ) {
		if( _isDebugEnabled ) {
			debug( origin, LogDelegateHelper.toString( value, name));
		}
	}
	
	
	@Override
	public void debug( Log origin, Loggable value, String name  ) {
		if( _isDebugEnabled ) {
			debug( origin, LogDelegateHelper.toString( value, name));
		}
	}
	
	@Override
	public void debug( Log origin, long value, String name  ) {
		if( _isDebugEnabled ) {
			debug( origin, LogDelegateHelper.toString( value, name));
		}
	}

	@Override
	public void debug( Log origin, Object value, String name  ) {
		if( _isDebugEnabled ) {
			debug( origin, LogDelegateHelper.toString( value, name));
		}
	}

	@Override
	public void debug( Log origin, String value, String name  ) {
		if( _isDebugEnabled ) {
			debug( origin, LogDelegateHelper.toString( value, name));
		}
	}
	
	@Override
	public void debugIp4Address( Log origin, int value, String name ) {
		if( _isDebugEnabled ) {
			debug( origin, LogDelegateHelper.toString( value, name));
		}
	}
	
	@Override
	public void debugFormat( Log origin, String format, Object ... args ) {
		if( _isDebugEnabled ) {
			debug( origin, LogDelegateHelper.formatString(format, args));
		}
	}


	////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void enteredMethod(Log origin) {
		if( _isDebugEnabled ) {
			debug( origin, "entered");
		}		
	}

	
	////////////////////////////////////////////////////////////////////////////
	//
	private void info( String message) {
		System.out.println( message );
	}

	
	@Override
	public void info(Log origin, String message) {

		String line; 
		{
			if( _isDebugEnabled ) { 
				String threadName = Thread.currentThread().getName();
				String methodName = LogDelegateHelper.getMethodName(origin);
				line = String.format( "INF %s [%s %s] %s", threadName, origin.getCallerClassName(), methodName, message);
			} else {
				String threadName = Thread.currentThread().getName();
				line = String.format( "INF %s [%s -] %s", threadName, origin.getCallerClassName(), message);
			}
		}
		
		info( line );
		
	}

	////////////////////////////////////////////////////////////////////////////
	//
	private void warn( String message) {
		System.err.println( message );
	}

	@Override
	public void warn(Log origin, String message) {
		
		String threadName = Thread.currentThread().getName();
		String methodName = LogDelegateHelper.getMethodName(origin);
		String line = String.format( "WRN %s [%s %s] %s", threadName, origin.getCallerClassName(), methodName, message);
		
		warn( line );
		
	}

	////////////////////////////////////////////////////////////////////////////
	//
	private void error( String message) {
		System.err.println( message );
	}

	@Override
	public void error(Log origin, String message) {

		String threadName = Thread.currentThread().getName();
		String methodName = LogDelegateHelper.getMethodName(origin);
		String line = String.format( "ERR %s [%s %s] %s", threadName, origin.getCallerClassName(), methodName, message);
		
		error( line );
	}

	

}
