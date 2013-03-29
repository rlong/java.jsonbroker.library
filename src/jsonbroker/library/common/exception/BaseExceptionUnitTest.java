// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.exception;

import jsonbroker.library.common.log.Log;
import junit.framework.TestCase;

public class BaseExceptionUnitTest extends TestCase {
	
	private static Log log = Log.getLog(BaseExceptionUnitTest.class);
	
	public void test1() { 
		log.enteredMethod();
		 
	}
	
	public void testLogBaseException() {
		BaseException e = new BaseException( this, "technicalError");
		log.error( e );
		
	}

}
