// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.auxiliary;

import jsonbroker.library.common.exception.BaseException;


public class NumericUtilities {

	public static int parseInteger( String integerValue ) {
		try {
			int answer = Integer.parseInt( integerValue );
			return answer;
		} catch( NumberFormatException nfe ) {
			
			String technicalError = "failed to parse '"+integerValue+"' as an integer";
			BaseException e = new BaseException(NumericUtilities.class, technicalError);
			throw e;
		}
	}
	
	public static long parseLong( String longValue ) {
		try {
			long answer = Long.parseLong( longValue );
			return answer;
		} catch( NumberFormatException nfe ) {
			
			String technicalError = "failed to parse '"+longValue+"' as a long";
			BaseException e = new BaseException(NumericUtilities.class, technicalError);
			throw e;
		}
	}

	public static Integer parseIntegerObject( String integerValue ) {
		
		try {
			Integer answer = Integer.decode( integerValue );
			return answer;
		} catch( NumberFormatException nfe ) {
			
			String technicalError = "failed to parse '"+integerValue+"' as an integer";
			BaseException e = new BaseException(NumericUtilities.class, technicalError);
			throw e;
		}		
	}
	
	public static double parseDouble( String doubleValue ) {
		try { 
			
			double answer = Double.parseDouble( doubleValue );
			
			return answer;
			
		} catch( NumberFormatException nfe ) {
			
			String technicalError = "failed to parse '"+doubleValue+"' as a float";
			BaseException e = new BaseException(NumericUtilities.class, technicalError);
			throw e;
		}
	}
	
	public static Double parseDoubleObject( String floatValue ) {
		try { 
			
			Double answer = Double.valueOf( floatValue );
			
			return answer;
			
		} catch( NumberFormatException nfe ) {
			
			String technicalError = "failed to parse '"+floatValue+"' as a float";
			BaseException e = new BaseException(NumericUtilities.class, technicalError);
			throw e;
		}
	}

	
}
