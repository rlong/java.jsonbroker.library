// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.http.headers.request;

import java.util.StringTokenizer;

import jsonbroker.library.common.auxiliary.NumericUtilities;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.log.Log;

public class Range {

	private static Log log = Log.getLog(Range.class);
	
	////////////////////////////////////////////////////////////////////////////
	// http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.35
	private Long _firstBytePosition;
	
	public Long getFirstBytePosition() {
		return _firstBytePosition;
	}

	////////////////////////////////////////////////////////////////////////////
	// http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.35
	private Long _lastBytePosition;
	
	
	public Long getLastBytePosition() {
		return _lastBytePosition;
	}

	////////////////////////////////////////////////////////////////////////////
	String _toString; 
	
	
	private Range(String value) {
		_toString = value;		
	}
	
	public String toString() {
		return _toString;
	}
	
	public static Range buildFromString(String value) {
		
		Range answer = new Range(value);
		
		StringTokenizer tokenizer = new StringTokenizer(value, "=,");
		
		String bytes = tokenizer.nextToken();
		if( !"bytes".equals( bytes ) ) {
			String technicalError = String.format( "!\"bytes\".equals( bytes ) ); bytes = '%s'; value = '%s'", bytes, value );			
			throw new BaseException( Range.class, technicalError);
		}
		
		String range = tokenizer.nextToken();

		if( tokenizer.hasMoreTokens() ) {
			String technicalError = String.format( "tokenizer.hasMoreTokens(); value = '%s'", value );
			throw new BaseException( Range.class, technicalError);
			
		}
		
		log.debug( range, "range" );
		
		
		int indexOfHyphen = range.indexOf( '-');
		if( -1 == indexOfHyphen ) {
			String technicalError = String.format( "-1 == indexOfHyphen; range = '%s'; value = '%s'", range, value );			
			throw new BaseException( Range.class, technicalError);
		}
		
		int secondHyphen = range.indexOf( '-', indexOfHyphen+1); 
		if( -1 != secondHyphen ) {
			String technicalError = String.format( "-1 != secondHyphen; range = '%s'; value = '%s'", range, value );			
			throw new BaseException( Range.class, technicalError);
		} 

		// "bytes=-500"
		if( 0 == indexOfHyphen ) {
			answer._firstBytePosition = NumericUtilities.parseLong( range );
			
			// leave _lastBytePosition as null;
			return answer;
		} 
		
		// "bytes=9500-"
		int rangeLength = range.length();
		if( '-' == range.charAt( rangeLength - 1 ) ) {
			
			range = range.substring( 0, rangeLength-1);
			
			answer._firstBytePosition = NumericUtilities.parseLong( range );
			
			// leave _lastBytePosition as null;
			return answer;
			
		}
		
		// "bytes=500-999" 
		
		String startPosition = range.substring( 0, indexOfHyphen );
		answer._firstBytePosition = NumericUtilities.parseLong( startPosition );
		
		String endPosition = range.substring( indexOfHyphen+1 );
		answer._lastBytePosition = NumericUtilities.parseLong( endPosition );
		
		return answer;
		
	}
	
	
	public String toContentRange( long entityContentLength ) {
		
		// http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.16
		long firstBytePosition = 0;
		if( null != _firstBytePosition ) {
			firstBytePosition = _firstBytePosition.longValue();
			
			// negative firstBytePosition is an offset from the end of the entityContentLength
			if( 0 > firstBytePosition ) {
				firstBytePosition  = entityContentLength + firstBytePosition;
			}
		}
		
		// http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.16
		long lastBytePosition = entityContentLength-1;
		if( null != _lastBytePosition ) {
			lastBytePosition = _lastBytePosition.longValue();
		}
		
		String answer = String.format( "bytes %d-%d/%d" , firstBytePosition, lastBytePosition, entityContentLength);
		return answer;
	}

	
	public long getSeekPosition( long entityContentLength ) {

		long firstBytePosition = 0;
		if( null != _firstBytePosition ) {
			firstBytePosition = _firstBytePosition.longValue();

			// negative firstBytePosition is an offset from the end of the entityContentLength
			if( 0 > firstBytePosition ) {
				firstBytePosition  = entityContentLength + firstBytePosition;
			}
		}
		long answer = firstBytePosition;
		return answer;

	}
	
	public long getContentLength( long entityContentLength ) {
		
		long firstBytePosition = 0;
		if( null != _firstBytePosition ) {
			firstBytePosition = _firstBytePosition.longValue();

			// negative firstBytePosition is an offset from the end of the entityContentLength
			if( 0 > firstBytePosition ) {
				firstBytePosition  = entityContentLength + firstBytePosition;
			}
		}
		
		long lastBytePosition = entityContentLength;
		if( null != _lastBytePosition ) {
			
			
			lastBytePosition = _lastBytePosition.longValue();

			// vvv http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.35.1
			
			// The last-byte-pos value gives the byte-offset of the last byte in the range; 
			// that is, the byte positions specified are inclusive
			
			// ^^^ http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.35.1
			
			// so ... 
			lastBytePosition++;

		}
		
		long answer = lastBytePosition - firstBytePosition;
		return answer;
		
	}



}
