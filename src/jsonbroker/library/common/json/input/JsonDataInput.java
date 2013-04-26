// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json.input;

import jsonbroker.library.common.auxiliary.Data;
import jsonbroker.library.common.log.Log;


public class JsonDataInput extends JsonInput {

	private static final Log log = Log.getLog(JsonDataInput.class );

	
	Data _data;
	
	int _cursor;
	
	int _line = 1;
	int _offset;
	

	public int getCursor() {
		return _cursor;
	}

	public JsonDataInput( Data data ) {
		
		_data = data;
		_cursor = 0;
		
	}
	
	public boolean hasNextByte() {
		
		if( 1 + _cursor >= _data.getCount()) {
			return false;
		}
		return true;
	}
	
	public byte nextByte() { 
		_cursor++;
		byte answer = _data.getByte( _cursor);
		
		if( '\n' == answer ) {
			_line++;
			_offset = 0;
		} else {
			_offset++;
		}
		return answer;
	}
	
	public byte currentByte() { 
		return _data.getByte( _cursor);
	}
	
	public String getPositionInformation() {
		
		String answer = _line + ":" + _offset;
		log.debug( answer );
		return answer;
	}
	

}
