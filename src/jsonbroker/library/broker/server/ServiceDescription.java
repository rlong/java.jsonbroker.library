// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.broker.server;

public class ServiceDescription {
	
	private String _serviceName;

	public String getServiceName() {
		return _serviceName;
	}

	public ServiceDescription( String serviceName ) {
		_serviceName = serviceName;
	}
	
	public int getMajorVersion() {
		return 1;
	}
	
	public int getMinorVersion() {
		return 0;
	}

}
