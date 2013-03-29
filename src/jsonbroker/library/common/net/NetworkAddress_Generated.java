// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

// generated ...

package jsonbroker.library.common.net;

public abstract class NetworkAddress_Generated {

	//////////////////////////////////////////////////////
	 int _port;

	public int getPort() {
		return _port;
	}

	 void setPort( int port ) {
		_port = port;
	}

	//////////////////////////////////////////////////////
	public NetworkAddress_Generated(){
	}

	public NetworkAddress_Generated( jsonbroker.library.common.json.JsonObject values ){
		_port = values.getInteger( "port" );
	}

	public jsonbroker.library.common.json.JsonObject toJsonObject() {
		jsonbroker.library.common.json.JsonObject answer = new jsonbroker.library.common.json.JsonObject();
		answer.put( "port", _port );
		return answer;
	}
}