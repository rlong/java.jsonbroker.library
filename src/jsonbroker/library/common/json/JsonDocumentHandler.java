// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json;

public interface JsonDocumentHandler  {

	////////////////////////////////////////////////////////////////////////////
	// document
	
	public void onObjectDocumentStart();
	public void onObjectDocumentEnd();
	
	public void onArrayDocumentStart();
	public void onArrayDocumentEnd();

	////////////////////////////////////////////////////////////////////////////
	// array
	
	public void onArrayStart( int index );
	public void onArrayEnd( int index );

	public void onBoolean( int index, Boolean value );
	public void onNull( int index );
	public void onNumber( int index, Number value );
	
	public void onObjectStart( int index );
	public void onObjectEnd( int index );

	public void onString( int index, String value );
	
	
	////////////////////////////////////////////////////////////////////////////
	// object
	
	public void onArrayStart( String key );
	public void onArrayEnd( String key );
	
	public void onBoolean( String key, Boolean value );
	public void onNull( String key );
	public void onNumber( String key, Number value );
	
	public void onObjectStart( String key );
	public void onObjectEnd( String key );

	public void onString( String key, String value );


	
}
