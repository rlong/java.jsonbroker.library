// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json.output;

import jsonbroker.library.common.json.JsonDocumentHandler;
import jsonbroker.library.common.json.handlers.JsonBooleanHandler;
import jsonbroker.library.common.json.handlers.JsonNullHandler;
import jsonbroker.library.common.json.handlers.JsonNumberHandler;
import jsonbroker.library.common.json.handlers.JsonStringHandler;

public class JsonWriter implements JsonDocumentHandler {
	
	
	JsonOutput _output;
	
	boolean _objectStarted;
	
	public JsonWriter( JsonOutput output ) {
		_output = output;
		_objectStarted = false;
	}
	

	////////////////////////////////////////////////////////////////////////////
	// document
	
	public void onObjectDocumentStart() {
		
		_output.append( '{');
		_objectStarted = true;
		
	}
	
	public void onObjectDocumentEnd() {
		
		_output.append( '}');
		_objectStarted = false;
		
	}
	
	public void onArrayDocumentStart() {
		
		_output.append( '[');
		
	}
	
	public void onArrayDocumentEnd() {
		
		_output.append( ']');
		
	}
	
	////////////////////////////////////////////////////////////////////////////
	// array
	

	@Override
	public void onArrayStart(int index) {
		
		if( 0 != index ) {
			_output.append( ',');
		}

		_output.append( '[');
	}

	@Override
	public void onArrayEnd(int index) {
		_output.append( ']');
		
	}

	@Override
	public void onBoolean(int index, Boolean value) {
		
		if( 0 != index ) {
			_output.append( ',');
		}
		
		JsonBooleanHandler.writeBoolean( value, _output);
		
	}

	@Override
	public void onNull(int index) {
		
		if( 0 != index ) {
			_output.append( ',');
		}
		
		JsonNullHandler.writeNull( _output );
		
	}

	@Override
	public void onNumber(int index, Number value) {
		if( 0 != index ) {
			_output.append( ',');
		}
		
		JsonNumberHandler.writeNumber( value, _output);
		
	}

	@Override
	public void onObjectStart(int index) {
		
		if( 0 != index ) {
			_output.append( ',');
		}
		
		
		_output.append( '{');
		_objectStarted = true;

	}
	
	@Override
	public void onObjectEnd(int index) {
		
		_output.append( '}');
		_objectStarted = false;
		
	}

	@Override
	public void onString(int index, String value) {
		
		if( 0 != index ) {
			_output.append( ',');
		}
		
		JsonStringHandler.writeString( value, _output);
	}

	
	////////////////////////////////////////////////////////////////////////////
	// object

	@Override
	public void onArrayStart(String key) {
		
		if( _objectStarted ) {
			_objectStarted = false;
		} else {
			_output.append( ',');
		}
		
		JsonStringHandler.writeString( key, _output);
		_output.append( ":[");


	}

	@Override
	public void onArrayEnd(String key) {
		_output.append( ']');
		
	}


	@Override
	public void onBoolean(String key, Boolean value) {
		if( _objectStarted ) {
			_objectStarted = false;
		} else {
			_output.append( ',');
		}

		JsonStringHandler.writeString( key, _output);
		_output.append( ':');

		JsonBooleanHandler.writeBoolean( value, _output);

	}

	@Override
	public void onNull(String key) {
		if( _objectStarted ) {
			_objectStarted = false;
		} else {
			_output.append( ',');
		}
		
		JsonStringHandler.writeString( key, _output);
		_output.append( ':');

		JsonNullHandler.writeNull( _output );

	}

	@Override
	public void onNumber(String key, Number value) {
		if( _objectStarted ) {
			_objectStarted = false;
		} else {
			_output.append( ',');
		}
		
		JsonStringHandler.writeString( key, _output);
		_output.append( ':');

		JsonNumberHandler.writeNumber( value, _output);
	}

	@Override
	public void onObjectStart(String key) {
		
		if( _objectStarted ) {
			_objectStarted = false;
		} else {
			_output.append( ',');
		}
		
		JsonStringHandler.writeString( key, _output);
		_output.append( ":{");
		_objectStarted = true;
		
	}

	@Override
	public void onObjectEnd(String key) {

		_output.append( '}');
		_objectStarted = false;

	}


	@Override
	public void onString(String key, String value) {
		
		if( _objectStarted ) {
			_objectStarted = false;
		} else {
			_output.append( ',');
		}
		
		JsonStringHandler.writeString( key, _output);
		_output.append( ':');
		
		JsonStringHandler.writeString( value, _output);

	}
}
