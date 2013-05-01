// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.channel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jsonbroker.library.common.auxiliary.InputStreamHelper;
import jsonbroker.library.common.auxiliary.MutableData;
import jsonbroker.library.common.auxiliary.StringHelper;

public class ChannelHelper {

	
	// can return null
	public static String readLine(InputStream inputStream, Object caller ) {
		
		
		MutableData data = new MutableData();
		
		int b = 0;

		b = InputStreamHelper.readByte(inputStream, caller);
		while( '\n' != b ) {
			if( -1 == b ) {
				return null;
			}
			data.append( (byte)b );
			b = InputStreamHelper.readByte(inputStream, caller);
		}

		return data.getUtf8String( 0, data.getCount() );
		
	}	

	public static void write( byte[] bytes, OutputStream outputStream ) {
		
		try {
			outputStream.write( bytes );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void write( String line, OutputStream outputStream ) {
		
		byte[] utfBytes = StringHelper.toUtfBytes( line  );
		write( utfBytes, outputStream );
	}
	
	

	public static void writeLine( String line, OutputStream outputStream ) {
		
		write( line + '\n', outputStream );
		
	}
	
}
