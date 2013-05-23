// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.work;

import jsonbroker.library.common.log.Log;

public class WorkManager {
		
	private static final Log log = Log.getLog( WorkManager.class );
		
	////////////////////////////////////////////////////////////////////////////	
	static WorkQueue _workQueue = new WorkQueue();
	
	//////////////////////////////////////////////////////////////////////////// 
	static Worker[] _workers;

	public static void start() { 
		
		log.enteredMethod();
		
		if( null != _workers )  {
			log.warn( "null != _workers" );
			return;
		}

		_workers = new Worker[5];
		for( int i = 0; i < _workers.length; i++ ) {
			
			String name = String.format( "worker.%d", i+1);
			
			_workers[i] = new Worker( name, _workQueue );
			_workers[i].start();
		}		
	}
	
	public static void enqueue( Job job ) {
		
		if( null == _workers )  {
			log.warn( "null == _workers" );
		}

		_workQueue.enqueue( job );
	}
	
	public static void enqueue( Job job, JobListener jobListener ) {
		
		if( null == _workers )  {
			log.warn( "null == _workers" );
		}
		
		ListenedJob listenedJob = new ListenedJob( job, jobListener );
		_workQueue.enqueue( listenedJob );
		
	}

}
