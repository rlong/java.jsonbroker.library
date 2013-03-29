// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.security;

import java.util.Collection;

public interface ServerSecurityConfiguration {
	
	
	////////////////////////////////////////////////////////////////////////////
	// ServerSecurityConfiguration::create
	
	public void addClient(Subject client);
	
	////////////////////////////////////////////////////////////////////////////
	// ServerSecurityConfiguration::read 
	public String getRealm();
	
	public boolean hasClient( String clientUsername);
	// can return null
	public Subject getClient( String clientUsername);
	public Collection<Subject> getClients();

	////////////////////////////////////////////////////////////////////////////
	// ServerSecurityConfiguration::delete 
	public void removeClient(String clientUsername);
	
	
}
