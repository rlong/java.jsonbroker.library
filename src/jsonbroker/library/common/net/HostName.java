// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.net;


public class HostName extends HostName_Generated {


	private static boolean compare( String s1, String s2 ) {
		if( s1 == null ) {
			if( s2 == null ) {
				return true;
			} else {
				return false;
			}			
		}
		return s1.equals( s2 );
	}
	
	public boolean isEqualToHostName( HostName other ) {
		
		if( this == other ) {
			return true;
		}
		
		if( !compare( _applicationName, other._applicationName ) ) {
			return false;
		}

		if( !compare( _zeroconfName, other._zeroconfName ) ) {
			return false;
		}
		
		if( !compare( _dnsName, other._dnsName ) ) {
			return false;
		}
		
		return true;

	}
	
	public String toString() {

		if( null != _applicationName ) {
			return _applicationName;
		}
		
		if( null != _zeroconfName ) {
			return _zeroconfName;
		}
		
		if( null != _dnsName ) {
			return _dnsName;
		}		
		return null;
	}
	

}
