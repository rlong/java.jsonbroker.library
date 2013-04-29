// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json;

import jsonbroker.library.common.log.Log;




public class JsonBuilder implements JsonDocumentHandler {
	
	private static final Log log = Log.getLog(JsonBuilder.class );

	JsonStack _stack;
	

	////////////////////////////////////////////////////////////////////////////
	//
	JsonObject _objectDocument;
	
	public JsonObject getObjectDocument() {
		return _objectDocument;
	}

	////////////////////////////////////////////////////////////////////////////
	//
	JsonArray _arrayDocument;

	public JsonArray getArrayDocument() {
		return _arrayDocument;
	}

	////////////////////////////////////////////////////////////////////////////
	//
	public JsonBuilder() {
		_stack = new JsonStack();
	}

	
	////////////////////////////////////////////////////////////////////////////
	// document
	
	@Override
	public void onObjectDocumentStart() {
		
		_objectDocument = new JsonObject();
		_stack.push( _objectDocument );
	}
	
	@Override
	public void onObjectDocumentEnd() {
		_stack.pop();
	}
	
	
	@Override
	public void onArrayDocumentStart() {
		
		_arrayDocument = new JsonArray();
		_stack.push( _arrayDocument );
		
	}
	
	@Override
	public void onArrayDocumentEnd() {
		_stack.pop();
		
	}
	

	////////////////////////////////////////////////////////////////////////////
	// array
	

	@Override
	public void onArrayStart(int index) {
		
		log.debug( index, "index");
		
		JsonArray jsonArray = new JsonArray();		
		_stack.getCurrentArray().add( jsonArray );
		_stack.push( jsonArray );
		
	}

	@Override
	public void onArrayEnd(int index) {

		log.debug( index, "index");
		_stack.pop();
		
	}

	@Override
	public void onBoolean(int index, Boolean value) {
		
		_stack.getCurrentArray().add( value);
		
	}

	@Override
	public void onNull(int index) {
		_stack.getCurrentArray().add( (Object)null );
		
	}

	@Override
	public void onNumber(int index, Number value) {
		_stack.getCurrentArray().add( value );
		
	}

	@Override
	public void onObjectStart(int index) {
		
		log.debug( index, "index");
		JsonObject jsonObject = new JsonObject();
		_stack.getCurrentArray().add( jsonObject );
		_stack.push( jsonObject );
	}
	
	@Override
	public void onObjectEnd(int index) {
		
		log.debug( index, "index");
		_stack.pop();
		
	}

	@Override
	public void onString(int index, String value) {
		_stack.getCurrentArray().add( value );
	}

	
	////////////////////////////////////////////////////////////////////////////
	// object

	@Override
	public void onArrayStart(String key) {
		
		log.debug( key, "key" );
		
		JsonArray jsonArray = new JsonArray();
		_stack.getCurrentObject().put( key, jsonArray);
		_stack.push( jsonArray );
		
	}

	@Override
	public void onArrayEnd(String key) {
		log.debug( key, "key" );
		_stack.pop();
	}


	@Override
	public void onBoolean(String key, Boolean value) {
		_stack.getCurrentObject().put( key, value);
	}

	@Override
	public void onNull(String key) {
		_stack.getCurrentObject().put( key, (Object)null );
		
	}

	@Override
	public void onNumber(String key, Number value) {
		_stack.getCurrentObject().put( key, value);
		
	}

	@Override
	public void onObjectStart(String key) {
		
		log.debug( key, "key" );

		
		JsonObject jsonObject = new JsonObject();
		_stack.getCurrentObject().put( key, jsonObject);
		_stack.push( jsonObject );

	}

	@Override
	public void onObjectEnd(String key) {
		log.debug( key, "key" );
		_stack.pop();
	}


	@Override
	public void onString(String key, String value) {
		_stack.getCurrentObject().put( key, value);
		
	}
	

}
