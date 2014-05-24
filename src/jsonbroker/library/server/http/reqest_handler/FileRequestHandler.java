// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.http.reqest_handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import jsonbroker.library.common.auxiliary.OperatingSystemUtilities;
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
		
		File root = new File( rootFolder );
		if( !root.exists() ) {
			log.warnFormat( "!root.exists(); rootFolder = %s", rootFolder);
		}
	}
	
	
	
	
	private File toFile( String  relativePath  ) {

		// for windows ... replace the forward slashes with back-slashes to make a file name		
		if( OperatingSystemUtilities.isWindows() ) {
			relativePath = relativePath.replace('/', '\\');
		}
        

        String absoluteFilename = _rootFolder + relativePath;
        
        log.debug( absoluteFilename, "absoluteFilename");
        
        File answer = new File( absoluteFilename );
        
        if( !answer.exists() ) {
        	
        	log.errorFormat( "!answer.exists(); absoluteFilename = '%s'" , absoluteFilename);
        	throw HttpErrorHelper.notFound404FromOriginator( this );
        }
        
        if( !answer.canRead() ) {
        	log.errorFormat( "!answer.canRead(); absoluteFilename = '%s'" , absoluteFilename);
        	throw HttpErrorHelper.forbidden403FromOriginator( this);
        }
        
        return answer;

	}
	

	private Entity readFile( File file  ) {


        int length = (int) file.length();
//        log.debug( length, "length");
        
		try {
			FileInputStream fileInputStream = new FileInputStream( file );
	        Entity answer = new StreamEntity( fileInputStream, length );
	        return answer;
		} catch (FileNotFoundException e) {
			log.errorFormat( "exception caught trying open file; file.getAbsolutePath() = '%s'; e.getMessage() = '%s'", file.getAbsolutePath(), e.getMessage());
			throw HttpErrorHelper.notFound404FromOriginator( this );
		}
	}
	
	
	public HttpResponse processRequest(HttpRequest request) {
		
		
		String requestUri = request.getRequestUri();
		
		if( requestUri.endsWith( "/" ) ) {
			requestUri = requestUri + "index.html";
		}
		
		requestUri = RequestHandlerHelper.removeUriParameters( requestUri );
		
		
		{ // some validation 
			
			RequestHandlerHelper.validateRequestUri( requestUri );
			RequestHandlerHelper.validateMimeTypeForRequestUri( requestUri );			
		}
		
		
    	File file = toFile( requestUri );
    	
    	String eTag = Long.toHexString( file.lastModified() );
    	
    	
    	
		

        try
        {
        	
        	Entity body = readFile( file );
        	
            HttpResponse answer = new HttpResponse( HttpStatus.OK_200, body );
            String contentType = MimeTypes.getMimeTypeForPath(requestUri);
            answer.setContentType( contentType );
            return answer;
            
        } catch( BaseException e ) {
        	throw e;
        } catch( Throwable t ) {        	
        	log.error(t.getMessage() ); 
        	throw HttpErrorHelper.notFound404FromOriginator(this);        	
        }
        		
	}

	@Override
	public String getProcessorUri() {
		
		return "/";
	}

}
