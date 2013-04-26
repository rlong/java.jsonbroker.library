// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jsonbroker.library.common.auxiliary.MutableData;
import jsonbroker.library.common.auxiliary.StreamUtilities;
import jsonbroker.library.common.auxiliary.StringHelper;

public class BluetoothChannelHelper {

	
	// can return null
	public static String readLine(InputStream inputStream, Object caller ) {
		
		
		MutableData data = new MutableData();
		
		int b = 0;

		b = StreamUtilities.readByte(inputStream, caller);
		while( '\n' != b ) {
			if( -1 == b ) {
				return null;
			}
			data.append( (byte)b );
			b = StreamUtilities.readByte(inputStream, caller);
		}

		return data.getUtf8String( 0, data.getCount() );
		
	}	
	
	public static void write( OutputStream outputStream, String line ) {
		
		byte[] utfBytes = StringHelper.toUtfBytes( line  );
		try {
			outputStream.write( utfBytes );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void writeLine( OutputStream outputStream, String line ) {
		
		write( outputStream, line + '\n' );
		
	}
	
}
