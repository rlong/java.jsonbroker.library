// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.log;

public interface LogDelegate {

	public boolean isDebugEnabled();
	
	public void debug( Log origin, String message );
	public void debug( Log origin, boolean value, String name );
	public void debug( Log origin, int value, String name  );
	public void debug( Log origin, Loggable value, String name  );
	public void debug( Log origin, long value, String name  );
	public void debug( Log origin, Object value, String name  );
	public void debug( Log origin, String value, String name  );
	public void debugIp4Address( Log origin, int value, String name );
	public void debugFormat( Log origin, String format, Object ... args );

	public void enteredMethod(Log origin);

	public void info( Log origin, String message );	

	public void warn( Log origin, String message );	

	public void error( Log origin, String message );

}
