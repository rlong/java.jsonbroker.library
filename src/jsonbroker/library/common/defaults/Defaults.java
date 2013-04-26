// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.defaults;

import jsonbroker.library.common.json.JsonObject;
import jsonbroker.library.common.json.JsonObjectHelper;
import jsonbroker.library.common.log.Log;

public class Defaults {
	
	private static final Log log = Log.getLog(Defaults.class );
	
    ///////////////////////////////////////////////////////////////////////
    // environment
    private JsonObject _environment;

    
    private Defaults(JsonObject environment) {
    	_environment = environment;
    }
    
    
	public static Defaults getDefaults( String scope ) {
		
		
		// environment ...
		{
	        // vvv http://stackoverflow.com/questions/2821043/allowed-characters-in-linux-environment-variable-names

			String environmentName = scope.replace('.', '_');

	        // ^^^ http://stackoverflow.com/questions/2821043/allowed-characters-in-linux-environment-variable-names

	        String environmentValue = System.getenv( environmentName );
	        if( null != environmentValue ) {
	        	
	        	log.debug( environmentValue, "environmentValue" );
	        	JsonObject environment = JsonObjectHelper.buildFromString(environmentValue);
	        	return new Defaults(environment);
	        }
		}
		
        // empty ...
        JsonObject environment = new JsonObject(); 
        return new Defaults(environment);

	}

    


    public boolean getBoolean(String name, boolean defaultValue) {
    	
    	return _environment.getBoolean( name, defaultValue);
    }

    public int getInt(String name, int defaultValue) {
    	return _environment.getInt( name, defaultValue);
    }

}
