// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.broker.client;

import java.util.HashMap;

import jsonbroker.library.client.http.HttpResponseHandler;
import jsonbroker.library.common.auxiliary.Data;
import jsonbroker.library.broker.BrokerMessage;
import jsonbroker.library.broker.BrokerMessageType;
import jsonbroker.library.broker.FaultSerializer;
import jsonbroker.library.broker.Serializer;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.http.DataEntity;
import jsonbroker.library.common.http.Entity;
import jsonbroker.library.common.json.JsonObject;
import jsonbroker.library.common.log.Log;

public class BrokerMessageResponseHandler implements HttpResponseHandler {
	
	private static Log log = Log.getLog( BrokerMessageResponseHandler.class );

	Data _responseData;
	

	////////////////////////////////////////////////////////////////////////////
	public BrokerMessageResponseHandler() {
		
	}
	
	
	private static Data toData( Entity responseEntity ) {
		
		if( responseEntity instanceof DataEntity ) {
			DataEntity dataEntity = (DataEntity)responseEntity;
			return dataEntity.getData();
		}

		Data answer = new Data( responseEntity.getContent(), (int)responseEntity.getContentLength());
		return answer;
		
	}

	@Override
	public void handleResponseEntity(HashMap<String, String> headers, Entity responseEntity) {
		
		_responseData = toData( responseEntity);

	}

	BrokerMessage getResponse() {
		
		BrokerMessage answer = Serializer.deserialize( _responseData );
		
		if( Log.isDebugEnabled() ) { 
			// log.debug( answer.toJsonArray().toString(), "answer.toJsonArray().toString()" );
		}
		
		if( BrokerMessageType.FAULT == answer.getMessageType() ) {
			
			JsonObject associativeParamaters = answer.getAssociativeParamaters();
			BaseException e = FaultSerializer.toBaseException( associativeParamaters );
			throw e;		
		}
		
		return answer;

	}



}
