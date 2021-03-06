// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.broker;


/**
 * 
 * @deprecated use jsonbroker.library.broker.BrokerMessageBuilder
 *
 */
public class BrokerMessageBuilder {

	public static BrokerMessage buildRequest( String serviceName, String methodName ) {

		BrokerMessage answer = new BrokerMessage();
		
		answer.setMessageType( BrokerMessageType.REQUEST );
		answer.setServiceName( serviceName );
		answer.setMethodName( methodName );

		return answer;
	}

}
