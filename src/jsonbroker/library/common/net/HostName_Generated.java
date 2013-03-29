// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

// generated ...

package jsonbroker.library.common.net;

public abstract class HostName_Generated {

	//////////////////////////////////////////////////////
	 String _applicationName;

	public String getApplicationName() {
		return _applicationName;
	}

	public void setApplicationName( String applicationName ) {
		_applicationName = applicationName;
	}

	//////////////////////////////////////////////////////
	 String _zeroconfName;

	public String getZeroconfName() {
		return _zeroconfName;
	}

	public void setZeroconfName( String zeroconfName ) {
		_zeroconfName = zeroconfName;
	}

	//////////////////////////////////////////////////////
	 String _dnsName;

	public String getDnsName() {
		return _dnsName;
	}

	public void setDnsName( String dnsName ) {
		_dnsName = dnsName;
	}

	//////////////////////////////////////////////////////
	public HostName_Generated(){
	}

	public HostName_Generated( jsonbroker.library.common.json.JsonObject values ){
		_applicationName = values.getString( "applicationName", null );
		_zeroconfName = values.getString( "zeroconfName", null );
		_dnsName = values.getString( "dnsName", null );
	}

	public jsonbroker.library.common.json.JsonObject toJsonObject() {
		jsonbroker.library.common.json.JsonObject answer = new jsonbroker.library.common.json.JsonObject();
		answer.put( "applicationName", _applicationName );
		answer.put( "zeroconfName", _zeroconfName );
		answer.put( "dnsName", _dnsName );
		return answer;
	}
}