// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.security;

public interface ClientSecurityConfiguration {

	
	////////////////////////////////////////////////////////////////////////////
	// ServerSecurityConfiguration::create
	
	public void addServer(Subject server);

	////////////////////////////////////////////////////////////////////////////
	// ClientSecurityConfiguration::read

	public String getUsername();
	
	public Subject getServer( String realm );
}
