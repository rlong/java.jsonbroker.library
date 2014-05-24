// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.http.reqest_handler;

import jsonbroker.library.common.log.Log;
import jsonbroker.library.server.http.HttpErrorHelper;
import jsonbroker.library.server.http.MimeTypes;

public class RequestHandlerHelper {

	private static Log log = Log.getLog(RequestHandlerHelper.class);

	public static void validateRequestUri( String requestUri ) {
		
//		log.debug( requestUri, "requestUri");
		
		if( '/' != requestUri.charAt(0 ) ) {
			
			log.errorFormat( "'/' != requestUri.charAt(0); requestUri = '%s'", requestUri);
			throw HttpErrorHelper.forbidden403FromOriginator( FileRequestHandler.class );
		}
		
		if( -1 != requestUri.indexOf( "/.") ) { // UNIX hidden files
			
			log.errorFormat( "-1 != requestUri.indexOf( \"/.\"); requestUri = '%s'", requestUri);
			
			throw HttpErrorHelper.forbidden403FromOriginator( FileRequestHandler.class );
			
		}
		
		if( -1 != requestUri.indexOf( "..") ) { // parent directory
			
			log.errorFormat( "-1 != requestUri.indexOf( \"..\"); requestUri = '%s'", requestUri);
			throw HttpErrorHelper.forbidden403FromOriginator( FileRequestHandler.class );
			
		}
		
	}

	public static void validateMimeTypeForRequestUri( String requestUri ) {
		
		
		
		if( null == MimeTypes.getMimeTypeForPath( requestUri ) ) {
			
			log.errorFormat( "null == getMimeTypeForRequestUri( requestUri ); requestUri = '%s'", requestUri ); 
			throw HttpErrorHelper.forbidden403FromOriginator( FileRequestHandler.class );
		}
	}
	
	public static String removeUriParameters( String uri ) {
		
		int indexOfFirstQuestionMark = uri.indexOf( '?' );
		if( -1 != indexOfFirstQuestionMark ) {
			uri = uri.substring( 0, indexOfFirstQuestionMark);
		}
		
		return uri;

	}
	
	
}
