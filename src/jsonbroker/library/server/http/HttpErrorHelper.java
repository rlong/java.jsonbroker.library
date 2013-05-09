// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.http;

import jsonbroker.library.common.auxiliary.Data;
import jsonbroker.library.common.auxiliary.StringHelper;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.http.DataEntity;
import jsonbroker.library.common.http.Entity;
import jsonbroker.library.common.http.HttpStatus;

public class HttpErrorHelper {

	
	private static BaseException buildException( Object originatingObject, int statusCode ) {
		
		String reason = HttpStatus.getReason( statusCode );
		BaseException answer = new BaseException( originatingObject, reason);
		answer.setFaultCode( statusCode );
		return answer;
	}
	
	
	public static BaseException badRequest400FromOriginator(Object originatingObject) {
		return buildException( originatingObject, HttpStatus.BAD_REQUEST_400 );
	}
	
	public static BaseException unauthorized401FromOriginator(Object originatingObject) {
		
		BaseException answer = buildException( originatingObject, HttpStatus.UNAUTHORIZED_401 );
		answer.setErrorDomain( HttpStatus.ErrorDomain.UNAUTHORIZED_401 );
		return answer;
				
	}
	
	
	public static BaseException forbidden403FromOriginator(Object originatingObject) {
		return buildException( originatingObject, HttpStatus.FORBIDDEN_403 );
	}
	
	public static BaseException notFound404FromOriginator(Object originatingObject) {
		
		return buildException( originatingObject, HttpStatus.NOT_FOUND_404 );
				
	}
	
	public static BaseException requestEntityTooLarge413FromOriginator( Object originatingObject) { 
		
		return buildException( originatingObject, HttpStatus.REQUEST_ENTITY_TOO_LARGE_413 );
		
	}
	
	public static BaseException internalServerError500FromOriginator( Object originatingObject ) {
		return buildException( originatingObject, HttpStatus.INTERNAL_SERVER_ERROR_500 );
		
	}
	
	public static BaseException methodNotImplemented501FromOriginator(Object originatingObject) {
		return buildException( originatingObject, HttpStatus.NOT_IMPLEMENTED_501 );
		
	}

	private static Entity toEntity( int statusCode ) {
		
		String statusString = HttpStatus.getReason( statusCode );
    	
    	String responseTemplate = "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\"><html><head><title>%d %s</title></head><body><h1>%s</h1></body></html>";
    	    	
    	String messageBody = String.format(responseTemplate, statusCode, statusString, statusString);
    	
    	byte[] utf8Bytes = StringHelper.toUtfBytes( messageBody );
    	Data data = new Data(utf8Bytes);
    	
    	Entity answer = new DataEntity(data);
    	return answer;
		
	}

	
	public static HttpResponse toHttpResponse( Throwable t ) {
		
		
		int statusCode = HttpStatus.INTERNAL_SERVER_ERROR_500;
		
		if( t instanceof BaseException ) {
			BaseException baseException = (BaseException)t;
			
			int faultCode = baseException.getFaultCode();
			
			// does BaseException have what looks like a HTTP CODE ?  
			if( 0 < faultCode &&  faultCode < 1000 ) {
				statusCode = faultCode;
			}
		}
		
		Entity entity = toEntity( statusCode );
		
		HttpResponse answer = new HttpResponse( statusCode, entity );
		
		return answer;
	}
	

}
