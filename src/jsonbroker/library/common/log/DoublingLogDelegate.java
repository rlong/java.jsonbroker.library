// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.log;

public class DoublingLogDelegate implements LogDelegate {

	private boolean _isDebugEnabled; 

	LogDelegate _primaryDelegate;
	LogDelegate _secondaryDelegate;
	
	public DoublingLogDelegate( boolean isDebugEnabled, LogDelegate primaryDelegate, LogDelegate secondaryDelegate ) {
		_isDebugEnabled = isDebugEnabled;
		_primaryDelegate = primaryDelegate;
		_secondaryDelegate = secondaryDelegate;
	}
	
	@Override
	public boolean isDebugEnabled() {
		return _isDebugEnabled;
	}

	@Override
	public void debug(Log origin, String message) {
		if( _isDebugEnabled ) {
			_primaryDelegate.debug( origin, message);
			_secondaryDelegate.debug( origin, message);
		}
	}

	@Override
	public void debug(Log origin, boolean value, String name) {
		if( _isDebugEnabled ) {
			_primaryDelegate.debug( origin, value, name);
			_secondaryDelegate.debug( origin, value, name);
		}
	}

	@Override
	public void debug(Log origin, int value, String name) {
		if( _isDebugEnabled ) {
			_primaryDelegate.debug( origin, value, name);
			_secondaryDelegate.debug( origin, value, name);
		}
	}

	@Override
	public void debug(Log origin, Loggable value, String name) {
		if( _isDebugEnabled ) {
			_primaryDelegate.debug( origin, value, name);
			_secondaryDelegate.debug( origin, value, name);
		}
	}

	@Override
	public void debug(Log origin, long value, String name) {
		if( _isDebugEnabled ) {
			_primaryDelegate.debug( origin, value, name);
			_secondaryDelegate.debug( origin, value, name);
		}
	}

	@Override
	public void debug(Log origin, Object value, String name) {
		if( _isDebugEnabled ) {
			_primaryDelegate.debug( origin, value, name);
			_secondaryDelegate.debug( origin, value, name);
		}
	}

	@Override
	public void debug(Log origin, String value, String name) {
		if( _isDebugEnabled ) {
			_primaryDelegate.debug( origin, value, name);
			_secondaryDelegate.debug( origin, value, name);
		}
	}

	@Override
	public void debugFormat(Log origin, String format, Object... args) {
		if( _isDebugEnabled ) {
			_primaryDelegate.debugFormat( origin, format, args);
			_secondaryDelegate.debugFormat( origin, format, args);
		}
	}

	@Override
	public void debugIp4Address(Log origin, int value, String name) {
		if( _isDebugEnabled ) {
			_primaryDelegate.debugIp4Address( origin, value, name);
			_secondaryDelegate.debugIp4Address( origin, value, name);
		}
	}

	@Override
	public void enteredMethod(Log origin) {
		if( _isDebugEnabled ) {
			_primaryDelegate.enteredMethod( origin );
			_secondaryDelegate.enteredMethod( origin );
		}
	}


	@Override
	public void info(Log origin, String message) {
		_primaryDelegate.info( origin, message);		
		_secondaryDelegate.info( origin, message);
	}


	@Override
	public void warn(Log origin, String message) {
		_primaryDelegate.warn( origin, message);
		_secondaryDelegate.warn( origin, message);
		
	}

	@Override
	public void error(Log origin, String message) {
		_primaryDelegate.error( origin, message);
		_secondaryDelegate.error( origin, message);		
	}

}
