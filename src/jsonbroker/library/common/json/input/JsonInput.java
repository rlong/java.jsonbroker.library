// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json.input;

import jsonbroker.library.common.auxiliary.MutableData;

public abstract class JsonInput {
	
	
	////////////////////////////////////////////////////////////////////////////
	
	MutableData[] _mutableDataPool;
	int _mutableDataPoolIndex;

	////////////////////////////////////////////////////////////////////////////

	protected JsonInput() {
			
		_mutableDataPool = null; // just to explicit about our intent
		_mutableDataPoolIndex = 0; // the next free MutableData
	}

	public abstract boolean hasNextByte();
	public abstract byte nextByte();
	public abstract byte currentByte();
	public abstract String getPositionInformation();

	
	private boolean doesByteBeginToken( byte candidateByte ) {
		
		if( ' ' == candidateByte) {
			return false;
		}
		
		if( '\t' == candidateByte) {
			return false;
		}

		
		if( '\n' == candidateByte) {
			return false;
		}

		if( '\r' == candidateByte) {
			return false;
		}

		if( ',' == candidateByte) {
			return false;
		}

		if( ':' == candidateByte) {
			return false;
		}
		
		return true;
	}

	public byte scanToNextToken() {
		
		byte currentByte = currentByte();
		
		if( doesByteBeginToken(currentByte) ) { 
			return currentByte;
		}
		
		do { 
			currentByte = nextByte();
		} while( !doesByteBeginToken(currentByte) );
		

		return currentByte;

	}

	public MutableData reserveMutableData() {
		
		if( null == _mutableDataPool ) {
			_mutableDataPool = new MutableData[5];
		}
		
		if( _mutableDataPoolIndex >=  _mutableDataPool.length ) {
			
			// revert to disposable MutableData objects
			_mutableDataPoolIndex++;
			return new MutableData();
		}
		
		if( null == _mutableDataPool[_mutableDataPoolIndex] ) {
			_mutableDataPool[_mutableDataPoolIndex] = new MutableData();
		}
		
		MutableData answer = _mutableDataPool[_mutableDataPoolIndex];
		
		_mutableDataPoolIndex++;
		
		return answer;
		
	}
	
	public void releaseMutableData(MutableData freedMutableData) {
		
		
		if( _mutableDataPoolIndex > _mutableDataPool.length ) {
			
			// release of disposable MutableData objects
			_mutableDataPoolIndex--;
			return;
		}
		
		_mutableDataPoolIndex--;
		
		_mutableDataPool[_mutableDataPoolIndex].clear();
		
		return;
		
	}

}
