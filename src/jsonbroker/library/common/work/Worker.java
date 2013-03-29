// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.work;

import jsonbroker.library.common.log.Log;

public class Worker implements Runnable {

	private static final Log log = Log.getLog( Worker.class );
	

	////////////////////////////////////////////////////////////////////////////
	// NSString* _name;
	String _name;

	////////////////////////////////////////////////////////////////////////////
	WorkQueue _workQueue;

	////////////////////////////////////////////////////////////////////////////

	public Worker(String name, WorkQueue workQueue) {
		
		_name = name;
		_workQueue = workQueue;
		
	}

	////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void run() {

		log.enteredMethod();
		
		try { 
			while( true ) {
				
				Job job = _workQueue.dequeue();
				try {
					job.execute();
				} catch( Throwable t ) {
					log.error(t);
				}
			}
		}
		finally {
			log.info("leaving");
		}
	}
	
	void start() {
		Thread t = new Thread( this, _name );
		t.start();
	}

}
