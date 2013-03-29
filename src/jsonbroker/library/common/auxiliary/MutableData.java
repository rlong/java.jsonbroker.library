// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.auxiliary;

import java.io.IOException;
import java.io.InputStream;

import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.log.Log;




public class MutableData extends Data {
	
	
	private static Log log = Log.getLog(MutableData.class);


	private byte[] _readBridgingBuffer = new byte[1024];
	
	private byte[] getReadBridgingBuffer() {
		if( null == _readBridgingBuffer ) { 
			
			_readBridgingBuffer = new byte[1024];
		}
		return _readBridgingBuffer;
	}

	
	public MutableData() { 
		super();
	}
	public MutableData( int capacity ) {
		super( capacity );
	}

	public MutableData( byte[] bytes) {
		super(bytes);
	}

	public void append( InputStream source, int length ) {
		
		int amountToRead = length;
		
		byte[] readBridgingBuffer = getReadBridgingBuffer();
		
		while( amountToRead > 0 ) {
			
			int blockSize = readBridgingBuffer.length;
			if( blockSize > amountToRead ) {
				blockSize = amountToRead;
			}
			
			try {
				
				blockSize = source.read( readBridgingBuffer, 0, blockSize );
				
			} catch (IOException e) {
				throw new BaseException( this, e);
			}
			_buffer.write( readBridgingBuffer, 0, blockSize );
			
			amountToRead -= blockSize;
			
		}

	}
	
	public void append(byte[] buffer) {
		
		this.append( buffer, 0, buffer.length);
		
		
	}
	
	public void append(byte[] buffer, int offset, int count) {
		
		_buffer.write( buffer, offset, count);
		
	}

	
	public void append(byte b ) {
		_buffer.write( b );
	}

	public void clear() { 
		_buffer.reset();
	}
	
	
}


