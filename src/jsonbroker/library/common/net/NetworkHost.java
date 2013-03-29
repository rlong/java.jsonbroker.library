// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.net;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.json.JsonArray;
import jsonbroker.library.common.json.JsonObject;

public class NetworkHost {

	
	////////////////////////////////////////////////////////////////////////////
	InetAddress _inetAddress;

	public InetAddress getInetAddress() {
		return _inetAddress;
	}

	public void setInetAddress(InetAddress inetAddress) {
		_inetAddress = inetAddress;
	}

	////////////////////////////////////////////////////////////////////////////
	HostName _hostName;

	public HostName getHostName() {
		return _hostName;
	}
		
	public NetworkHost( byte[] inetAddressBytes ) {
		
		try {
			_inetAddress = InetAddress.getByAddress(inetAddressBytes);
		} catch (UnknownHostException e) {
			throw new BaseException( NetworkAddress.class , e );
		}
		_hostName = new HostName();
	}
	
	public NetworkHost( String dnsName) {
		
		try {
			_inetAddress = InetAddress.getByName( dnsName );
		} catch (UnknownHostException e) {
			throw new BaseException( NetworkAddress.class , e );
		}
		_hostName = new HostName();
		_hostName.setDnsName( dnsName );
	}
	
	public NetworkHost( InetAddress inetAddress ) {
		
		_inetAddress = inetAddress;
		_hostName = new HostName();
	}
	
	
	public boolean isEqualToNetworkHost( NetworkHost other ) {
		
		if( this == other ) {
			return true;
		}
		
		if( null == _inetAddress ) {
			if( null != other._inetAddress ) {
				return false;
			}
		} else if( !_inetAddress.equals( other._inetAddress )) {
			return false;
		}
		
		return _hostName.isEqualToHostName( other._hostName );
	}
	
	public String getHostAddress() {
		if( null == _inetAddress ) {
			return "0.0.0.0";
		}
		return _inetAddress.getHostAddress();
	}

	public String getLabel() {
		
		String hostName = _hostName.toString();
		if( hostName != null ) {
			return hostName;
		}
		
		return getHostAddress();

	}
	
	
	public JsonObject toJSONObject() {
		
		JsonArray inetAddress = null;
		
		if( null != _inetAddress ) {
			byte[] ip4 = _inetAddress.getAddress();
			
			inetAddress = new JsonArray(ip4.length);
			for( int i = 0, count = ip4.length; i < count; i++ ) {
				inetAddress.add( new Integer(ip4[i]));
			}
		}

		JsonObject answer = new JsonObject(
				"inet_address",inetAddress,
				"host_name",_hostName.toJsonObject());
		
		return answer;
	}
	
	
	public InetSocketAddress buildInetSocketAddress( int port ) { 
		InetSocketAddress answer = new InetSocketAddress( _inetAddress, port );
		return answer;
	}

}
