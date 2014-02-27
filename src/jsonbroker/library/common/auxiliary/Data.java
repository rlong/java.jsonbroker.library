// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.auxiliary;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.log.Loggable;





public class Data implements Loggable {
	
	
	private static final char[] HEXADECIMAL_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };


	/**
	 * 
	 */
	private static final long serialVersionUID = 5020841024567524224L;
	
	DataBuffer _buffer;
	
	Data() {		
		_buffer = new DataBuffer();
	}
	
	Data( int capacity ) {
		_buffer = new DataBuffer(capacity);	
	}
	
	
	public Data( byte[] bytes, int offset, int length ) {
		_buffer = new DataBuffer(bytes,offset,length);		
	}
	
	public Data( InputStream inputStream, int contentLength ) {
		
		_buffer = new DataBuffer(contentLength);
		
		InputStreamHelper.write(inputStream, contentLength, _buffer);
		
	}
	
	
	public Data( byte[] bytes ) {
		_buffer = new DataBuffer(bytes.length);
		try {
			_buffer.write( bytes );
		} catch (IOException e) {
			throw new BaseException( this, e);
		}
	}
	
	
	public String getUtf8String( int offset, int length ) { 
		
		return ByteHelper.toUtf8String( _buffer.getBytes(), offset, length);
		
	}
	
	public void arraycopy(int sourceOffset, byte[] destination, int destinationOffset, int length) { 
		
		System.arraycopy(_buffer.getBytes(), sourceOffset, destination, destinationOffset, length);
		
	}
	
	public byte getByte( int offset ) {
		
		return _buffer.getBytes()[offset];
		
	}
	
	// gets the underlying buffer, changes to the returned value will be reflected in the Data object
	public byte[] getBytes() {
		return _buffer.getBytes();
	}
	
	public int getCount() {
		return _buffer.getCount();
	}

	public void writeTo( OutputStream destination ) {
		try {
			destination.write( _buffer.getBytes(), 0, _buffer.getCount());
		} catch (IOException e) {
			throw new BaseException( this , e );
		}
	}

	
	public InputStream toInputStream() {
		
		InputStream answer = new ByteArrayInputStream( _buffer.getBytes(), 0, _buffer.getCount() );
		return answer;
		
	}

	
	private String getDataDebugLine( int offset) {
		
		int count = 16; 
		int upperBound = offset+16;
		
		if( upperBound > getCount() ) {
			upperBound = getCount();
			count = getCount() - offset;			
		}
		
		StringBuilder answer = new StringBuilder(80);
		
		for( int i = offset; i < upperBound; i++ ) {
			
			byte b = getByte( i );
						
			answer.append( HEXADECIMAL_DIGITS[(b >> 4) & 0xF]); // shift the offset to the right
			answer.append( HEXADECIMAL_DIGITS[b & 0xF]); // mask off the high bits

			if (i > 0) {
				if (0 == ((i + 1) % 4)) {
					answer.append(' ');
				}
			}
		}
		
		for (int i = count; i < 16; i++) {
			answer.append( "  ");
			
			if (i > 0) {
				if (0 == ((i + 1) % 4)) {
					answer.append(' ');
				}
			}
		}
		
		// ascii
		
		
		for( int i = offset; i < upperBound; i++ ) {
			
			byte b = getByte( i );
			
			
			if( '!' <= b && '~' >= b ) {
				answer.append( (char)b);
			} else { 
				answer.append( '.');
			}
			
		}
		
		return answer.toString();
		
	}

	@Override
	public String[] getLogMessages() {
		
		ArrayList<String> arrayList = new ArrayList<String>();
		
        int length = getCount();
        
        arrayList.add( "length = " + length);
        
        for(int offset = 0; offset < length; offset += 16) {
        	
        	String message = getDataDebugLine( offset );
        	arrayList.add( message );
        }

        String[] answer = new String[arrayList.size()];
        arrayList.toArray(answer);
		return answer;
	}
	
	
	
	
}
