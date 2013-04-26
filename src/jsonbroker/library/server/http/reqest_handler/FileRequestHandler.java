// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.http.reqest_handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.http.Entity;
import jsonbroker.library.common.http.HttpStatus;
import jsonbroker.library.common.http.StreamEntity;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.server.http.HttpErrorHelper;
import jsonbroker.library.server.http.HttpRequest;
import jsonbroker.library.server.http.HttpResponse;
import jsonbroker.library.server.http.MimeTypes;
import jsonbroker.library.server.http.RequestHandler;

public class FileRequestHandler implements RequestHandler {
	
	private static Log log = Log.getLog(FileRequestHandler.class);


	////////////////////////////////////////////////////////////////////////////
	private String _rootFolder;

	////////////////////////////////////////////////////////////////////////////

	public FileRequestHandler( String rootFolder ) {		
		_rootFolder = rootFolder;		
		
	}
	
	
	private static void validateRequestUri( String requestUri ) {
		
		log.debug( requestUri, "requestUri");
		
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
	
	
	private static void validateMimeTypeForRequestUri( String requestUri ) {
		
		if( null == MimeTypes.getMimeTypeForPath( requestUri ) ) {
			
			log.errorFormat( "null == getMimeTypeForRequestUri( requestUri ); requestUri = '%s'", requestUri ); 
			throw HttpErrorHelper.forbidden403FromOriginator( FileRequestHandler.class );
		}
	}
	
	public Entity readFile( String relativePath  ) {

		// for windows ... replace the forward slashes with back-slashes to make a file name
        relativePath = relativePath.replace('/', '\\');

        String absoluteFilename = _rootFolder + relativePath;
        
        log.debug( absoluteFilename, "absoluteFilename");
        
        File file = new File( absoluteFilename );
        
        if( !file.exists() ) {
        	
        	log.errorFormat( "!file.exists(); absoluteFilename = '%s'" , absoluteFilename);
        	throw HttpErrorHelper.notFound404FromOriginator( this );
        }
        
        if( !file.canRead() ) {
        	log.errorFormat( "!file.canRead(); absoluteFilename = '%s'" , absoluteFilename);
        	throw HttpErrorHelper.forbidden403FromOriginator( this);
        }

        int length = (int) file.length();
        log.debug( length, "length");
        
        FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream( file );
		} catch (FileNotFoundException e) {
			throw new BaseException(this, e);
		}
        Entity answer = new StreamEntity( length, fileInputStream );
        return answer;
        
	}
	
	
	public HttpResponse processRequest(HttpRequest request) {
		
		
		String requestUri = request.getRequestUri();
		
		if( requestUri.endsWith( "/" ) ) {
			requestUri = requestUri + "index.html";
		}
		
		
		{ // some validation 
			
			validateRequestUri( requestUri );
			validateMimeTypeForRequestUri( requestUri );			
		}

        try
        {
        	
        	Entity body = readFile( requestUri );        	
            HttpResponse answer = new HttpResponse( HttpStatus.OK_200, body );
            String contentType = MimeTypes.getMimeTypeForPath(requestUri);
            answer.setContentType( contentType );
            return answer;
            
        } catch( BaseException e ) {
        	throw e;
        } catch( Throwable t ) {        	
        	log.error(t.getMessage(), "t.getMessage()" ); 
        	throw HttpErrorHelper.notFound404FromOriginator(this);        	
        }
        		
	}

	@Override
	public String getProcessorUri() {
		
		return "/";
	}


}
