// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json;


public class DefaultJsonHandler implements JsonDocumentHandler {

	////////////////////////////////////////////////////////////////////////////
	// document

	@Override
	public void onArrayDocumentEnd() {
	}

	@Override
	public void onArrayDocumentStart() {
	}

	@Override
	public void onObjectDocumentEnd() {
		
	}

	@Override
	public void onObjectDocumentStart() {
	}

	////////////////////////////////////////////////////////////////////////////
	// array
	

	@Override
	public void onArrayStart(int index) {
	}

	@Override
	public void onArrayEnd(int index) {
		
	}

	@Override
	public void onBoolean(int index, Boolean value) {
		
	}

	@Override
	public void onNull(int index) {
		
	}

	@Override
	public void onNumber(int index, Number value) {
		
	}

	@Override
	public void onObjectStart(int index) {		
	}
	
	@Override
	public void onObjectEnd(int index) {
		
	}

	@Override
	public void onString(int index, String value) {
	}


	////////////////////////////////////////////////////////////////////////////
	// object

	@Override
	public void onArrayStart(String key) {
	}

	@Override
	public void onArrayEnd(String key) {
		
	}


	@Override
	public void onBoolean(String key, Boolean value) {
		
	}

	@Override
	public void onNull(String key) {
		
	}

	@Override
	public void onNumber(String key, Number value) {
		
	}

	@Override
	public void onObjectStart(String key) {
	}

	@Override
	public void onObjectEnd(String key) {
		
	}


	@Override
	public void onString(String key, String value) {
		
	}

}
