// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.http.headers;

import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.log.Log;

//http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.6
public class ParametersScanner {

	private static final Log log = Log.getLog(ParametersScanner.class);
	
	private static boolean[] tokenChars = new boolean[128];
	
	static {
		
        for (int i = 0; i < 128; i++)
        {
            tokenChars[i] = true;
        }
        
        // http://www.w3.org/Protocols/rfc2616/rfc2616-sec2.html#sec2.2
        // CTL chars ... 
        {
            for (int i = 0; i < 32; i++)
            {
                tokenChars[i] = false;
            }

            tokenChars[127] = false; // 'DEL'
        }

        
        // http://www.w3.org/Protocols/rfc2616/rfc2616-sec2.html#sec2.2
        // separators ...  
        {
            tokenChars['('] = false;
            tokenChars[')'] = false;
            tokenChars['<'] = false;
            tokenChars['>'] = false;
            tokenChars['@'] = false;

            tokenChars[','] = false;
            tokenChars[';'] = false;
            tokenChars[':'] = false;
            tokenChars['\\'] = false;
            tokenChars['"'] = false;

            tokenChars['/'] = false;
            tokenChars['['] = false;
            tokenChars[']'] = false;
            tokenChars['?'] = false;
            tokenChars['='] = false;

            tokenChars['{'] = false;
            tokenChars['}'] = false;
            tokenChars[' '] = false;
            tokenChars[9] = false;  // HT
        }

	}
	
    int _offset;
    String _value;

    
    public ParametersScanner(int offset, String value) {

        _offset = offset;
        _value = value;

    }
    
    // http://www.w3.org/Protocols/rfc2616/rfc2616-sec2.html#sec2.2
    // only covers the octets 0-31 and 127 ... does not include ' ' (space)
    private static boolean isTokenCharacter(char c) {
        if (c > 128) {
            return false;
        }
        return tokenChars[c];
    }

    // returns true if another token was found
    private boolean moveToStartOfNextToken(boolean quotesIsTokenCharacter)
    {

        int length = _value.length();

        while (_offset < length && !isTokenCharacter(_value.charAt( _offset ))) {
            if (quotesIsTokenCharacter && '"' == _value.charAt( _offset )) {
                return true;
            }
            _offset++;
            
        }

        // run out of string ? 
        if (_offset == length) {
            return false;
        }

        return true;

    }

    
    private void moveToEndOfToken()
    {

        int length = _value.length();

        while (_offset < length && isTokenCharacter(_value.charAt(_offset))) {
            _offset++;
        }

    }
    
    // http://www.w3.org/Protocols/rfc2616/rfc2616-sec2.html#sec2.2
    private String nextToken(boolean quotesIsTokenCharacter) {

        if (!moveToStartOfNextToken(quotesIsTokenCharacter)) {
            return null;
        }

        int startOfToken = _offset;
        log.debug(startOfToken, "startOfToken");

        moveToEndOfToken();

        String answer = _value.substring(startOfToken, _offset); // subtring( int start, int end )

        return answer;

    }
    
    // returns null when there are no more keys
    // http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.6
    public String nextAttribute()
    {
        if (_offset >= _value.length())
        {
            return null;
        }

        String answer = nextToken(false);
        return answer;

    }
    
    private void moveToEndOfQuotedString()
    {
        boolean lastCharWasAnEscape = false;

        int length = _value.length();

        while(_offset < length && (lastCharWasAnEscape || '"' != _value.charAt(_offset)) ) {
            char lastChar = _value.charAt(_offset);
            _offset++;

            if (lastCharWasAnEscape) {
                lastCharWasAnEscape = false;
                continue;
            }

            if ('\\' == lastChar) {
                lastCharWasAnEscape = true;
                continue;
            }
        }
    }
    
    public String nextValue()
    {
        if (!moveToStartOfNextToken(true))
        {
            BaseException e = new BaseException( this, "!moveToStartOfNextToken(true); _value = '%s'", _value );
            throw e;
        }

        boolean isQuotedString = false;
        int startOfToken = _offset;

        // http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.6
        if ('"' == _value.charAt(_offset))
        {
            isQuotedString = true;
            _offset++; // move past the quotes
            startOfToken = _offset;
            if (_offset >= _value.length())
            {
                BaseException e = new BaseException(this, "_offset >= _value.length(); _offset = %d; _value.Length = %d; _value = '%s'", _offset, _value.length(), _value);
                throw e;
            }
            moveToEndOfQuotedString();
        }
        else
        {
            moveToEndOfToken();
        }

        String answer = _value.substring(startOfToken, _offset); // subtring( int start, int end )

        if (isQuotedString) // move past the closing quotes
        {
            _offset++;
        }

        return answer;

    }
	
}
