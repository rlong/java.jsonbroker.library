// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.broker.client;

import jsonbroker.library.broker.BrokerMessage;
import jsonbroker.library.broker.Serializer;
import jsonbroker.library.broker.server.Service;
import jsonbroker.library.client.http.Authenticator;
import jsonbroker.library.client.http.HttpDispatcher;
import jsonbroker.library.client.http.HttpRequestAdapter;
import jsonbroker.library.common.auxiliary.Data;
import jsonbroker.library.common.http.DataEntity;
import jsonbroker.library.common.http.Entity;
import jsonbroker.library.common.log.Log;

public class ServiceHttpProxy implements Service {
	
	private static Log log = Log.getLog( ServiceHttpProxy.class );

    ////////////////////////////////////////////////////////////////////////////
    HttpDispatcher _httpDispatcher;
    
    ////////////////////////////////////////////////////////////////////////////
    Authenticator _authenticator = null; // just to be clear about our intent

    public Authenticator getAuthenticator() {
		return _authenticator;
	}

	////////////////////////////////////////////////////////////////////////////    
    BrokerMessageResponseHandler _responseHandler;

    
    public ServiceHttpProxy( HttpDispatcher httpDispatcher, Authenticator authenticator ) {
    	
    	_httpDispatcher = httpDispatcher;
    	_authenticator = authenticator;
    	_responseHandler = new BrokerMessageResponseHandler();
    }
    
    public ServiceHttpProxy( HttpDispatcher httpDispatcher ) {
    	_httpDispatcher = httpDispatcher;
    	_responseHandler = new BrokerMessageResponseHandler();
    }

    public BrokerMessage process( BrokerMessage request ) {
    	
    	
    	Data bodyData = Serializer.serialize( request );
    	if( Log.isDebugEnabled() ) { 
        	//log.debug( bodyData, "bodyData" );
    	}
    	Entity entity = new DataEntity( bodyData);
    	
    	String requestUri;
    	if( null == _authenticator ) {
    		requestUri = "/_dynamic_/open/services";
    	} else {
    		if( _authenticator.authInt() ) {
    			requestUri = "/_dynamic_/auth-int/services";
    		} else {
    			requestUri = "/_dynamic_/auth/services";
    		}
    	}
    	
    	HttpRequestAdapter requestAdapter = new HttpRequestAdapter( requestUri );
    	requestAdapter.setRequestEntity( entity );

		_httpDispatcher.post( requestAdapter, _authenticator, _responseHandler );
		return _responseHandler.getResponse();

    }

   
}
