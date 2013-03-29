// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.http.headers.request;

import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.log.Log;
import junit.framework.TestCase;

public class RangeUnitTest extends TestCase {

	private static Log log = Log.getLog( RangeUnitTest.class );
	
	public void test1() { 
		log.enteredMethod();
	}
	
	
	// from http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.35.1 
	public void testHyphen500() {
		Range range = Range.buildFromString( "bytes=-500" );
		assertEquals( -500, range.getFirstBytePosition().longValue() );
		assertNull( range.getLastBytePosition() );
		
		assertEquals( "bytes 9500-9999/10000", range.toContentRange( 10000 ) );
		assertEquals( 9500, range.getSeekPosition( 10000 ) );
		assertEquals( 500, range.getContentLength( 10000 ) );
		
		
	}
	
	// from http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.35.1
	public void test9500Hyphen() { 
		Range range = Range.buildFromString( "bytes=9500-" );
		assertEquals( 9500, range.getFirstBytePosition().longValue() );
		assertNull( range.getLastBytePosition() );
		
		assertEquals( "bytes 9500-9999/10000", range.toContentRange( 10000 ) );
		assertEquals( 9500, range.getSeekPosition( 10000 ) );
		assertEquals( 500, range.getContentLength( 10000 ) );

	}

	// from http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.35.1
	public void test500to999() {
		
		Range range = Range.buildFromString( "bytes=500-999" );
		assertEquals( 500, range.getFirstBytePosition().longValue() );
		assertEquals( 999, range.getLastBytePosition().longValue() );
		
		assertEquals( "bytes 500-999/10000", range.toContentRange( 10000 ) );
		assertEquals( 500, range.getSeekPosition( 10000 ) );
		assertEquals( 500, range.getContentLength( 10000 ) );

		
	}
	public void test0Hyphen499() { 
		Range range = Range.buildFromString( "bytes=0-499" );
		assertEquals( 0, range.getFirstBytePosition().longValue() );
		assertEquals( 499, range.getLastBytePosition().longValue() );
		
		assertEquals( "bytes 0-499/10000", range.toContentRange( 10000 ) );
		assertEquals( 0, range.getSeekPosition( 10000 ) );
		assertEquals( 500, range.getContentLength( 10000 ) );
	}
	
	public void test0Hyphen() { 
		Range range = Range.buildFromString( "bytes=0-" );
		assertEquals( 0, range.getFirstBytePosition().longValue() );
		assertNull( range.getLastBytePosition() );

		assertEquals( "bytes 0-9999/10000", range.toContentRange( 10000 ) );
		assertEquals( 0, range.getSeekPosition( 10000 ) );
		assertEquals( 10000, range.getContentLength( 10000 ) );

	}
	
	public void test0Hyphen1() { 
		Range range = Range.buildFromString( "bytes=0-1" );
		assertEquals( 0, range.getFirstBytePosition().longValue() );
		assertEquals( 1, range.getLastBytePosition().longValue() );
		
		assertEquals( "bytes 0-1/10000", range.toContentRange( 10000 ) );
		assertEquals( 0, range.getSeekPosition( 10000 ) );
		assertEquals( 2, range.getContentLength( 10000 ) );

	}
	
	public void test0Hyphen0() {
		
		Range range = Range.buildFromString( "bytes=0-0" );
		assertEquals( 0, range.getFirstBytePosition().longValue() );
		assertEquals( 0, range.getLastBytePosition().longValue() );
		
		assertEquals( "bytes 0-0/10000", range.toContentRange( 10000 ) );
		assertEquals( 0, range.getSeekPosition( 10000 ) );
		assertEquals( 1, range.getContentLength( 10000 ) );
	}

	public void test0Hyphen9999() {
		Range range = Range.buildFromString( "bytes=0-9999" );
		assertEquals( 0, range.getFirstBytePosition().longValue() );
		assertEquals( 9999, range.getLastBytePosition().longValue() );
		
		assertEquals( "bytes 0-9999/10000", range.toContentRange( 10000 ) );
		assertEquals( 0, range.getSeekPosition( 10000 ) );
		assertEquals( 10000, range.getContentLength( 10000 ) );
	}

	public void test0HyphenEightGig() {
		
		long eightMeg = 8 * 1024 * 1024;
		
		long eightGig = eightMeg * 1024;
		String rangeString = "bytes=0-" + eightGig;
		log.debug( rangeString, "rangeString" );
		
		Range range = Range.buildFromString( rangeString );
		
		assertEquals( 0, range.getSeekPosition( eightGig ) );
		assertEquals( eightGig+1, range.getContentLength( eightGig ) );
		assertEquals( "bytes=0-8589934592", range.toString() );
	}
	
	public void testBadRange1() {
		
		try { 
			Range.buildFromString( "bytes=1-2-3" );
			fail();
		} catch( BaseException e )  {
			// good 
		}
	}
	
	
	public void testUnhandledRange1() {
		
		try { 
			log.info( "valid but unhandled scenario" );
			
			// from http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.35.1
			Range.buildFromString( "bytes=0-0,-1" );
			fail();
		} catch( BaseException e )  {
			// good 
		}
	}

}
