// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.broker;

import jsonbroker.library.common.exception.BaseException;

public class BrokerMessageType {
	
	////////////////////////////////////////////////////////////////////////////
	protected String _identifier;
	
	public String getIdentifier() {
		return _identifier;
	}

	public static final BrokerMessageType FAULT = new BrokerMessageType("fault");
	public static final BrokerMessageType NOTIFICATION = new BrokerMessageType("notification");
	public static final BrokerMessageType ONEWAY = new BrokerMessageType("oneway");
	public static final BrokerMessageType META_REQUEST = new BrokerMessageType("meta-request");
	public static final BrokerMessageType META_RESPONSE = new BrokerMessageType("meta-response");
	public static final BrokerMessageType REQUEST = new BrokerMessageType("request");
	public static final BrokerMessageType RESPONSE = new BrokerMessageType("response");
	
	protected BrokerMessageType(String identifier) {
		_identifier = identifier;
	}
	
	public static BrokerMessageType lookup( String identifier) {

		if( FAULT._identifier.equals(identifier) ) {
			return FAULT;
		}
		
		if( NOTIFICATION._identifier.equals( identifier) ) {
			return NOTIFICATION;
		}

		if( ONEWAY._identifier.equals(identifier) ) {
			return ONEWAY;
		}
		
		if( META_REQUEST._identifier.equals(identifier) ) {
			return META_REQUEST;
		}
		
		if( META_RESPONSE._identifier.equals(identifier) ) {
			return META_RESPONSE;
		}
		
		if( REQUEST._identifier.equals(identifier) ) {
			return REQUEST;
		}


		if( RESPONSE._identifier.equals(identifier) ) {
			return RESPONSE;
		}

		String technicalError = String.format("unexpected identifier; identifier = '%s'", identifier);
		throw new BaseException(BrokerMessageType.class, technicalError);
		
	}
	

}
