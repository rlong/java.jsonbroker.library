// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json.input;

import java.io.InputStream;

import jsonbroker.library.common.auxiliary.StreamUtilities;

public class JsonStreamInput implements JsonInput {
	
	private byte _currentByte;
	private int _nextByte;
		
	InputStream _inputStream;
	
	int _line;
	int _offset;

	////////////////////////////////////////////////////////////////////////////
	//
	private MutableDataPool _mutableDataPool;
	
	@Override
	public MutableDataPool getMutableDataPool() {
		if( null == _mutableDataPool ) {
			_mutableDataPool = new MutableDataPool();
		}
		return _mutableDataPool;
	}
	

	
	public JsonStreamInput( InputStream inputStream ) {
		_inputStream = inputStream;
		
		_currentByte = (byte)StreamUtilities.readByte( inputStream, this);
		_nextByte = StreamUtilities.readByte( inputStream, this);
		
	}
	
	@Override
	public byte currentByte() {
		
		return _currentByte;
	}

	@Override
	public boolean hasNextByte() {
		
		if( -1 == _nextByte ) {
			return false;
		}
		return true;
	}

	@Override
	public byte nextByte() {
		_currentByte = (byte)_nextByte;
		_nextByte = StreamUtilities.readByte( _inputStream,this);
		
		return _currentByte;
		
	}
	
	public String getPositionInformation() {
		
		return _line + ":" + _offset;

	}



}
