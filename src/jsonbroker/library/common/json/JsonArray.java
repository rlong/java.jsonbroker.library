// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

import jsonbroker.library.common.auxiliary.Data;
import jsonbroker.library.common.auxiliary.MutableData;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.json.handlers.JsonArrayHandler;
import jsonbroker.library.common.json.input.JsonDataInput;
import jsonbroker.library.common.json.input.JsonInputHelper;
import jsonbroker.library.common.json.output.JsonStringOutput;




public class JsonArray implements Serializable {

	private static final JsonArrayHandler JSON_ARRAY_HANDLER = JsonArrayHandler.getInstance();

	
	////////////////////////////////////////////////////////////////////////////
	ArrayList<Object> _values;

	/**
	 * 
	 */
	private static final long serialVersionUID = -8701051979229796899L;

	public JsonArray() {
		_values = new ArrayList<Object>();
	}
	
	public JsonArray( int capacity ) {
		_values = new ArrayList<Object>(capacity);
		
	}
	
	public JsonArray(Object ... values) {
		
		_values = new ArrayList<Object>(values.length);
		
		for( int i = 0, count = values.length; i < count; i++ ) {
			_values.add( values[i]);
		}
	}
	
	/**
	 * java version
	 * @return
	 */
	public int size(){
		return _values.size();
	}
	
	/**
	 * javascript version
	 * @return
	 */
	public int length() {
		return _values.size();
	}
	

	public void add( Integer value ) {
		_values.add( value);
	}

	public void add( JsonArray value ) {
		_values.add( value);
	}

	public void add( JsonObject value ) {
		_values.add( value);
	}


	public void add( Object value ) {
		_values.add( value);
	}

	public void add( String value ) {
		_values.add( value);
	}


	public int getInteger( int index ) {
		
		Object blob = _values.get(index);
		
		if( !(blob instanceof Number) ) {
			String technicalError = String.format( "!(blob instanceof Number); index = %d; blob.getClass().getName() = %s", index, blob.getClass().getName() );
			throw new BaseException(JsonArray.class, technicalError);			
		}
		
		Number number = (Number)blob;
		return number.intValue();
	}
	
	
	public Integer getIntegerObject( int index ) {
		
		Object blob = _values.get(index);
		
		if( (blob instanceof Integer) ) {
			return (Integer)blob;
		}

		if( !(blob instanceof Number) ) {
			String technicalError = String.format( "!(blob instanceof Number); index = %d; blob.getClass().getName() = %s", index, blob.getClass().getName() );
			throw new BaseException(this, technicalError);			
		}
		
		Number number = (Number)blob;
		return number.intValue();
	}

	public Integer getIntegerObject( int index, Integer defaultValue ) {
		
		Object blob = _values.get(index);
		
		if( null == blob ) {
			return defaultValue;
		}
		
		if( (blob instanceof Integer) ) {
			return (Integer)blob;
		}

		if( !(blob instanceof Number) ) {
			String technicalError = String.format( "!(blob instanceof Number); index = %d; blob.getClass().getName() = %s", index, blob.getClass().getName() );
			throw new BaseException(this, technicalError);			
		}
		
		Number number = (Number)blob;
		return number.intValue();
	}


	public JsonArray getJsonArray( int index ) {
		
		Object blob = _values.get(index);
		
		if( null == blob ) {
			String technicalError = String.format( "null == blob; index = %d", index );
			throw new BaseException( this, technicalError );
			
		}
		
		if( !(blob instanceof JsonArray) ) {
			
			String technicalError = String.format( "!(blob instanceof JSONArray); index = %d; blob.getClass().getName() = %s", index, blob.getClass().getName() );
			throw new BaseException( this, technicalError );
		}
		
		return (JsonArray)blob;
		
	}
	
	

	public JsonObject getJsonObject( int index ) {
		
		Object blob = _values.get(index);
		
		if( null == blob ) {
			String technicalError = String.format( "null == blob; index = %d", index );
			throw new BaseException( this, technicalError );
			
		}
		
		if( !(blob instanceof JsonObject) ) {
			
			String technicalError = String.format( "!(blob instanceof JSONObject); index = %d; blob.getClass().getName() = %s", index, blob.getClass().getName() );
			throw new BaseException( this, technicalError );
		}
		
		return (JsonObject)blob;
		
	}
	
	public long getLong( int index ) {
		
		Object blob = _values.get(index);
		
		if( !(blob instanceof Number) ) {
			String technicalError = String.format( "!(blob instanceof Number); index = %d; blob.getClass().getName() = %s", index, blob.getClass().getName() );
			throw new BaseException(JsonArray.class, technicalError);			
		}
		
		Number number = (Number)blob;
		return number.longValue();
	}


	public Object getObject( int index ) {
		
		Object blob = _values.get(index);
		
		if( null == blob ) {
			String technicalError = String.format( "null == blob; index = %d", index );
			throw new BaseException( this, technicalError );			
		}

		
		return blob;
	}
	
	public Object getObject( int index, Object defaultValue ) {
		
		Object blob = _values.get(index);
		
		if( null == blob ) {
			
			return defaultValue;
		}
		
		return blob;
	}

	
	public String getString( int index ) {
		
		Object blob = _values.get(index);
		
		if( null == blob ) {
			return null;
		}
		
		if( !(blob instanceof String) ) {
			
			String technicalError = String.format( "!(blob instanceof String); index = %d; blob.getClass().getName() = %s", index, blob.getClass().getName() );
			throw new BaseException( this, technicalError );
		}
		
		return (String)blob;

	}
	
	public String getString( int index, String defaultValue ) {
		
		Object blob = _values.get(index);
		
		if( null == blob ) {
			return defaultValue;
		}
		
		if( !(blob instanceof String) ) {
			
			String technicalError = String.format( "!(blob instanceof String); index = %d; blob.getClass().getName() = %s", index, blob.getClass().getName() );
			throw new BaseException( this, technicalError );
		}
		
		return (String)blob;

	}
	
	
	public static JsonArray build( Data data ) {
		
		JsonDataInput input = new JsonDataInput(data);
		
		try{ 
			
			JsonInputHelper.scanToNextToken( input );			
			return JsonArrayHandler.readJsonArray( input );
			
		} catch( BaseException e ) {
			
			e.addContext( "position", input.getPositionInformation() );
			throw e;
		}
		
	}
	
	public static JsonArray build( InputStream inputStream, int length ) {
		
		MutableData data = new MutableData(length);
		data.append( inputStream, length);
		return build( data );
	}

	
	public void set( int index, int value ) {
		_values.set( index, value );
	}
	
	public void set( int index, long value ) {
		_values.set( index, value );
	}


	public void set( int index, String value ) {
		_values.set( index, value );
	}

	
	public String toString(JsonStringOutput jsonWriter) {
		
		JSON_ARRAY_HANDLER.writeValue( this, jsonWriter);
		return jsonWriter.toString();
		
	}
	
	public String toString() {
		JsonStringOutput writer = new JsonStringOutput();
		return this.toString( writer);
	}

	// pre: all the elements of the 'JsonArray' are of type 'String'
	public String[] toStringArray() {
		
		String[] answer = new String[ _values.size() ];
		_values.toArray( answer );
		
		return answer;
		
	}



}
