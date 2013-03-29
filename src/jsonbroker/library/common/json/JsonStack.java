// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json;

import java.util.Stack;

public final class JsonStack {


	Stack<Object> _stack;
	
	////////////////////////////////////////////////////////////////////////////
	//
	JsonObject _currentObject;
	
	public JsonObject getCurrentObject() {
		return _currentObject;
	}


	////////////////////////////////////////////////////////////////////////////
	//
	JsonArray _currentArray;
	
	public JsonArray getCurrentArray() {
		return _currentArray;
	}

	////////////////////////////////////////////////////////////////////////////
	//
	JsonStack() {
		_stack = new Stack<Object>();
		_currentObject = null;
		_currentArray = null;
	}
	
	
	public Object pop() {
		
		
		Object popped = _stack.pop();

		_currentObject = null;
		_currentArray = null;

		if( 0 != _stack.size() ) {
			
			Object current = _stack.peek();
			if( current instanceof JsonObject ) {
				_currentObject = (JsonObject)current;
			} else {
				_currentArray = (JsonArray)current;
			}
		}
		
		return popped;

	}
	
	public void push( JsonObject top ) {
		
		_stack.push( top );
		_currentObject = top;
		_currentArray = null;		
	}
	
	public void push( JsonArray top ) {
		
		_stack.push( top );
		_currentObject = null;
		_currentArray = top;		
	}




}
