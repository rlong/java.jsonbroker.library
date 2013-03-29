// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.auxiliary;

import java.io.ByteArrayOutputStream;

class DataBuffer extends ByteArrayOutputStream {

	DataBuffer() { 
		super();
	}
	
	DataBuffer(int capacity) { 
		super( capacity);
		
	}
	
	
	public DataBuffer(byte[] bytes, int offset, int length) {
		super( length );
		write( bytes, offset, length);
	}

	byte[] getBytes() {
		return buf;
	}
	
	int getCount() { 
		return count;
	}

}
