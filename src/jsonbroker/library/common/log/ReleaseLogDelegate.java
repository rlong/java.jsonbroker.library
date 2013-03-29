// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.log;

public class ReleaseLogDelegate implements LogDelegate {

	
	LogDelegate _delegate;
	
	public ReleaseLogDelegate( LogDelegate delegate ) {
		_delegate = delegate;
	}
	
	@Override
	public boolean isDebugEnabled() {
		
		return false;
	}

	@Override
	public void debug(Log origin, String message) {
	}

	@Override
	public void debug(Log origin, boolean value, String name) {
	}

	@Override
	public void debug(Log origin, int value, String name) {
	}

	@Override
	public void debug(Log origin, Loggable value, String name) {
	}

	@Override
	public void debug(Log origin, long value, String name) {
	}

	@Override
	public void debug(Log origin, Object value, String name) {
	}

	@Override
	public void debug(Log origin, String value, String name) {
	}

	@Override
	public void debugFormat(Log origin, String format, Object... args) {
	}

	@Override
	public void debugIp4Address(Log origin, int value, String name) {
	}

	@Override
	public void enteredMethod(Log origin) {
	}


	@Override
	public void info(Log origin, String message) {
		_delegate.info( origin, message);		
	}


	@Override
	public void warn(Log origin, String message) {
		_delegate.warn( origin, message);
		
	}

	@Override
	public void error(Log origin, String message) {
		_delegate.error( origin, message);
		
	}

}
