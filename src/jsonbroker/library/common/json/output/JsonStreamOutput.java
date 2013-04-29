// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import jsonbroker.library.common.auxiliary.StringHelper;
import jsonbroker.library.common.exception.BaseException;

public class JsonStreamOutput implements JsonOutput {

	OutputStreamWriter _outputStreamWriter;
	
	public JsonStreamOutput( OutputStream outputStream ) {
		
		try {
			_outputStreamWriter = new OutputStreamWriter( outputStream, StringHelper.UTF_8 );
		} catch (UnsupportedEncodingException e) {
			throw new BaseException( this, e);
		} 
	}

	@Override
	public void append(char c) {
		
		try {
			_outputStreamWriter.append( c );
		} catch (IOException e) {
			throw new BaseException( this, e);
		}
	}

	@Override
	public void append(String string) {
		try {
			_outputStreamWriter.append( string );
		} catch (IOException e) {
			throw new BaseException( this, e);
		}
	}
	
	public void flush() {
		try {
			_outputStreamWriter.flush();
		} catch (IOException e) {
			throw new BaseException( this, e);
		}
	}
	
}
