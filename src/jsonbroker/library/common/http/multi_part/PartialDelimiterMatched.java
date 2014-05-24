// Copyright (c) 2014 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.http.multi_part;

class PartialDelimiterMatched implements DelimiterIndicator {

    ///////////////////////////////////////////////////////////////////////
    // matchingBytes
    private byte[] _matchingBytes;

    
    byte[] getMatchingBytes() {
		return _matchingBytes;
	}


    ///////////////////////////////////////////////////////////////////////
    // <init>
	public PartialDelimiterMatched( byte[] matchingBytes)
    {
        _matchingBytes = matchingBytes;
    }

}
