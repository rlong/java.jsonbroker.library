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


	public void add( JsonArray value ) {
		_values.add( value);
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
	
	
	public void add( JsonObject value ) {
		_values.add( value);
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
	
	public void add( Object value ) {
		_values.add( value);
	}


	public Object getObject( int index ) {
		
		Object blob = _values.get(index);
		
		return (Object)blob;
	}

	public void add( String value ) {
		_values.add( value);
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
	
	public String getString( int index, String def ) {
		
		Object blob = _values.get(index);
		
		if( null == blob ) {
			return def;
		}
		
		if( !(blob instanceof String) ) {
			
			String technicalError = String.format( "!(blob instanceof String); index = %d; blob.getClass().getName() = %s", index, blob.getClass().getName() );
			throw new BaseException( this, technicalError );
		}
		
		return (String)blob;

	}
	
	
	public static JsonArray build( Data data ) {
		
		JsonDataInput reader = new JsonDataInput(data);
		
		try{ 
			
			reader.scanToNextToken();
			return JsonArrayHandler.readJsonArray( reader );
			
		} catch( BaseException e ) {
			
			e.addContext( "position", reader.getPositionInformation() );
			throw e;
		}
		
	}
	
	public static JsonArray build( InputStream inputStream, int length ) {
		
		MutableData data = new MutableData(length);
		data.append( inputStream, length);
		return build( data );
	}

	
	
	public String toString(JsonStringOutput jsonWriter) {
		
		JSON_ARRAY_HANDLER.writeValue( this, jsonWriter);
		return jsonWriter.toString();
		
	}
	
	public String toString() {
		JsonStringOutput writer = new JsonStringOutput();
		return this.toString( writer);
		
	}



}
