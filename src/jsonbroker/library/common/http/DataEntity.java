// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.http;

import java.io.InputStream;
import java.io.OutputStream;

import jsonbroker.library.common.auxiliary.Data;
import jsonbroker.library.common.auxiliary.InputStreamHelper;
import jsonbroker.library.common.security.SecurityUtilities;

public class DataEntity implements Entity{
	
	////////////////////////////////////////////////////////////////////////////
	Data _data;

	public Data getData() {
		return _data;
	}
	
    ////////////////////////////////////////////////////////////////////////////
    // streamContent
    private InputStream _streamContent;

	
	////////////////////////////////////////////////////////////////////////////

	public DataEntity( Data delegate ) {
		_data = delegate;

	}

	@Override
	public InputStream getContent() {
		
		if( null != _streamContent ) { 
			InputStreamHelper.close( _streamContent, false, this);
		}
		_streamContent = _data.toInputStream();
		
	    return _streamContent;

	}

	@Override
	public long getContentLength() {
		return _data.getCount();
	}

	@Override
	public String md5() {
		return SecurityUtilities.md5HashOfData( _data );
	}

	@Override
	public void teardownForCaller(boolean swallowErrors, Object caller) {
		
		if( null != _streamContent ) {
			InputStreamHelper.close(_streamContent, swallowErrors, caller);
			_streamContent = null;
		}
	}

	@Override
	public void writeTo(OutputStream destination, long offset, long length) {
		
		InputStream content = this.getContent();		
		InputStreamHelper.skip( offset, content, this);		
		InputStreamHelper.write( content, length, destination);		
	}
	

}
