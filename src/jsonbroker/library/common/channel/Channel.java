// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.channel;

import java.io.InputStream;

public interface Channel {

	public void close(boolean ignoreErrors);
	
	public void flush();
	
	// can return null
	public String readLine();
	
	public void write( byte[] bytes ); 
	public void write( InputStream inputStream );
	public void write( String line );
	public void writeLine( String line );
	
	
}
