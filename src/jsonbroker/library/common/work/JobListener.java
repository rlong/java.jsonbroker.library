// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.work;

import jsonbroker.library.common.exception.BaseException;

public interface JobListener {

	public void jobCompleted( Job job );
	public void jobFailed( Job job, BaseException exception );
	
		
}
