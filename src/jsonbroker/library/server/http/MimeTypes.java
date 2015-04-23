// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.http;

import java.util.HashMap;

public class MimeTypes {

	
	private static final HashMap<String, String> MIME_TYPES;
	
	public static String APPLICATION_JSON = "application/json";
	
	public static String IMAGE_JPEG = "image/jpeg";
	
	static {
		MIME_TYPES = new HashMap<String, String>();
		
		MIME_TYPES.put( ".css", "text/css");
		MIME_TYPES.put( ".eot", "application/vnd.ms-fontobject");
		MIME_TYPES.put( ".html", "text/html");
		MIME_TYPES.put( ".gif", "image/gif");
		MIME_TYPES.put( ".ico", "image/x-icon");
		MIME_TYPES.put( ".jpeg", IMAGE_JPEG);
		MIME_TYPES.put( ".jpg", IMAGE_JPEG);
		MIME_TYPES.put( ".js", "application/javascript");
		MIME_TYPES.put( ".json", APPLICATION_JSON );
		MIME_TYPES.put( ".map", APPLICATION_JSON ); // http://stackoverflow.com/questions/19911929/what-mime-type-should-i-use-for-source-map-files
		MIME_TYPES.put( ".png", "image/png");
		MIME_TYPES.put( ".svg", "image/svg+xml"); // http://www.ietf.org/rfc/rfc3023.txt, section 8.19
		MIME_TYPES.put( ".ts", "text/x.typescript"); // http://stackoverflow.com/questions/13213787/whats-the-mime-type-of-typescript
		MIME_TYPES.put( ".ttf", "application/x-font-ttf"); // http://symbolset.com/blog/properly-serve-webfonts/
		MIME_TYPES.put( ".woff", "application/x-font-woff"); // http://symbolset.com/blog/properly-serve-webfonts/
	}
	
	
	public static String getMimeTypeForPath( String path ) {
		
		
		int lastDot = path.lastIndexOf( '.' );
		
		if( -1 == lastDot ) {
			return null;
		}
		
		String extension = path.substring( lastDot );
		String answer = MIME_TYPES.get( extension );
		
		return answer;
		
	}

}
