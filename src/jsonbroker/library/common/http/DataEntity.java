// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.http;

import java.io.InputStream;

import jsonbroker.library.common.auxiliary.Data;
import jsonbroker.library.common.security.SecurityUtilities;

public class DataEntity implements Entity{
	
	////////////////////////////////////////////////////////////////////////////
	Data _data;

	public Data getData() {
		return _data;
	}
	
	////////////////////////////////////////////////////////////////////////////

	public DataEntity( Data delegate ) {
		_data = delegate;

	}

	@Override
	public InputStream getContent() {
		
		return _data.toInputStream();
	}

	@Override
	public long getContentLength() {
		return _data.getCount();
	}

	@Override
	public String md5() {
		return SecurityUtilities.md5HashOfData( _data );
	}

}
