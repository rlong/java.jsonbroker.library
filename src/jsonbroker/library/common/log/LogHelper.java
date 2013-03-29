// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.log;

public class LogHelper {
	
	
	
	
	public static void setupReleaseLog( Object caller ) {
		
		Class<?> callerClass;
		
		if( caller instanceof Class<?> ) {
			callerClass = (Class<?>)caller;
		}else {
			callerClass = caller.getClass();
		}
		
		String message = String.format( "[LogHelper setupReleaseLog] settting up release log for '%s'", callerClass.getName() );
		System.out.println(message); 
		
		ConsoleLogDelegate consoleLog = new ConsoleLogDelegate(false);
		ReleaseLogDelegate releaseLogFilter = new ReleaseLogDelegate(consoleLog);
		Log.setDelegate(releaseLogFilter); 
	}
	
	public static void setupReleaseLog( Object caller, LogDelegate logDelegate ) {
		
		Class<?> callerClass;
		
		if( caller instanceof Class<?> ) {
			callerClass = (Class<?>)caller;
		}else {
			callerClass = caller.getClass();
		}
		
		String message = String.format( "[LogHelper setupReleaseLog] settting up release log for '%s'", callerClass.getName() );
		System.out.println(message);
		
		ConsoleLogDelegate consoleLog = new ConsoleLogDelegate(false);
		DoublingLogDelegate logDoubleDelegator = new DoublingLogDelegate(false, consoleLog, logDelegate );
		ReleaseLogDelegate releaseLogFilter = new ReleaseLogDelegate(logDoubleDelegator);
		Log.setDelegate(releaseLogFilter);
		
	}


}
