// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.channel;

import jsonbroker.library.common.auxiliary.Data;
import jsonbroker.library.common.auxiliary.StringHelper;
import jsonbroker.library.common.channel.Channel;
import jsonbroker.library.common.json.JsonArray;
import jsonbroker.library.common.json.JsonArrayHelper;
import jsonbroker.library.common.json.JsonBuilder;
import jsonbroker.library.common.json.JsonObject;
import jsonbroker.library.common.json.input.JsonDataInput;
import jsonbroker.library.common.json.input.JsonInput;
import jsonbroker.library.common.json.input.JsonReader;
import jsonbroker.library.common.log.Log;

public class ChannelRequest {

	
	private static final Log log = Log.getLog(ChannelRequest.class);

	
	////////////////////////////////////////////////////////////////////////////
	//
	JsonArray _channelHeader;
	
	public JsonArray getChannelHeader() {
		return _channelHeader;
	}

	////////////////////////////////////////////////////////////////////////////
	//
	JsonArray _endpointHeaderArray = null;
	
	public JsonArray getEndpointHeaderArray() {
		return _endpointHeaderArray;
	}

	////////////////////////////////////////////////////////////////////////////
	//
	JsonObject _endpointHeaderObject = null;
	
	
	public JsonObject getEndpointHeaderObject() {
		return _endpointHeaderObject;
	}

	////////////////////////////////////////////////////////////////////////////
	//
	private ChannelRequest( ) {
	}
	
	
	// can return null if the connection has been closed
	public static ChannelRequest readRequest( Channel channel )  {
		
		JsonArray channelHeader;
		{
			String channelHeaderString = channel.readLine();
			log.debug( channelHeaderString, "channelHeaderString" );
			if( null == channelHeaderString ) {
				return null;
			}
			
			channelHeader = JsonArrayHelper.fromString( channelHeaderString );
		}
		
		
		JsonArray endpointHeaderArray = null;
		JsonObject endpointHeaderObject = null;		
		{
			String endpointHeader = channel.readLine();
			log.debug( endpointHeader, "endpointHeader" );
			
			byte[] rawData = StringHelper.toUtfBytes(endpointHeader);
			Data data = new Data(rawData);
			JsonInput input = new JsonDataInput(data);

			JsonBuilder builder = new JsonBuilder();

			JsonReader.read(input, builder);
			
			endpointHeaderArray = builder.getArrayDocument();
			endpointHeaderObject = builder.getObjectDocument();
		}
		
		ChannelRequest answer = new ChannelRequest();
		
		answer._channelHeader = channelHeader;
		answer._endpointHeaderArray = endpointHeaderArray;
		answer._endpointHeaderObject = endpointHeaderObject;
		
		return answer;

	}
	
	
}
