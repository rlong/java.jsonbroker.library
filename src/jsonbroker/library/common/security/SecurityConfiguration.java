// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.security;

import java.util.Collection;
import java.util.HashMap;

import jsonbroker.library.common.json.JsonArray;
import jsonbroker.library.common.json.JsonObject;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.service.configuration.ConfigurationService;

public final class SecurityConfiguration implements ClientSecurityConfiguration, ServerSecurityConfiguration {
	
	private static final Log log = Log.getLog(SecurityConfiguration.class);

	
	public static final SecurityConfiguration TEST = buildTestConfiguration();
	
		
	//////////////////////////////////////////////////////
	private String _identifier;

	////////////////////////////////////////////////////////////////////////////
	//
	ConfigurationService _configurationService;

	////////////////////////////////////////////////////////////////////////////
	//
	HashMap<String, Subject> _clients;

	////////////////////////////////////////////////////////////////////////////
	//
	HashMap<String, Subject> _servers;

	
	////////////////////////////////////////////////////////////////////////////
	//
	private SecurityConfiguration( String identifier, ConfigurationService configurationService ) {
		
		_identifier = identifier;
		_configurationService = configurationService;
		
		_clients = new HashMap<String, Subject>();
		_servers = new HashMap<String, Subject>();
		
		
	}
	
	 

	private SecurityConfiguration( JsonObject value, ConfigurationService configurationService ) {
		
		_identifier = value.getString( "identifier" );
		log.debug( _identifier, "_identifier" );
		_configurationService = configurationService;
		 
		_clients = new HashMap<String, Subject>();
		_servers = new HashMap<String, Subject>();

		{ // subjects
			
			JsonArray registeredSubjects = value.getJsonArray( "subjects" );
			
			for( int i = 0, count = registeredSubjects.size(); i < count; i++ ) {
				
				JsonObject subjectData = registeredSubjects.getJsonObject( i );
				
				String subjectIdentifier = subjectData.getString( "identifier" );
				String subjectPassword = subjectData.getString( "password" );
				String subjectLabel = subjectData.getString( "label" );
				
				this.addSubject( subjectIdentifier, subjectPassword, subjectLabel);
			}
		}
	}
	
	public static SecurityConfiguration buildTestConfiguration() {
		
		SecurityConfiguration answer = new SecurityConfiguration( Subject.TEST_REALM, null );
		answer.addClient( Subject.TEST );
		
		return answer;
	}
	
	public static SecurityConfiguration build( SecurityAdapter identifierProvider, ConfigurationService configurationService ) {
		
		JsonObject bundleData = configurationService.getBundle( SimpleSecurityAdapter.BUNDLE_NAME );
		
		SecurityConfiguration answer = null;
		
		if( null != bundleData ) {
			if( bundleData.contains( "identifier" ) ) {
				answer = new SecurityConfiguration( bundleData, configurationService );
				return answer;
			}
		}
		String identifer = identifierProvider.getIdentifier();
		log.debug( identifer, "identifer" ); 
		
		answer = new SecurityConfiguration( identifer, configurationService ); 
		answer.save(); // ensure we persist the newly created 'identifer' 
		
		return answer;
	
	}
	
	private void save() {
		
		if( null == _configurationService ) {
			log.debug( "null == _configurationService" );
			return;
		}
		
		JsonObject bundleData = this.toJsonObject();
		_configurationService.setBundle( SimpleSecurityAdapter.BUNDLE_NAME, bundleData);
		_configurationService.save_bundles();
		
	}
	
	private void addSubject( String subjectIdentifier, String subjectPassword, String subjectLabel ) {

		Subject client = new Subject( subjectIdentifier, _identifier, subjectPassword, subjectLabel );
		_clients.put( subjectIdentifier, client);
		
		Subject server = new Subject( _identifier, subjectIdentifier, subjectPassword, subjectLabel );
		_servers.put( subjectIdentifier, server );
		
		this.save();
		
	}
	
	private void removeSubject( String subjectIdentifier ) {
	
		_clients.remove( subjectIdentifier );
		_servers.remove( subjectIdentifier );

		this.save();
	}
	
	public JsonObject toJsonObject() {
		
		JsonObject answer = new JsonObject();
		answer.put( "identifier", _identifier );

		JsonArray subjects = new JsonArray();
		
		for( Subject client : this.getClients() ) {

			JsonObject subjectData = new JsonObject();
			subjectData.put( "identifier", client.getUsername() );
			subjectData.put( "password", client.getPassword() );
			subjectData.put( "label", client.getLabel() );

			subjects.add( subjectData );
		}

		answer.put( "subjects" , subjects);
		
		return answer;
	}

	////////////////////////////////////////////////////////////////////////////
	// ClientSecurityConfiguration

	public void addServer(Subject server) {
		
		String subjectIdentifier = server.getRealm();
		String subjectPassword = server.getPassword();
		String subjectLabel = server.getLabel();

		this.addSubject( subjectIdentifier, subjectPassword, subjectLabel);
		
	}

	////////////////////////////////////////////////////////////////////////////
	// ClientSecurityConfiguration::read

	public String getUsername() {
		return _identifier;
	}
	
	// can return null
	public Subject getServer( String realm ) {
		return _servers.get( realm );		
	}

	
	////////////////////////////////////////////////////////////////////////////
	// ServerSecurityConfiguration

	////////////////////////////////////////////////////////////////////////////
	// ServerSecurityConfiguration::create

	public void addClient(Subject client) {
		
		
		String subjectIdentifier = client.getUsername();
		String subjectPassword = client.getPassword();
		String subjectLabel = client.getLabel();
		
		this.addSubject( subjectIdentifier, subjectPassword, subjectLabel);
	}

	
	////////////////////////////////////////////////////////////////////////////
	// ServerSecurityConfiguration::read 

	public String getRealm() {
		return _identifier;		
	}

	
	
	// can return null
	public Subject getClient( String clientUsername) {
		return _clients.get( clientUsername );
	}

	public boolean hasClient( String clientUsername) {
		if( _clients.containsKey( clientUsername ) ) {
			return true;
		}
		return false;
	}
	
    public Collection<Subject> getClients() {
    	
    	Collection<Subject> answer = _clients.values();
    	
    	return answer;
    }
    
	////////////////////////////////////////////////////////////////////////////
	// ServerSecurityConfiguration::delete 
	public void removeClient( String clientUsername) {
		
		this.removeSubject( clientUsername );
		
	}
	
}
