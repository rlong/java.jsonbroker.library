// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json.output;

public class JsonStringOutput extends JsonOutput {
	
	StringBuilder  _stringBuilder;
	
	public JsonStringOutput() {
		
		_stringBuilder = new StringBuilder(); 
	}
	
	public void append(char c) {
		_stringBuilder.append(c);
	}
	
	public void append(String string ) {
		_stringBuilder.append(string);
	}

	public String toString(){ 		
		return _stringBuilder.toString();		
	}
	
}
