// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.net;

import java.net.InetAddress;

import jsonbroker.library.common.json.JsonObject;



public class NetworkAddress extends NetworkAddress_Generated {
	

	
	

	////////////////////////////////////////////////////////////////////////////
	//
	String _hostName;


	////////////////////////////////////////////////////////////////////////////
	//
	NetworkHost _networkHost = null;
	

	public  NetworkHost getNetworkHost() {
		return _networkHost;
	}


	////////////////////////////////////////////////////////////////////////////
	//  Instance lifecycle 

	
	public NetworkAddress( InetAddress inetAddress, int port ) {
		_networkHost = new NetworkHost( inetAddress );
		_port = port;
	}
	
	public NetworkAddress( byte[] inetAddressBytes, int port ) {
		_networkHost = new NetworkHost( inetAddressBytes );
		_port = port;
	}
	
	public NetworkAddress( String hostName, int port ) {
		
		_hostName = hostName;
		_networkHost = new NetworkHost( hostName );
		_port = port;
//		InetAddress inetAddress = InetAddress.getByName( hostAddress );
		
	}
	

	////////////////////////////////////////////////////////////////////////////
	//  Other
	
	public String getHostAddress() {
		if( null != _hostName ) {
			return _hostName;
		}
		return _networkHost.getHostAddress();
	}
	
	public String toString() {
		return getHostAddress() + ":" + _port;
	}

	public boolean isEqualToNetworkAddress( NetworkAddress other ) {
		if( this == other ) {
			return true;
		}
		
		if( this._port != other._port ) {
			return false;
		}
		
		return _networkHost.isEqualToNetworkHost( other._networkHost );
	}
	
	public String getLabel() {
		
		return _networkHost.getLabel();

	}
	
	
	public JsonObject toJsonObject() {
		
		
		JsonObject answer = super.toJsonObject();
		answer.put( "network_host",_networkHost.toJSONObject());
			
		
		return answer;
		
	}

}
