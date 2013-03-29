// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.http;

import java.io.InputStream;
import java.util.StringTokenizer;

import jsonbroker.library.common.auxiliary.MutableData;
import jsonbroker.library.common.auxiliary.NumericUtilities;
import jsonbroker.library.common.auxiliary.StreamUtilities;
import jsonbroker.library.common.auxiliary.StringHelper;
import jsonbroker.library.common.http.Entity;
import jsonbroker.library.common.http.StreamEntity;
import jsonbroker.library.common.log.Log;

public class HttpRequestReader {

	private static final int LINE_LENGTH_UPPER_BOUND = 512;
	private static final int NUMBER_HEADERS_UPPER_BOUND = 32;
	
	private static final boolean[] INVALID_CHARS = new boolean[256];
	
	static {
		
		// valid chars are 'cr', 'nl', and all the chars between 'space' and '~' 
		for( int i = 0; i < 256; i++ ) {
			INVALID_CHARS[i] = true;
		}
		
		INVALID_CHARS[0x0d] = false; // 0x0d = 'cr'
		INVALID_CHARS[0x0a] = false; // 0x0a = 'nl'
		
		for( int i = 0x20; i <= 0x7e; i++ ) { // 0x20 = 'space'; 0x7e = '~'
			INVALID_CHARS[i] = false;
		}
	}

	private static final Log log = Log.getLog(HttpRequestReader.class);

	//private static void setOperationDetailsForRequest(HTTPRequest request, String line)
	private static void setOperationDetailsForRequest(HttpRequest request, String line) {
		
		//log.debug(line, "line");
		//log.debug(line, "line");
		
		// inherited from c-sharp ... not sure this will happen in the java world
		if (line == null) {
			
			log.error( "line == null");
			
			//throw HTTPException.badRequest400FromOriginator(typeof(HTTPRequestReader));
			throw HttpErrorHelper.badRequest400FromOriginator(HttpRequestReader.class);
			
			
		}
		
        // HTTP request lines are of the form:
        // [METHOD] [Encoded URL] HTTP/1.?
		//string[] tokens = line.Split(new char[] { ' ' });
		StringTokenizer tokenizer = new StringTokenizer( line );

		//if (tokens.Length != 3)
		if( 3 != tokenizer.countTokens() ) { 
			
			log.errorFormat("3 != tokenizer.countTokens(); tokenizer.countTokens() = %d; line = '%s'", tokenizer.countTokens(), line);
			
			//throw HTTPException.badRequest400FromOriginator(typeof(HTTPRequestReader));
			throw HttpErrorHelper.badRequest400FromOriginator(HttpRequestReader.class);
			
		}
		
        /*
         * HTTP method ... 
         */
		String method = tokenizer.nextToken();
		
		if( "GET".equals( method ) || "get".equals( method )) {
			request.setMethod( HttpRequest.METHOD_GET );
		} else if( "POST".equals( method ) || "post".equals( method )) {
			request.setMethod( HttpRequest.METHOD_POST );			
		} else { 
			
			log.errorFormat( "unknown HTTP method; method = '%s'; line = '%s'" , method, line );
			
			//throw HTTPException.methodNotImplemented501FromOriginator(typeof(HTTPRequestReader));
			throw HttpErrorHelper.methodNotImplemented501FromOriginator( HttpRequestReader.class);
		}
		
		
        /*
         * HTTP request-uri ... 
         */
		//String requestUri = tokens[1];
		String requestUri = tokenizer.nextToken();
		
		
		//log.debug(requestUri, "requestUri");
		//log.debug(requestUri, "requestUri");
		
		//request.requestUri = requestUri;
		request.setRequestUri( requestUri );
		
		
	}

	
	
	// null corresponds to the end of a stream
	private static String readLine(InputStream inputStream, MutableData buffer) {
		
		
		int byteRead = StreamUtilities.readByte(  inputStream, HttpRequestReader.class);
		
		if( -1 == byteRead ) { 
			return null;
		}
		
		int i = 0;
		
		do { 
			
			if( -1 != byteRead ) {
				
				if( INVALID_CHARS[ byteRead ] ) {
					
					log.errorFormat( "INVALID_CHARS[ byteRead ]; byteRead = 0x%x" , byteRead );
					
					// unexpected character
					throw HttpErrorHelper.badRequest400FromOriginator(HttpRequestReader.class);

				}
			}
			
			// end of stream or end of the line
			if( -1 == byteRead || '\n' == byteRead ) {
				return StringHelper.getUtf8String( buffer );
			}
			
			// filter out '\r'
			if( '\r' != byteRead ) {
				buffer.append( (byte)byteRead );
			}

			byteRead = StreamUtilities.readByte(  inputStream, HttpRequestReader.class);
			
			i++;
		} while( i < LINE_LENGTH_UPPER_BOUND );
		
		log.errorFormat( "line too long; i = %d", i);
		
		// line is too long
		throw HttpErrorHelper.badRequest400FromOriginator(HttpRequestReader.class);
		
	}
	
	
	private static void addHeader(String header, HttpRequest request) {
	
		String name;
		String value;
		
		int firstColon = header.indexOf(':');
		
		if (-1 == firstColon) {
			
			log.errorFormat( "-1 == firstColon; header = '%s'" , header);
			
			throw HttpErrorHelper.badRequest400FromOriginator(HttpRequestReader.class);
		}
		
		name = header.substring( 0, firstColon).toLowerCase(); // headers are case insensitive		
		value = header.substring(firstColon + 1).trim();
		
		if( Log.isDebugEnabled() ) {
			if( "authorization".equals( name ) ) {
				log.debug( value, name);
			}
		}
		
		request.setHttpHeader( name, value);
		
	}


	
	// null corresponds to the end of a stream
	public static HttpRequest readRequest(InputStream inputStream) {
		
		MutableData buffer = new MutableData();
		String firstLine = readLine(inputStream,buffer);
		
		log.debug(firstLine, "firstLine");
		
		// null corresponds to the end of a stream
		if( null == firstLine ) {
			return null;
		}
		
		HttpRequest answer = new HttpRequest();
		
		setOperationDetailsForRequest(answer, firstLine);
	
		int i = 0;
		do {
			buffer.clear();
			String line = readLine(inputStream,buffer);
			
			//log.debug( line, "line");
			
			if (0 == line.length()) {
				break;
			} else {
				addHeader(line, answer);
			}
			i++;
		} while( i < NUMBER_HEADERS_UPPER_BOUND );
		
		if( i > NUMBER_HEADERS_UPPER_BOUND ) {
			log.errorFormat( "i > NUMBER_HEADERS_UPPER_BOUND; i = %d", i);
			throw HttpErrorHelper.badRequest400FromOriginator(HttpRequestReader.class);
		}
		
		String contentLengthString = null;
		
		if( answer.getHeaders().containsKey( "content-length") ) {
			contentLengthString = answer.getHeaders().get( "content-length" );
		}

		// no body ?
		if (null == contentLengthString) {
			log.debug("null == contentLengthString");			
			return answer;
		}
		
		long contentLength = NumericUtilities.parseLong(contentLengthString);
		
		log.debug(contentLength, "contentLength");
		
		Entity body = new StreamEntity( contentLength, inputStream );
		answer.setEntity( body );

		return answer;
	}
	
}
