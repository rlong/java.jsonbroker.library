package jsonbroker.library.server.http;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public interface ConnectionDelegate {

	public ConnectionDelegate processRequest( Socket socket, InputStream inputStream, OutputStream outputStream);

}
