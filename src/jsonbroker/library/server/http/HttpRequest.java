// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.http;

import java.util.HashMap;

import jsonbroker.library.common.http.Entity;
import jsonbroker.library.common.http.headers.request.Range;


public class HttpRequest {
	
	public static final String METHOD_GET = "GET";
	public static final String METHOD_POST = "POST";

	
	////////////////////////////////////////////////////////////////////////////
	private long _created;


	public long getCreated() {
		return _created;
	}

	////////////////////////////////////////////////////////////////////////////
	// 
	String _method;
	
	public String getMethod() {
		return _method;
	}

	public void setMethod(String method) {
		_method = method;
	}

	////////////////////////////////////////////////////////////////////////////
    //
	private String _requestUri;
	

	public String getRequestUri() {
		return _requestUri;
	}

	public void setRequestUri(String requestUri) {
		_requestUri = requestUri;
	}

	////////////////////////////////////////////////////////////////////////////
    //
	private HashMap<String,String> _headers;

	public HashMap<String, String> getHeaders() {
		return _headers;
	}

	////////////////////////////////////////////////////////////////////////////
	//
	private Entity _entity;
	

	public Entity getEntity() {
		return _entity;
	}

	
	public void setEntity(Entity entity) {
		_entity = entity;
	}

    ///////////////////////////////////////////////////////////////////////
    // closeConnectionIndicated
    private Boolean _closeConnectionIndicated;


	////////////////////////////////////////////////////////////////////////////
	Range _range; 
	
    public Range getRange() {
    	
    	if( null != _range ) {
    		return _range; 
    	}
    	
    	String rangeValue = _headers.get( "range" );
    	if( null == rangeValue ) {
    		return null;
    	}
    	
    	_range = Range.buildFromString( rangeValue );
    	return _range;
    }
	

	////////////////////////////////////////////////////////////////////////////
	public HttpRequest()
    {
		_created = System.currentTimeMillis();
		
		_method = HttpRequest.METHOD_GET;
    	_headers = new HashMap<String, String>();
    	    	
    }
    
    
    public void setHttpHeader(String headerName, String headerValue)
    {
    	_headers.put( headerName, headerValue);
    }
    
    public String getHttpHeader(String headerName ) {
    	return _headers.get( headerName );
    }
    
    
    // vvv http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.10
    public boolean isCloseConnectionIndicated() 
    {
        if (null != _closeConnectionIndicated)
        {
            return _closeConnectionIndicated.booleanValue();
        }
        
        String connectionHeader = _headers.get( "connection" );
        
        if( null == connectionHeader ) {
        	
        	_closeConnectionIndicated = Boolean.FALSE;
        	
        } else {
        	
        	if( "close".equals( connectionHeader ) ) {
        		_closeConnectionIndicated = Boolean.TRUE;
        	} else {
        		_closeConnectionIndicated = Boolean.FALSE;
        	}
        }
        return _closeConnectionIndicated.booleanValue();
        
    }
    // ^^^ http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.10

    
	
}
