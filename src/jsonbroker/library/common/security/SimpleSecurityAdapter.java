// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.security;

import jsonbroker.library.common.log.Log;

public class SimpleSecurityAdapter implements SecurityAdapter {

	private static final Log log = Log.getLog(SimpleSecurityAdapter.class);
	
	
	public static final String BUNDLE_NAME = "jsonbroker.SimpleSecurityAdapter";
	
	@Override
	public String getIdentifier() {
		
		String answer = SecurityUtilities.generateNonce();
		log.debug( answer, "answer" ); 

		return answer;
	}
	
	 
}
