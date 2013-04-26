// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.bluetooth;

public interface BluetoothChannel {

	public void close();
	public String readLine();
	public void write( String line );
	public void writeLine( String line );
	
}
