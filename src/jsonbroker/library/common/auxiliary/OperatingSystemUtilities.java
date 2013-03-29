// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.auxiliary;

import java.io.File;

import jsonbroker.library.common.log.Log;

public class OperatingSystemUtilities {
	
	private static Log log = Log.getLog(OperatingSystemUtilities.class);

	private static Boolean _isWindows;
	private static Boolean _isOsx;
	
	public static boolean isWindows() {
		
		
		if( null == _isWindows ) {

			String seperator =  "'" + File.separatorChar + "'";
			log.debug( seperator, "seperator" );

			
			if( '\\' == File.separatorChar ) { // windows machine ?
				_isWindows = Boolean.TRUE;
			} else {
				_isWindows = Boolean.FALSE;
			}
		}
		return _isWindows.booleanValue();
	}
	
	public static boolean isOsx() {
		
		if( null == _isOsx ) {
			
			File applications = new File( "/Applications");
			File users = new File( "/Users");
			if( applications.exists() && applications.isDirectory() && users.exists() && users.isDirectory() ) {
				_isOsx = Boolean.TRUE;
			} else {
				_isOsx = Boolean.FALSE;
			}
		}
		return _isOsx.booleanValue();
	}

}
