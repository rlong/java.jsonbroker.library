// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.client.channel;

import jsonbroker.library.broker.BrokerMessage;
import jsonbroker.library.broker.server.Service;
import jsonbroker.library.common.channel.Channel;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.json.JsonArray;
import jsonbroker.library.common.json.JsonArrayHelper;
import jsonbroker.library.common.log.Log;

public class ServiceChannelProxy implements Service {

	private static final byte[] NEWLINE = { '\n' };

	
	private static Log log = Log.getLog( ServiceChannelProxy.class );

	Channel _channel;
	
	
	public ServiceChannelProxy( Channel channel ) {
		_channel = channel;
	}
	
	public void close(boolean ignoreErrors) {
		_channel.close(ignoreErrors);
	}
	
	private void dispachRequest( BrokerMessage request ) {
		
		JsonArray requestArray = request.toJsonArray();
		byte[] endpointHeader = JsonArrayHelper.toBytes( requestArray );

		// channel header ...
		{
			String channelHeader = "[\"jsonbroker.JsonbrokerEndpoint\",1,0,null,"+endpointHeader.length+"]\n";
			log.debug( channelHeader, "channelHeader"  );
			_channel.write( channelHeader );
		}
		
		// endpoint header ...
		{

			_channel.write( endpointHeader );
			_channel.write( NEWLINE );

		}
		_channel.flush();
	}
	
	private BrokerMessage readResponse() {
		
		String channelHeader = _channel.readLine();
		log.debug( channelHeader, "channelHeader"  );
		
        if (null == channelHeader)
        {
            String errorMessage = "null == channelHeader; channel looks like it is closed";
            throw new BaseException(this, errorMessage);
        }

		
		String endpointHeader = _channel.readLine();
		log.debug( endpointHeader, "endpointHeader" );
		
		JsonArray brokerMessageArray = JsonArrayHelper.fromString( endpointHeader );
		return new BrokerMessage( brokerMessageArray ); 

	}

	@Override
	public BrokerMessage process(BrokerMessage request) {
		
		dispachRequest( request );
		return readResponse();
	}

}
