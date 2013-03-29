// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.client.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import jsonbroker.library.common.auxiliary.StreamUtilities;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.http.Entity;
import jsonbroker.library.common.http.HttpStatus;
import jsonbroker.library.common.http.StreamEntity;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.common.net.NetworkAddress;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;



public class HttpDispatcher {

    private static Log log = Log.getLog(HttpDispatcher.class);

    
    ////////////////////////////////////////////////////////////////////////////
    NetworkAddress _networkAddress;
    
    ////////////////////////////////////////////////////////////////////////////
    HttpClient _client;

    ////////////////////////////////////////////////////////////////////////////
    public HttpDispatcher(NetworkAddress networkAddress ) {
    	
    	_networkAddress = networkAddress;
    	    	
    	/*
    	  with ... 
    	  _client = new DefaultHttpClient();
    	  ... we get the following error ... 
    	  "Invalid use of SingleClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one."
    	  ... using a thread safe connecion manager ... 
    	  * http://hc.apache.org/httpcomponents-client-ga/tutorial/html/connmgmt.html
    	  * http://thinkandroid.wordpress.com/2009/12/31/creating-an-http-client-example/ 
    	 */
    	
        //sets up parameters
        HttpParams params = new BasicHttpParams();
        
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "utf-8");
        params.setBooleanParameter("http.protocol.expect-continue", false);
        
        // timeouts ... 
        HttpConnectionParams.setConnectionTimeout( params, 5 * 1000);
        HttpConnectionParams.setSoTimeout( params, 5 * 1000);

