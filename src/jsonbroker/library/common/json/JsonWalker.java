// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json;

import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import jsonbroker.library.common.auxiliary.NullObject;

public class JsonWalker {
	
	private static void walkArray( JsonArray jsonArray, JsonDocumentHandler visitor ) {
		
		for( int index = 0, count = jsonArray.size(); index < count; index++ ) {
			
			Object value = jsonArray.getObject( index, null );
			
			if( null == value ) {
				visitor.onNull( index );
				continue;
			}
			
			if( NullObject.INSTANCE == value ) {
				visitor.onNull( index );
				continue;
			}
			
			if( value instanceof Boolean )  {
				visitor.onBoolean( index, (Boolean)value);
				continue;
			}
			
			if( value instanceof Number ) {
				visitor.onNumber( index, (Number)value);
				continue;
			}
			
			if( value instanceof String ) {
				visitor.onString( index, (String)value);
				continue;
			}
			
			if( value instanceof JsonObject ) {
				visitor.onObjectStart( index );
				walkObject( (JsonObject)value, visitor );
				visitor.onObjectEnd( index );
				continue;
			}
			
			if( value instanceof JsonArray ) {
				visitor.onArrayStart( index );
				walkArray( (JsonArray)value, visitor);
				visitor.onArrayEnd(index);
				continue;
			}
		}
	}
	
	private static void walkObject( JsonObject jsonObject, JsonDocumentHandler visitor ) {

		Set<Entry<String, Object>> entrySet = jsonObject.entrySet();
		for( Iterator<Entry<String, Object>> i = entrySet.iterator(); i.hasNext(); ) {
			
			Entry<String, Object> e = i.next();
			String key = e.getKey();
			Object value = e.getValue();
			
			if( null == value ) {
				visitor.onNull( key );
				continue;
			}
			
			if( NullObject.INSTANCE == value ) {
				visitor.onNull( key );
				continue;
			}
			
			if( value instanceof Boolean )  {
				visitor.onBoolean( key, (Boolean)value);
				continue;
			}
			
			if( value instanceof Number ) {
				visitor.onNumber( key, (Number)value);
				continue;
			}
			
			if( value instanceof String ) {
				visitor.onString( key, (String)value);
				continue;
			}
			
			if( value instanceof JsonObject ) {
				visitor.onObjectStart( key );
				walkObject( (JsonObject)value, visitor );
				visitor.onObjectEnd( key );
				continue;
			}
			
			if( value instanceof JsonArray ) {
				visitor.onArrayStart( key );
				walkArray( (JsonArray)value, visitor);
				visitor.onArrayEnd(key);
				continue;
			}			
		}
	}
	
	public static void walk( JsonObject jsonObject, JsonDocumentHandler visitor ) {
		
		visitor.onObjectDocumentStart();
		
		walkObject( jsonObject, visitor);
		
		visitor.onObjectDocumentEnd();
		
	}

	public static void walk( JsonArray jsonArray, JsonDocumentHandler visitor ) {
		
		visitor.onArrayDocumentStart();
		
		walkArray( jsonArray, visitor);
		
		visitor.onArrayDocumentEnd();
		
	}

}
