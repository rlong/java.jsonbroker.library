// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.channel;

import jsonbroker.library.common.channel.Channel;
import jsonbroker.library.common.log.Log;

public class ChannelHeader {
	
	
	private static final Log log = Log.getLog(ChannelHeader.class);

	String _rawHeader;
	
	private ChannelHeader( String rawHeader ) {
		
		_rawHeader = rawHeader;
	}
	
	public static ChannelHeader readHeader( Channel channel )  {
		
		String rawHeader = channel.readLine();
		log.debug( rawHeader, "rawHeader" );
		
		return new ChannelHeader( rawHeader );

	}
	
	public void writeResponse( Channel channel, int statusCode ) {
		
		String rawHeader = _rawHeader.substring( 0, _rawHeader.length() - 2 ); // hacky removal of the last ']'
		rawHeader = rawHeader + "," + statusCode + "]";
		
	}

}
