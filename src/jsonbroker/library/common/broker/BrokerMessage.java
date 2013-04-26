// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.broker;

import jsonbroker.library.common.json.JsonArray;
import jsonbroker.library.common.json.JsonObject;

public class BrokerMessage {
	
	
    ////////////////////////////////////////////////////////////////////////////
	private BrokerMessageType _messageType = BrokerMessageType.REQUEST;
	
    public BrokerMessageType getMessageType() {
		return _messageType;
	}

    public void setMessageType(BrokerMessageType messageType) {
		_messageType = messageType;
	}
    
    ////////////////////////////////////////////////////////////////////////////
    private JsonObject _metaData;
    
    
	public JsonObject getMetaData() {
		return _metaData;
	}

    
	////////////////////////////////////////////////////////////////////////////
	private String _serviceName;

	public String getServiceName() {
		return _serviceName;
	}

	public void setServiceName(String service) {
		_serviceName = service;
	}
	

	///////////////////////////////////////////////////////////////////////
	private String _methodName;
	
	public String getMethodName() {
		return _methodName;
	}
	
	public void setMethodName(String methodName) {
		_methodName = methodName;
	}

	///////////////////////////////////////////////////////////////////////
	private JsonObject _associativeParamaters;


	public JsonObject getAssociativeParamaters() {
		return _associativeParamaters;
	}

	public void setAssociativeParamaters(JsonObject associativeParamaters) {
		_associativeParamaters = associativeParamaters;
	}

	///////////////////////////////////////////////////////////////////////
	private JsonArray _orderedParamaters;

	///////////////////////////////////////////////////////////////////////

	/**
	 * @return
	 */
	public JsonArray getOrderedParamaters() {
		return _orderedParamaters;
	}

	/**
	 * @return
	 */
	public void setOrderedParamaters(JsonArray paramaters) {
		_orderedParamaters = paramaters;
	}

	///////////////////////////////////////////////////////////////////////
	public BrokerMessage() {
		_metaData = new JsonObject();
		_associativeParamaters = new JsonObject();
		_orderedParamaters = new JsonArray();
	}
	
	
	///////////////////////////////////////////////////////////////////////
	public BrokerMessage(JsonArray values) {
		
		String messageTypeIdentifer = values.getString(0 );
		_messageType = BrokerMessageType.lookup( messageTypeIdentifer );
		_metaData = values.getJsonObject( 1 );
		_serviceName = values.getString( 2 );
//		int majorVersion = values.getInteger( 3 );
//		int minorVersion = values.getInteger( 4 );
		_methodName = values.getString( 5 );
		_associativeParamaters = values.getJsonObject( 6 );
		if( 7 < values.size() ) {
			_orderedParamaters = values.getJsonArray( 7 );
		} else {
			_orderedParamaters = new JsonArray(0);	
		}
	}	
	
	public static BrokerMessage buildRequest( String serviceName, String methodName ) {

		BrokerMessage answer = new BrokerMessage();
		
		answer._messageType = BrokerMessageType.REQUEST;
		answer._serviceName = serviceName;
		answer._methodName = methodName;

		return answer;
	}
	
	
	public static BrokerMessage buildMetaRequest( String serviceName, String methodName ) {

		BrokerMessage answer = new BrokerMessage();
		
		answer._messageType = BrokerMessageType.META_REQUEST;
		answer._serviceName = serviceName;
		answer._methodName = methodName;

		return answer;
	}
	
	public static BrokerMessage buildFault( BrokerMessage request, Throwable t ) {

		BrokerMessage answer = new BrokerMessage();
		
		answer._messageType = BrokerMessageType.FAULT;
		answer._metaData = request.getMetaData();
		answer._serviceName = request._serviceName;
		answer._methodName = request._methodName;
		answer._associativeParamaters = FaultSerializer.toJsonObject( t );
		answer._orderedParamaters = new JsonArray(0);
		
		return answer;			

	}

	public static BrokerMessage buildMetaResponse( BrokerMessage request) {
		
		BrokerMessage answer = new BrokerMessage();
		
		answer._messageType = BrokerMessageType.META_RESPONSE;
		answer._metaData = request.getMetaData();
		answer._serviceName = request._serviceName;
		answer._methodName = request._methodName;
		answer._associativeParamaters = new JsonObject();
		answer._orderedParamaters = new JsonArray(0);
		
		return answer;			
	}

	public static BrokerMessage buildResponse( BrokerMessage request) {
				
		BrokerMessage answer = new BrokerMessage();
		
		answer._messageType = BrokerMessageType.RESPONSE;
		answer._metaData = request.getMetaData();
		answer._serviceName = request._serviceName;
		answer._methodName = request._methodName;
		answer._associativeParamaters = new JsonObject();
		answer._orderedParamaters = new JsonArray(0);
		
		return answer;			
	}
	
	public JsonArray toJsonArray() {
		
		JsonArray answer = new JsonArray(5);
		
		answer.add( _messageType.getIdentifier() );
		answer.add( _metaData );
		answer.add( _serviceName );
		answer.add( 1 );
		answer.add( 0 );
		answer.add( _methodName );
		answer.add( _associativeParamaters );
		answer.add( _orderedParamaters );
		
		return answer;
		
	}

	public void addParameter( Integer parameter ) {
		_orderedParamaters.add( parameter );
	}

	public void addParameter( JsonObject parameter ) {
		_orderedParamaters.add( parameter );
	}
	
	public void addParameter( JsonArray parameter ) {
		_orderedParamaters.add( parameter );
	}

	public void addParameter( Object parameter ) {
		_orderedParamaters.add( parameter );
	}


	public void addParameter(String parameter) {
		_orderedParamaters.add( parameter );
	}
	
	public void setResponseType( String type ) {
		_metaData.put( "responseType", type);
	}

}
