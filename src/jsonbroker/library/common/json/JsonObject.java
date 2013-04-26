// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json;

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import jsonbroker.library.common.auxiliary.Data;
import jsonbroker.library.common.auxiliary.MutableData;
import jsonbroker.library.common.auxiliary.StringHelper;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.json.handlers.JsonObjectHandler;
import jsonbroker.library.common.json.input.JsonDataInput;
import jsonbroker.library.common.json.output.JsonStringOutput;
import jsonbroker.library.common.log.Log;




public class JsonObject implements Serializable {//extends HashMap<String, Object> {

	
	private static Log log = Log.getLog( JsonObject.class );

	/**
	 * 
	 */
	private static final long serialVersionUID = 2941215311750480925L;

	////////////////////////////////////////////////////////////////////////////
	HashMap<String, Object> _values;

	////////////////////////////////////////////////////////////////////////////

	public JsonObject(int capacity) {
		_values = new HashMap<String, Object>(capacity);		
	}

	public JsonObject() {
		_values = new HashMap<String, Object>();		
	}

	
	public JsonObject(Object ... nameValues) {
		
		_values = new HashMap<String, Object>(nameValues.length/2);
		
		for( int i = 0, count = nameValues.length; i < count; i+=2 ) {
			String key = (String)nameValues[i];
			Object value =  nameValues[i+1];
			_values.put( key, value);
		}
	}
	
	
	
	public boolean contains( String key ) {
		
		Object blob = _values.get(key);
		if( null == blob ) { 
			return false;
		}
		return true;
	}
	
	public boolean getBoolean( String key ) {
		
		Object blob = _values.get(key);
		
		if( null == blob ) {
			String technicalError = String.format( "null == blob; key = %s", key );
			throw new BaseException( this, technicalError );
			
		}

			
		if( !(blob instanceof Boolean) ) {
			String technicalError = String.format( "!(blob instanceof Boolean); key = %s; blob.getClass().getName() = %s", key, blob.getClass().getName() );
			throw new BaseException( this, technicalError );
		}
		
		Boolean answer = (Boolean)blob;
		return answer.booleanValue();

	}
	
	public boolean getBoolean( String key, boolean defaultValue ) {
	
		Object blob = _values.get(key);
		
		if( null == blob ) {
			String message = String.format( "null == blob; key = %s", key );			
			log.debug( message );
			return defaultValue;
		}
		
		if( !(blob instanceof Boolean) ) {
			
			String warning = String.format( "!(blob instanceof Boolean); key = %s; blob.getClass().getName() = %s", key, blob.getClass().getName() );
			log.warn( warning );
			return defaultValue;
		}
		
		return (Boolean)blob;

	}

	public Integer getIntegerObject( String key ) {
		
		Object blob = _values.get(key);
		
		if( null == blob ) {
			
			String technicalError = String.format( "null == blob; key = %s", key );
			throw new BaseException( this, technicalError );
		}

		if( !(blob instanceof Integer) ) {
			String technicalError = String.format( "!(blob instanceof Integer); key = %s; blob.getClass().getName() = %s", key, blob.getClass().getName() );
			throw new BaseException( this, technicalError );
		}
		
		Integer integer = (Integer)blob;
		return integer;

	}
	
	public Integer getIntegerObject( String key, Integer defaultValue ) {
		
		Object blob = _values.get(key);
		
		if( null == blob ) {
			return defaultValue;			
		}

		if( !(blob instanceof Integer) ) {
			String technicalError = String.format( "!(blob instanceof Integer); key = %s; blob.getClass().getName() = %s", key, blob.getClass().getName() );
			throw new BaseException( this, technicalError );
		}
		
		Integer integer = (Integer)blob;
		return integer;

	}
	
	
	public int getInt( String key ) {
		
		Object blob = _values.get(key);
		
		if( null == blob ) {
			String technicalError = String.format( "null == blob; key = %s", key );
			throw new BaseException( this, technicalError );
			
		}

		if( !(blob instanceof Integer) ) {
			
			String technicalError = String.format( "!(blob instanceof Integer); key = %s; blob.getClass().getName() = %s", key, blob.getClass().getName() );
			throw new BaseException( this, technicalError );
		}
		
		Integer integer = (Integer)blob;
		return integer.intValue();

	}
	
	public int getInt( String key, int defaultValue ) {
		
		Object blob = _values.get(key);
		
		if( null == blob ) {
			//String warning = String.format( "null == blob; key = %s", key );
			//log.warn( warning );
			return defaultValue;			
		}

		if( !(blob instanceof Integer) ) {
			
			String warning = String.format( "!(blob instanceof Integer); key = %s; blob.getClass().getName() = %s", key, blob.getClass().getName() );
			log.warn( warning );
			return defaultValue;
		}
		
		Integer integer = (Integer)blob;
		return integer.intValue();
	}
	
	public JsonArray getJsonArray( String key ) {
		
		Object blob = _values.get(key);
		
		if( null == blob ) {
			String technicalError = String.format( "null == blob; key = %s", key );
			throw new BaseException( this, technicalError );
			
		}
		
		if( !(blob instanceof JsonArray) ) {
			
			String technicalError = String.format( "!(blob instanceof JSONArray); key = %s; blob.getClass().getName() = %s", key, blob.getClass().getName() );
			throw new BaseException( this, technicalError );
		}
		
		return (JsonArray)blob;
	}
	
	public JsonArray getJsonArray( String key, JsonArray defaultValue ) {
		
		Object blob = _values.get(key);
		
		if( null == blob ) {
			String warning = String.format( "null == blob; key = %s", key );
			log.warn( warning );
			return defaultValue;
		}
		
		if( !(blob instanceof JsonArray) ) {
			
			String warning = String.format( "!(blob instanceof JSONArray); key = %s; blob.getClass().getName() = %s", key, blob.getClass().getName() );
			log.warn( warning );
			return defaultValue;
		}
		
		return (JsonArray)blob;
	}

