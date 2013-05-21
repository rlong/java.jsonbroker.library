// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.http;

import java.io.InputStream;
import java.io.OutputStream;

public interface Entity  {
	

	public InputStream getContent();

	public long getContentLength();

	// can return null. depends on the underlying object and how it was built
	public String md5();
	
	public void teardownForCaller(boolean swallowErrors, Object caller );
	
	public void writeTo( OutputStream destination, long offset, long length );

}
