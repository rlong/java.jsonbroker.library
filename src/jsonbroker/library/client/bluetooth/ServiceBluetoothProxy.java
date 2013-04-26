// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//
package jsonbroker.library.client.bluetooth;

import jsonbroker.library.common.bluetooth.BluetoothChannel;
import jsonbroker.library.common.broker.BrokerMessage;
import jsonbroker.library.common.json.JsonArray;
import jsonbroker.library.common.json.JsonArrayHelper;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.server.broker.Service;

public class ServiceBluetoothProxy implements Service {

	
	private static Log log = Log.getLog( ServiceBluetoothProxy.class );

	BluetoothChannel _bluetoothChannel;
	
	
	public ServiceBluetoothProxy( BluetoothChannel bluetoothChannel ) {
		_bluetoothChannel = bluetoothChannel;
	}
	

	@Override
	public BrokerMessage process(BrokerMessage request) {
		
		JsonArray requestArray = request.toJsonArray();

		String payload = JsonArrayHelper.toString( requestArray );
		
		String bluetoothRequest = "[\"jsonbroker\",1,0,null,2,"+payload.length()+"]\n{}\n";
		log.debug( bluetoothRequest, "bluetoothRequest"  );
		_bluetoothChannel.write( bluetoothRequest );

		log.debug( payload, "payload"  );
		_bluetoothChannel.writeLine( payload );

		
		String header = _bluetoothChannel.readLine();
		log.debug( header, "header"  );
		String meta = _bluetoothChannel.readLine();
		log.debug( meta, "meta"  );
		
		String responseString = _bluetoothChannel.readLine();
		log.debug( responseString, "responsePayload" );

		JsonArray responseArray = JsonArrayHelper.buildFromString( responseString );
		BrokerMessage answer = new BrokerMessage( responseArray );

		return answer;
	}

}
