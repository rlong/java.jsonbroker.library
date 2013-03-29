// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.http.security;

import jsonbroker.library.common.log.Log;

public class HttpSecurityJanitor implements Runnable {

	private static final Log log = Log.getLog(HttpSecurityJanitor.class);

	
    ///////////////////////////////////////////////////////////////////////
	//
	HttpSecurityManager _httpSecurityManager;
	
    public HttpSecurityJanitor(HttpSecurityManager httpSecurityManager)
    {
        _httpSecurityManager = httpSecurityManager;
    }

	@Override
	public void run() {
		log.enteredMethod();
		
		try {
			while( true ) { 
				Thread.sleep( 2 * 60 * 1000 );
				_httpSecurityManager.runCleanup();
			}
		} catch (InterruptedException e) {
			log.error( e );
		}
	}

    public void start() {
    	
    	Thread t = new Thread(this);
    	t.setName( "HttpSecurityJanitor" );
    	t.start();
    	
    }


}