        //registers schemes for both http and https
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        final SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
        sslSocketFactory.setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        registry.register(new Scheme("https", sslSocketFactory, 443));

        ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, registry);
        _client = new DefaultHttpClient(manager, params);
        
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    
    
    private static HashMap<String, String> getHeaders(  org.apache.http.HttpResponse apacheResponse ) {
    	
    	HashMap<String, String> answer = new HashMap<String, String>();
    	
		for( HeaderIterator headerIterator = apacheResponse.headerIterator();  headerIterator.hasNext(); ) {
			Header apacheHeader = headerIterator.nextHeader();
			
			if( Log.isDebugEnabled() ) {
				if( "WWW-Authenticate".equals( apacheHeader.getName()) ) {
					log.debug( apacheHeader.getValue(), apacheHeader.getName());
				}
				if( "Authentication-Info".equals( apacheHeader.getName()) ) {
					log.debug( apacheHeader.getValue(), apacheHeader.getName());
				}
			}
			
			answer.put( apacheHeader.getName().toLowerCase(), apacheHeader.getValue());
			
		}
		return answer;
    }
    
    // can return null 
    private static InputStream getInputStream( HttpEntity apacheEntity ) {
    	
    	if( null == apacheEntity ) {
    		return null;
    	}
    	
		try {
			return apacheEntity.getContent();
		} catch (IllegalStateException e) {
			throw new BaseException( HttpDispatcher.class, e);
		} catch (IOException e) {
			throw new BaseException( HttpDispatcher.class, e);
		}

    }
    
    // authenticator can be null 
    private int dispatch( HttpRequestBase apacheRequest, Authenticator authenticator, HttpResponseHandler responseHandler ) {
    	
    	org.apache.http.HttpResponse apacheResponse = null;
		try {
			apacheResponse = _client.execute( apacheRequest );
		} catch (ClientProtocolException e) {
			throw new BaseException( this, e);
		} catch (IOException e) {
			throw new BaseException( this, e);
		}
		
		HttpEntity apacheEntity = apacheResponse.getEntity();
		InputStream apacheEntityInputStream = getInputStream( apacheEntity );

		
		try {
			HashMap<String, String> responseHeaders = getHeaders( apacheResponse );
			if( null != authenticator ) { 
				authenticator.handleHttpResponseHeaders( responseHeaders );
			}
			
			
			int statusCode = apacheResponse.getStatusLine().getStatusCode();
			
	    	if( statusCode >= 200 && statusCode < 300 ) {
	    		// all is well 
	    		if( null == apacheEntityInputStream ) { // e.g. http 204 
	    			responseHandler.handleResponseEntity( responseHeaders, null );
	    		} else {
		    		StreamEntity responseEntity = new StreamEntity(apacheEntity.getContentLength(), apacheEntityInputStream);
		    		responseHandler.handleResponseEntity( responseHeaders, responseEntity );
	    		}	    		
	    	}
			return statusCode;
			
		} finally {

			// vvv http://hc.apache.org/httpcomponents-client-ga/httpclient/apidocs/org/apache/http/client/HttpClient.html
			if( null != apacheEntityInputStream ) {
				// Closing the input stream will trigger connection release
				StreamUtilities.close( apacheEntityInputStream, false, this);
			}
			// ^^^ http://hc.apache.org/httpcomponents-client-ga/httpclient/apidocs/org/apache/http/client/HttpClient.html
			
		}
    }
    


    
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    
    
    // authenticator can be null 
    private HttpRequestBase buildGetRequest( HttpRequestAdapter requestAdapter, Authenticator authenticator ) {
    	
    	String requestUri = requestAdapter.getRequestUri();
    	
    	String host = _networkAddress.getHostAddress();
    	int port =  _networkAddress.getPort();
    	
    	String uri = String.format( "http://%s:%d%s", host, port, requestUri );
    	log.debug( uri, "uri" );

    	HttpGet answer = new HttpGet( uri );

		// extra headers ... 
		{
			HashMap<String, String> requestHeaders = requestAdapter.getRequestHeaders();
			for (Map.Entry<String, String> item : requestHeaders.entrySet()) {				
				answer.setHeader( item.getKey(), item.getValue());				
			}
		}

    	if( null != authenticator ) { 
        	String authorization = authenticator.getRequestAuthorization( answer.getMethod(), requestUri, null );
        	log.debug( authorization, "authorization" );
        	if( null != authorization ) {
        		answer.addHeader( "Authorization", authorization);
        	}
    	}

		return answer;
    }
    
    
    

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    
    
    public void get( HttpRequestAdapter requestAdapter, Authenticator authenticator, HttpResponseHandler responseAdapter ) {
    	

    	HttpRequestBase apacheRequest = buildGetRequest( requestAdapter, authenticator);
    	int statusCode = dispatch( apacheRequest, authenticator, responseAdapter);
    	
    	if( 401 == statusCode  ) {
    		apacheRequest = buildGetRequest( requestAdapter, authenticator);
    		statusCode = dispatch( apacheRequest, authenticator, responseAdapter);
    	}
    	
    	if( statusCode < 200 || statusCode > 299 ) {
    		BaseException e = new BaseException( this, HttpStatus.getReason( statusCode ) );
    		e.setFaultCode( statusCode );
        	String requestUri = requestAdapter.getRequestUri();
    		e.addContext( "requestUri" , requestUri);
    		throw e;
    	}
    }

    public void get( HttpRequestAdapter requestAdapter,  HttpResponseHandler responseAdapter) {
    	
    	
    	HttpRequestBase apacheRequest = buildGetRequest( requestAdapter, null );
    	
    	int statusCode = dispatch( apacheRequest, null, responseAdapter);
    	if( statusCode < 200 || statusCode > 299 ) {
    		BaseException e = new BaseException( this, HttpStatus.getReason( statusCode ) );
    		e.setFaultCode( statusCode );
        	String requestUri = requestAdapter.getRequestUri();
    		e.addContext( "requestUri" , requestUri);
    		throw e;
    	}
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    
    // authenticator can be null 
    private HttpRequestBase buildPostRequest( HttpRequestAdapter requestAdapter, Authenticator authenticator ) {
    	
    	String requestUri = requestAdapter.getRequestUri();
    	Entity entity = requestAdapter.getRequestEntity();


    	String host = _networkAddress.getHostAddress();
    	int port =  _networkAddress.getPort();
    	
    	String uri = String.format( "http://%s:%d%s", host, port, requestUri );
    	log.debug( uri, "uri" );
    	
		HttpPost answer = new HttpPost( uri );

		// extra headers ... 
		{
			HashMap<String, String> requestHeaders = requestAdapter.getRequestHeaders();
			for (Map.Entry<String, String> item : requestHeaders.entrySet()) {				
				answer.setHeader( item.getKey(), item.getValue());				
			}
		}
		
		// auth headers ... 
		if( null != authenticator ) {
	    	String authorization = authenticator.getRequestAuthorization( answer.getMethod(), requestUri, entity );
	    	log.debug( authorization, "authorization" );
	    	if( null != authorization ) {
	    		answer.addHeader( "Authorization", authorization);
	    	}
		}

		InputStreamEntity inputStreamEntity = new InputStreamEntity( entity.getContent(), entity.getContentLength() );
		answer.setEntity( inputStreamEntity);		


		return answer;
    	
    }

    
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////

    public void post( HttpRequestAdapter requestAdapter, Authenticator authenticator, HttpResponseHandler responseAdapter ) {
    	
    	
    	HttpRequestBase apacheRequest = buildPostRequest( requestAdapter, authenticator);
    	int statusCode = dispatch( apacheRequest, authenticator, responseAdapter );
    	
    	if( 401 == statusCode ) {
    		apacheRequest = buildPostRequest( requestAdapter, authenticator);
    		statusCode = dispatch( apacheRequest, authenticator, responseAdapter);
    	}
    	log.debug( statusCode, "statusCode" );
    	if( statusCode < 200 || statusCode > 299 ) {
    		BaseException e = new BaseException( this, HttpStatus.getReason( statusCode ) );
    		e.setFaultCode( statusCode );
    		String requestUri = requestAdapter.getRequestUri();
    		e.addContext( "requestUri" , requestUri);
    		throw e;
    	}
    }


    public void post( HttpRequestAdapter requestAdapter, HttpResponseHandler responseAdapter ) {
    	
    	HttpRequestBase apacheRequest = buildPostRequest( requestAdapter, null );
    	
    	int statusCode = dispatch( apacheRequest, null, responseAdapter );
    	log.debug( statusCode, "statusCode" );
    	
    	if( statusCode < 200 || statusCode > 299 ) {
    		BaseException e = new BaseException( this, HttpStatus.getReason( statusCode ) );
    		e.setFaultCode( statusCode );
    		String requestUri = requestAdapter.getRequestUri();
    		e.addContext( "requestUri" , requestUri);
    		throw e;
    	}
    }


}
