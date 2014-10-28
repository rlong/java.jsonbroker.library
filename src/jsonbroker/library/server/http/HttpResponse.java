// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.http;

import java.util.HashMap;

import jsonbroker.library.common.http.Entity;
import jsonbroker.library.common.http.headers.request.Range;




public class HttpResponse {
	
	////////////////////////////////////////////////////////////////////////////
	//
	ConnectionDelegate _connectionDelegate;
	
	public void setConnectionDelegate(ConnectionDelegate connectionDelegate) {
		_connectionDelegate = connectionDelegate;
	}

	////////////////////////////////////////////////////////////////////////////
	//
	private int _status;
	
	
	public int getStatus() {
		return _status;
	}

	////////////////////////////////////////////////////////////////////////////
    //
	private HashMap<String,String> _headers;
	
	
	public HashMap<String, String> getHeaders() {
		return _headers;
	}

	
	////////////////////////////////////////////////////////////////////////////
	Range _range; 

	public Range getRange() {
		return _range;
	}


	public void setRange(Range range) {
		_range = range;
	}

	
	////////////////////////////////////////////////////////////////////////////
	private Entity _entity;
	

	public Entity getEntity() {
		return _entity;
	}

	
	public void setEntity(Entity entity) {
		_entity = entity;
	}

	////////////////////////////////////////////////////////////////////////////
	public HttpResponse(int status) {
		
		_status = status;
		_headers = new HashMap<String, String>();
		
	}
	
    public HttpResponse(int status, Entity entity) {
    	
    	_status = status;
    	_headers = new HashMap<String, String>();
    	_entity = entity;
    	
    }

    
    public void setContentType( String contentType ) {
    	
    	_headers.put( "Content-Type", contentType);
    	
    }
    
	
    
    public void putHeader( String name, String value ) {
    	_headers.put( name, value);
    }
	
    // can return null
    public String getHeader( String name ) {
    	
    	return _headers.get( name );
    }

    public Long getContentLength() {
    	
    	if( null == _entity ) {
    		return null;    		
    	}
    	
    	long entityContentLength = _entity.getContentLength();
    	
    	if( null == _range ) {
    		return new Long( entityContentLength );
    	}
    	
    	long rangedContentLength = _range.getContentLength(entityContentLength);
    	Long answer = new Long(rangedContentLength);
        return answer;
    }
}
