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

public class ChannelHandler implements Runnable {

	private static final Log log = Log.getLog(ChannelHandler.class);

	private static int _threadId = 1;

	Channel _channel;	
	ChannelEndpoint _channelEndpoint;
	
	private ChannelHandler( Channel channel, ChannelEndpoint channelEndpoint ) {
		
		_channel = channel;
		_channelEndpoint = channelEndpoint;
		
	}
	
	 
	// can return null (in the event of a closed channel)
	private JsonArray readChannelRequestHeader()
	{
		String channelHeaderString = _channel.readLine();
		log.debug( channelHeaderString, "channelHeaderString" );
		if( null == channelHeaderString ) {
			return null;
		}
		
		JsonArray answer = JsonArrayHelper.fromString( channelHeaderString );
		return answer;
	}

	private JsonBuilder readerEndpointHeader() {
		
		String endpointHeader = _channel.readLine();
		log.debug( endpointHeader, "endpointHeader" );
		
		byte[] rawData = StringHelper.toUtfBytes(endpointHeader);
		Data data = new Data(rawData);
		
		JsonInput input = new JsonDataInput(data);

		JsonBuilder answer = new JsonBuilder();
		JsonReader.read(input, answer);
		
		return answer;

	}
	
	private boolean processRequest() {
		
		JsonArray channelRequestHeader = readChannelRequestHeader();
		if( null == channelRequestHeader ) {
			return false;
		}

		ChannelResponse channelResponse = new ChannelResponse( channelRequestHeader );
		
		JsonBuilder endpointRequestBuilder = readerEndpointHeader();

		{
			JsonArray endpointRequest = endpointRequestBuilder.getArrayDocument();
			if( null != endpointRequest ) {
				_channelEndpoint.handleRequest(_channel, endpointRequest, channelResponse);
				return true;
			}			
		}

		{
			JsonObject endpointRequest = endpointRequestBuilder.getObjectDocument();
			if( null != endpointRequest ) {
				_channelEndpoint.handleRequest(_channel, endpointRequest, channelResponse);
				return true;
			}			
		}
		
		log.error( "unexpected code path" );
		
		return false;
	}
	
	
	private void processRequests() {
		
		try { 
			while( processRequest() ) {
			}
		} catch( Throwable t) {
			log.error( t );
		}
	}
	
	@Override
	public void run() {
		
		try { 
			processRequests();
		} catch( Throwable t) {
			log.error( t );
		}
		
		
		try { 
			_channel.close(true);
		} catch( Throwable t) {
			log.error( t );
		}
		
		log.debug( "finished" );

	}

	
	public static void handleConnection( Channel channel, ChannelEndpoint channelEndpoint ) {
		
		log.enteredMethod();
		
		ChannelHandler channelHandler = new ChannelHandler(channel,channelEndpoint);
		
		Thread thread = new Thread(channelHandler, "ChannelHandler." + _threadId++);
		
		thread.start();
	}

	
}
