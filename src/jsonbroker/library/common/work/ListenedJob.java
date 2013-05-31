package jsonbroker.library.common.work;

import jsonbroker.library.common.exception.BaseException;

public class ListenedJob implements Job {

	
    ////////////////////////////////////////////////////////////////////////////
    // delegate
    private Job _delegate;
    
    ////////////////////////////////////////////////////////////////////////////
    // listener
    private JobListener _listener;


    public ListenedJob( Job delegate, JobListener listener ) {
    	
    	_delegate = delegate;
    	_listener = listener;
    }

    
	@Override
	public void execute() {
		
		try {
			_delegate.execute();			
			_listener.jobCompleted( _delegate );
			
		} catch( BaseException e ) {
			_listener.jobFailed( _delegate, e );
		}
	}
	
}


