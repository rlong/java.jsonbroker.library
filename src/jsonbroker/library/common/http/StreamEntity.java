// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import jsonbroker.library.common.auxiliary.InputStreamHelper;
import jsonbroker.library.common.exception.BaseException;

public class StreamEntity implements Entity {
	
	
	////////////////////////////////////////////////////////////////////////////
	//
	InputStream _content;
	

	////////////////////////////////////////////////////////////////////////////
	//
	private long _contentLength;
	
	public long getContentLength() {
		return _contentLength;
	}

	////////////////////////////////////////////////////////////////////////////
	//
	URL _contentSource;

	////////////////////////////////////////////////////////////////////////////
	//
	private String _mimeType;
	
	
	@Override
	// can return null
	public String getMimeType() {
		return _mimeType;
	}

	
	////////////////////////////////////////////////////////////////////////////
	// intended for file & network streams
	public StreamEntity( InputStream content, long contentLength ) {
		_contentLength = contentLength;
		_content = content;
	}
	
	public StreamEntity( URL contentSource, long contentLength, String mimeType ) {
		
		_contentSource = contentSource;
		_contentLength = contentLength;
		_mimeType = mimeType;
	
	}
	
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////
	
	@Override
	public InputStream getContent() {
		
		
		if( null == _content ) {
			
			try {
				_content = _contentSource.openStream();
			} catch (IOException e) {
				throw new BaseException( this, e );
			}
		}
		
		return _content;
	}

	
	public String md5() {
		throw new BaseException( this, "unsupported" );
	}


	@Override
	public void writeTo(OutputStream destination, long offset, long length) {

		InputStream content = getContent();
		InputStreamHelper.skip( offset, content, this);		
		InputStreamHelper.write( content, length, destination);
		InputStreamHelper.close( content, false, this );		
		_content = null;

	}


}
