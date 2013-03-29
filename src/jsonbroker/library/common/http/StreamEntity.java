// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.http;

import java.io.InputStream;

import jsonbroker.library.common.exception.BaseException;

public class StreamEntity implements Entity {
	
	////////////////////////////////////////////////////////////////////////////
	private long _contentLength;
	
	public long getContentLength() {
		return _contentLength;
	}
	
	////////////////////////////////////////////////////////////////////////////
	InputStream _content;
	
	@Override
	public InputStream getContent() {
		return _content;
	}
	
	////////////////////////////////////////////////////////////////////////////
	// intended for file & network streams
	public StreamEntity( long contentLength, InputStream content ) {
		_contentLength = contentLength;
		_content = content;
	}
	
	
	public String md5() {
		throw new BaseException( this, "unsupported" );
	}


}
