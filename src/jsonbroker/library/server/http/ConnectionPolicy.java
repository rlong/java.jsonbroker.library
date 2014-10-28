package jsonbroker.library.server.http;

import java.net.Socket;

public interface ConnectionPolicy {
	
	
	// will return false if the socket fails to pass implemented policy
	public boolean accept( Socket clientSocket );
	

}
