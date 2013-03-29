// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.service.test.job;

import jsonbroker.library.common.log.Log;
import jsonbroker.library.common.work.Job;
import jsonbroker.library.server.broker.Service;
import jsonbroker.library.service.test.TestProxy;

public class PingJob implements Job {

	private static final Log log = Log.getLog( PingJob.class );
	
	
	private Service _service;
	
	
	public PingJob( Service service ) {
		_service = service;
	}
	
	
	public void execute() {

		
		TestProxy test = new TestProxy( _service );
		test.ping();
		
		log.info( "OK" );
		
	}
	

}