	public JsonObject getJsonObject( String key ) {
		
		Object blob = _values.get(key);
		
		if( null == blob ) {
			String technicalError = String.format( "null == blob; key = %s", key );
			throw new BaseException( this, technicalError );
			
		}
		
		if( !(blob instanceof JsonObject) ) {
			
			String technicalError = String.format( "!(blob instanceof JSONObject); key = %s; blob.getClass().getName() = %s", key, blob.getClass().getName() );
			throw new BaseException( this, technicalError );
		}
		
		return (JsonObject)blob;
	}

	public JsonObject getJsonObject( String key, JsonObject defaultValue ) {
		
		Object blob = _values.get(key);
		
		if( null == blob ) {
			String warning = String.format( "null == blob; key = %s", key );
			log.warn( warning );
			return defaultValue;
			
		}
		
		if( !(blob instanceof JsonObject) ) {
			
			String warning = String.format( "!(blob instanceof JSONObject); key = %s; blob.getClass().getName() = %s", key, blob.getClass().getName() );
			log.warn( warning );
			return defaultValue;
		}
		
		return (JsonObject)blob;
	}

	
	public long getLong( String key ) {
		
		Object blob = _values.get(key);
		
		if( null == blob ) {
			String technicalError = String.format( "null == blob; key = %s", key );
			throw new BaseException( this, technicalError );
			
		}

		if( blob instanceof Integer ) {
			Integer integer = (Integer)blob;
			return integer.longValue();	
		}
		if( blob instanceof Long ) {
			Long longObject = (Long)blob;
			return longObject.longValue();			
		}
		
		throw new BaseException( this, "unhandled blob type; key = %s; blob.getClass().getName() = %s", key, blob.getClass().getName() );

	}
	
	public long getLong( String key, long defaultValue ) {
		
		Object blob = _values.get(key);
		
		if( null == blob ) {
			log.warnFormat( "null == blob; key = %s", key );
			return defaultValue;
		}
		
		if( blob instanceof Integer ) {
			Integer integer = (Integer)blob;
			return integer.longValue();	
		}
		if( blob instanceof Long ) {
			Long longObject = (Long)blob;
			return longObject.longValue();			
		}

		throw new BaseException( this, "unhandled blob type; key = %s; blob.getClass().getName() = %s", key, blob.getClass().getName() );

	}

	
	public Object getObject( String key ) {
		
		Object blob = _values.get(key);
		
		if( null == blob ) {
			String technicalError = String.format( "null == blob; key = %s", key );
			throw new BaseException( this, technicalError );
			
		}
		
		return blob;
		
	}
	
	public Object getObject( String key, Object defaultValue ) {
		
		Object blob = _values.get(key);
		
		if( null == blob ) {
			return defaultValue;
		}
		
		return blob;

	}


	public String getString( String key) {
		Object blob = _values.get(key);
		
		if( null == blob ) {
			String technicalError = String.format( "null == blob; key = %s", key );
			throw new BaseException( this, technicalError );
		}
		
		if( !(blob instanceof String) ) {
			
			String technicalError = String.format( "!(blob instanceof String); key = %s; blob.getClass().getName() = %s", key, blob.getClass().getName() );
			throw new BaseException( this, technicalError );
		}
		
		return (String)blob;
	}

	public String getString( String key, String defaultValue ) {
		
		Object blob = _values.get(key);
		
		if( null == blob ) {
			return defaultValue;
		}
		
		if( !(blob instanceof String) ) {
			
			String warning = String.format( "!(blob instanceof String); key = %s; blob.getClass().getName() = %s", key, blob.getClass().getName() );
			log.warn( warning );
			
			return defaultValue;
		}
		
		return (String)blob;

	}

	public static JsonObject build( Data data ) {
		
		JsonDataInput reader = new JsonDataInput(data);
		reader.scanToNextToken();
		
		return JsonObjectHandler.readJsonObject( reader );
		
	}
	
	public static JsonObject build( InputStream inputStream, int length ) {
		
		MutableData data = new MutableData(length);
		data.append( inputStream, length);
		return build( data );
	}

	
	public static JsonObject build( String jsonString ) {
		
		byte[] bytes = jsonString.getBytes();		
		MutableData data = new MutableData( bytes );
		
		return build( data );
		
	}

	public String toString(JsonStringOutput jsonWriter) {
		
		JsonObjectHandler.writeJsonObject(this, jsonWriter);
		return jsonWriter.toString();
		
	}
	
	
	public String toString() {
		JsonStringOutput writer = new JsonStringOutput();
		return this.toString( writer);
		
	}

	public byte[] toBytes() {
		String stringRepresentation = this.toString();
		return StringHelper.toUtfBytes( stringRepresentation );
	}

	public void update( JsonObject other ) {
		
		Set<Entry<String, Object>> entrySet = other._values.entrySet();

		for( Iterator<Entry<String, Object>> i = entrySet.iterator(); i.hasNext(); ) {
			
			Entry<String, Object> entry = i.next();
			_values.put( entry.getKey(), entry.getValue());			
		}

	}

	public void put(String key, boolean value) {
		_values.put( key, value);
	}

	public void put(String key, int value) {
		_values.put( key, value);
	}
	
	public void put(String key, String value) {
		_values.put( key, value);
	}

	public void put(String key, Object value) {
		_values.put( key, value);
	}

	
	public void remove( String key ) {
		_values.remove( key );
	}
	
	public Set<Entry<String, Object>> entrySet() {
		return _values.entrySet();
	}

	
}
