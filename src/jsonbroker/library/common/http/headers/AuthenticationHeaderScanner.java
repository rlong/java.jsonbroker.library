// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.http.headers;

import java.math.BigInteger;

import jsonbroker.library.common.log.Log;

public class AuthenticationHeaderScanner {
	

	private static final Log log = Log.getLog(AuthenticationHeaderScanner.class);
	
	
	
    ///////////////////////////////////////////////////////////////////////
    String _authenticationHeader;


    ///////////////////////////////////////////////////////////////////////
    int _cursor;


	protected int getCursor() {
		return _cursor;
	}


	protected void setCursor(int cursor) {
		_cursor = cursor;
	}
	
	
	////////////////////////////////////////////////////////////////////////////

    public AuthenticationHeaderScanner(String authenticationHeader)
    {
        _authenticationHeader = authenticationHeader;

        _cursor = 0;

    }

	////////////////////////////////////////////////////////////////////////////

    // equivalent to [NSScanner scanString:]
    // protected for test purposes
    // will skip over any trailing spaces 
    protected void scanString(String expectedValue)
    {

        // expectedValue too long ?
        if( _cursor + expectedValue.length() > _authenticationHeader.length() ) {
            return;
        }

        int newCursorPosition = _cursor;

        for( int i = 0, count = expectedValue.length(); i < count; i++, newCursorPosition++ ) {

            // mismatch ? 
            if( expectedValue.charAt(i) != _authenticationHeader.charAt(newCursorPosition) ) {

                // return without updating _cursor
                return;
            }
        }

        _cursor = newCursorPosition;

        // skip past space characters 
        while (_cursor < _authenticationHeader.length() && ' ' == _authenticationHeader.charAt(_cursor))
        {
            _cursor++;
        }

    }
    
    // equivalent to [NSScanner scanUpToString:]
    // protected for test purposes 
    // can return null if expectedValue is not found 
    public String scanUpToString(String target)
    {
        String answer = null;

        int indexOfTarget = _authenticationHeader.indexOf(target, _cursor);
        if (-1 == indexOfTarget)
        {

            answer = _authenticationHeader.substring(_cursor);
            _cursor = _authenticationHeader.length();

            return answer;
        }
        
        int length = indexOfTarget - _cursor;

        // C# 'Substring' takes starting point and length ... 
        //answer = _authenticationHeader.Substring(_cursor, length);
        // Java 'substring' takes starting point and end point ... 
        answer = _authenticationHeader.substring(_cursor, indexOfTarget);

        _cursor += length;

        return answer;

    }

    
    // equivalent to [NSScanner isAtEnd]
    // public for test purposes 
    public Boolean isAtEnd()
    {
        if (_cursor >= _authenticationHeader.length())
        {
            return true;
        }
        return false;
    }
    
    
    public void scanPastDigestString()
    {
        scanString("Digest");
    }
    
    
    public String scanName()
    {
        if (isAtEnd())
        {
            return null;
        }

        String answer = scanUpToString("=");

        if (null != answer)
        {
            scanString("=");
        }

        return answer;

    }

    
    public String scanQuotedValue()
    {
        scanString("\""); // dispose of the opening '"'

        String answer = scanUpToString("\"");
        scanString("\""); // dispose of the closing '"'
        scanString(","); // discard any trailing ',' that *might* exist

        return answer;

    }

    
    private int convertHexChar(char c)
    {

        if (c >= '0' && c <= '9')
        {
            return c - '0';
        }
        if (c >= 'a' && c <= 'f')
        {
            return (c - 'a') + 10;
        }
        if (c >= 'A' && c <= 'F')
        {
            return (c - 'A')+10;
        }

        return -1;
    }

    // equivalent to [NSScanner scanHexLongLong:]
    private BigInteger scanHexULong()
    {
    	
    	byte[] bigIntegerBuffer = new byte[1];

        if (isAtEnd()) {
        	//return 0;
        	return BigInteger.ZERO;
          
        }

        BigInteger answer = BigInteger.ZERO;

        while( _cursor < _authenticationHeader.length() ) 
        {
            char c = _authenticationHeader.charAt(_cursor);
            int numericValue = convertHexChar(c);

            // not a hex digit ?
            if (-1 == numericValue) 
            {
                return answer; // ... leave _cursor pointing at the non-numeric character
            }
            
            
            answer = answer.shiftLeft( 4 ); // shift left by one nibble
            
            bigIntegerBuffer[0] = (byte)(numericValue & 0xF);
            BigInteger nibble = new BigInteger( bigIntegerBuffer );
            answer = answer.or( nibble );
            
            _cursor++;

        }

        return answer;
    }

    
    public long scanHexUInt32()
    {

    	long answer = scanHexULong().longValue();
    	

    	if( answer > 0xFFFFFFFFl ) { // overflow
    		
            log.warn("answer > 0xFFFFFFFF");

            return 0;
        }

        scanString(","); // discard any trailing ',' that *might* exist

        return answer;

    }

    public String scanValue()
    {
        String answer = null;

        answer = scanUpToString(",");
        scanString(","); // discard any trailing ',' that *might* exist

        return answer;

    }



}
