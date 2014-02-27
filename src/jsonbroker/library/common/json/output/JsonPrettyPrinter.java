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

public class JsonPrettyPrinter implements JsonDocumentHandler {
	
	
	JsonOutput _output;
	
	boolean _objectStarted;
	private int _currentIndent;
	
	
	private static final String INDENT = "    ";
	
	
	public JsonPrettyPrinter( JsonOutput output ) {
		_output = output;
		_objectStarted = false;
	}
	
	
	private void newlineAndIndent() {
		
		_output.append( '\n' );
		
		for( int i = 0; i < _currentIndent; i++ ) {
			_output.append( INDENT );
		}
	}
	

	////////////////////////////////////////////////////////////////////////////
	// document
	
	public void onObjectDocumentStart() {
		
		_output.append( "{");
		_currentIndent++;
		_objectStarted = true;
		
	}
	
	public void onObjectDocumentEnd() {
		
		
		_output.append( "\n}" );
		_currentIndent--;
		_objectStarted = false;
		
	}
	
	public void onArrayDocumentStart() {
		
		_currentIndent++;
		_output.append( "[");
		
	}
	
	public void onArrayDocumentEnd() {
		
		_output.append( "\n]" );
		_currentIndent--;
		
	}
	
	////////////////////////////////////////////////////////////////////////////
	// array
	

	@Override
	public void onArrayStart(int index) {
		
		newlineAndIndent();
		
		if( 0 != index ) {
			_output.append( ",");
		}

		_currentIndent++;

		_output.append( '[');
	}

	@Override
	public void onArrayEnd(int index) {
		
		_currentIndent--;
		
		newlineAndIndent();
		_output.append( ']');
		
	}

	@Override
	public void onBoolean(int index, Boolean value) {
		
		newlineAndIndent();
		
		if( 0 != index ) {
			_output.append( ',');
		}
		
		JsonBooleanHandler.writeBoolean( value, _output);
		
	}

	@Override
	public void onNull(int index) {
		
		newlineAndIndent();
		
		
		if( 0 != index ) {
			_output.append( ',');
		}
		
		JsonNullHandler.writeNull( _output );
		
	}

	@Override
	public void onNumber(int index, Number value) {
		
		newlineAndIndent();
		
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

		
		newlineAndIndent();

		
		_output.append( '{');
		_objectStarted = true;
		_currentIndent++;

	}
	
	@Override
	public void onObjectEnd(int index) {
		
		_currentIndent--;
		newlineAndIndent();
		
		_output.append( '}');
		_objectStarted = false;
		
	}

	@Override
	public void onString(int index, String value) {
		
		if( 0 != index ) {
			_output.append( ',');
		}
		newlineAndIndent();
		
		
		JsonStringHandler.writeString( value, _output);
	}

	
	////////////////////////////////////////////////////////////////////////////
	// object

	@Override
	public void onArrayStart(String key) {
		
		
		newlineAndIndent();
		
		
		if( _objectStarted ) {
			_objectStarted = false;
		} else {
			_output.append( ", ");
		}
		
		JsonStringHandler.writeString( key, _output);
		_output.append( " :[");
		_currentIndent++;


	}

	@Override
	public void onArrayEnd(String key) {
		_currentIndent--;
		newlineAndIndent();
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
		
		newlineAndIndent();
		
		JsonStringHandler.writeString( key, _output);
		_output.append( ": {");
		_objectStarted = true;
		_currentIndent++;
		
	}

	@Override
	public void onObjectEnd(String key) {
		
		_currentIndent--;
		newlineAndIndent();
//		if( _objectStarted ) { 
//		}

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
		
		newlineAndIndent();
		
		JsonStringHandler.writeString( key, _output);
		_output.append( ':');
		
		JsonStringHandler.writeString( value, _output);

	}
}
