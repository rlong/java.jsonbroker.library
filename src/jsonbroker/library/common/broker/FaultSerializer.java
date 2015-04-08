// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.broker;

import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.json.JsonObject;


/**
 * 
 * @deprecated use jsonbroker.library.broker.FaultSerializer
 *
 */
public class FaultSerializer {
	
	private static JsonObject toJsonObject( BaseException baseException ) {

		return jsonbroker.library.broker.FaultSerializer.toJsonObject( baseException );
		
	}
	
	public static JsonObject toJsonObject( Throwable t  ) {
		
		return jsonbroker.library.broker.FaultSerializer.toJsonObject( t );
		
	}
	
	public static BaseException toBaseException( JsonObject jsonObject ) {

		return jsonbroker.library.broker.FaultSerializer.toBaseException( jsonObject );

	}

}
