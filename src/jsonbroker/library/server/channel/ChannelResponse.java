// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.channel;

import jsonbroker.library.common.channel.Channel;
import jsonbroker.library.common.json.JsonArray;
import jsonbroker.library.common.json.JsonArrayHelper;
import jsonbroker.library.common.json.JsonObject;
import jsonbroker.library.common.json.JsonObjectHelper;

public class ChannelResponse {

	private static final byte[] NEWLINE = { '\n' };

	////////////////////////////////////////////////////////////////////////////
	//
	JsonArray _channelHeader = null;
	
	
	ChannelResponse( JsonArray channelRequestHeader ) {
		
		_channelHeader = channelRequestHeader;
		
	}
	
	private void writeResponse( Channel channel, byte[] endpointResponse ) {

		_channelHeader.set( 4, endpointResponse.length );
		_channelHeader.add( 200 ); // 200: OK

		// write the channel header
		{
			byte[] channelHeaderBytes = JsonArrayHelper.toBytes( _channelHeader );
			channel.write( channelHeaderBytes );
			channel.write( NEWLINE );
		}
		
		// write the endpoint header 
		{
			channel.write( endpointResponse );
			channel.write( NEWLINE );
		}
	}
	
	public void writeResponse( Channel channel, JsonArray endpointResponse ) {
		
		byte[] endpointResponseBytes = JsonArrayHelper.toBytes( endpointResponse );
		writeResponse( channel, endpointResponseBytes );
	}
	
	public void writeResponse( Channel channel, JsonObject endpointResponse ) {
		
		byte[] endpointResponseBytes = JsonObjectHelper.toBytes( endpointResponse );
		writeResponse( channel, endpointResponseBytes );
	}


}
