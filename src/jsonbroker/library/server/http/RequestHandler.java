// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.http;

public interface RequestHandler {
	
	public String getProcessorUri();
	
	
	public HttpResponse processRequest(HttpRequest request);
	

}
