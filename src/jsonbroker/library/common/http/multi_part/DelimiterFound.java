// Copyright (c) 2014 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.http.multi_part;

class DelimiterFound implements DelimiterIndicator {
	
    ///////////////////////////////////////////////////////////////////////
    // startOfDelimiter
    private int _startOfDelimiter;

    
    int getStartOfDelimiter() {
		return _startOfDelimiter;
	}


	///////////////////////////////////////////////////////////////////////
    // endOfDelimiter
    private int _endOfDelimiter;

    
    int getEndOfDelimiter() {
		return _endOfDelimiter;
	}


	///////////////////////////////////////////////////////////////////////
    // isCloseDelimiter
    private boolean _isCloseDelimiter;

    
    boolean isCloseDelimiter() {
		return _isCloseDelimiter;
	}


	///////////////////////////////////////////////////////////////////////
    // completesPartialMatch
    private boolean _completesPartialMatch;


    boolean completesPartialMatch() {
		return _completesPartialMatch;
	}


	///////////////////////////////////////////////////////////////////////
    // 
    public DelimiterFound(int startOfDelimiter, int endOfDelimiter, boolean isCloseDelimiter, boolean completesPartialMatch) {
        _startOfDelimiter = startOfDelimiter;
        _endOfDelimiter = endOfDelimiter;
        _isCloseDelimiter = isCloseDelimiter;
        _completesPartialMatch = completesPartialMatch;
    }


}
