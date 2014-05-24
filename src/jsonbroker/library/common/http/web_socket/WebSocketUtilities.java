package jsonbroker.library.common.http.web_socket;

import jsonbroker.library.common.security.SecurityUtilities;

public class WebSocketUtilities {

	
	public static String buildSecWebSocketAccept( String secWebSocketKey ) {
		
		String magic= "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
		String sha1Input = secWebSocketKey + magic;
		byte[] sha1Bytes = SecurityUtilities.sha1HashOfString( sha1Input );
		
		String answer = SecurityUtilities.base64Encode( sha1Bytes );    	
    	return answer;
	}

}
