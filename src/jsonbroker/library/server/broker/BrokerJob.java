// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.broker;

import jsonbroker.library.common.auxiliary.Data;
import jsonbroker.library.common.auxiliary.StringHelper;
import jsonbroker.library.common.broker.BrokerMessage;
import jsonbroker.library.common.broker.BrokerMessageType;
import jsonbroker.library.common.broker.Serializer;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.common.work.Job;

public class BrokerJob implements Job{
	private static final Log log = Log.getLog( BrokerJob.class );

	public static final String JSON_BROKER_SCHEME = "jsonbroker:";
	public static final String JSON_BROKER_URI_ENCODED_SCHEME = "jsonbroker_uri_encoded:";
	
	private Data _jsonRequestData;
	
	////////////////////////////////////////////////////////////////////////////
	private String _jsonRequestString;

	
	////////////////////////////////////////////////////////////////////////////
	private JavascriptCallbackAdapter _callbackAdapter;
	
	
	JavascriptCallbackAdapter getCallbackAdapter() {
		return _callbackAdapter;
	}

	////////////////////////////////////////////////////////////////////////////
	private Service _service;
	
	public void setService(DescribedService servicesRegistery) {
		_service = servicesRegistery;
	}


	////////////////////////////////////////////////////////////////////////////
	// 
	private boolean _isUriEncoded = false;
	
	////////////////////////////////////////////////////////////////////////////
	public BrokerJob(String jsonRequest,boolean isUriEncoded, Service service, JavascriptCallbackAdapter callbackAdapter) {
		_jsonRequestString = jsonRequest;
		_isUriEncoded = isUriEncoded;
		_service = service;
		_callbackAdapter = callbackAdapter;
	}
	
	public BrokerJob(Data jsonRequest,JavascriptCallbackAdapter callbackAdapter) {
		_jsonRequestData = jsonRequest;
		_callbackAdapter = callbackAdapter;
		
	}
	
	public Data getJsonRequest() {
		
		if( null != _jsonRequestData ) {
			return _jsonRequestData;
		}
		
		String jsonString = _jsonRequestString;
		
		
		// strip off 'jsonbroker:'
		jsonString = jsonString.substring( JSON_BROKER_SCHEME.length() );
		
		if( _isUriEncoded ) {
			jsonString = StringHelper.decodeURIComponent( jsonString );
		}
		
		
		byte[] bytes = StringHelper.toUtfBytes( jsonString );		
		
		_jsonRequestData = new Data( bytes);
		
		return _jsonRequestData;
		
	}


	@Override
	public void execute() {
		
		BrokerMessage request = null;
		try {			
			request = Serializer.deserialize( getJsonRequest() );
		}catch( Throwable t ) {
			log.warn(t);
			return;
		}
		
		try {
			
			BrokerMessage response = _service.process( request );
			
			if( BrokerMessageType.ONEWAY == request.getMessageType() ) {
				// no reply 
			} else { 
				_callbackAdapter.onResponse( response );
			}
		} catch( Throwable t ) {
			_callbackAdapter.onFault( request, t);			
		}		
	}

}
