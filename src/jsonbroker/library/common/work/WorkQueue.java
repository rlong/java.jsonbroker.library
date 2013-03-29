// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.work;

import java.util.concurrent.LinkedBlockingQueue;

import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.log.Log;

public class WorkQueue {

	private static final Log log = Log.getLog( WorkQueue.class );

	private LinkedBlockingQueue<Job> _queue;
	
	public WorkQueue() { 
		
		_queue = new LinkedBlockingQueue<Job>();
		
	}
	
	
	public void enqueue( Job job ) {
		
		//log.enteredMethod();
		
		try {
			
			_queue.put( job );			
		} catch (InterruptedException e) {
			throw new BaseException( this, e);
		}
	}
	
	public Job dequeue() {
		
		//log.enteredMethod();
		
		Job answer;
		try {
			answer = _queue.take();
		} catch (InterruptedException e) {
			throw new BaseException( this, e);
		}
		
		return answer;
	}

}
