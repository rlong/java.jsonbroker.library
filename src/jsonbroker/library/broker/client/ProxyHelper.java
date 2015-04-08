// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.broker.client;

import jsonbroker.library.client.http.Authenticator;
import jsonbroker.library.client.http.HttpDispatcher;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.common.net.NetworkAddress;
import jsonbroker.library.common.security.ClientSecurityConfiguration;

public class ProxyHelper  {

	Log log = Log.getLog( ProxyHelper.class );

	
	////////////////////////////////////////////////////////////////////////////
	//
	String _host;
	
	public String getHost() {
		return _host;
	}

	////////////////////////////////////////////////////////////////////////////
	//
	int _port;

	public int getPort() {
		return _port;
	}

	////////////////////////////////////////////////////////////////////////////
	//
	ServiceHttpProxy _openHttpProxy;
	
	////////////////////////////////////////////////////////////////////////////
	//
	ServiceHttpProxy _authHttpProxy;
	


	////////////////////////////////////////////////////////////////////////////
	//
	public ProxyHelper() {
	}

	public ServiceHttpProxy getOpenHttpProxy() {
		
		if( null == _host ) {
			BaseException e = new BaseException(this, "null == _host");
			throw e;
		}
		
		if( null != _openHttpProxy ) {
			return _openHttpProxy;
		}

		NetworkAddress networkAddress = new NetworkAddress( _host, _port );
		HttpDispatcher httpDispatcher = new HttpDispatcher( networkAddress );
		_openHttpProxy = new ServiceHttpProxy( httpDispatcher );

		return _openHttpProxy;
	}
	
	public ServiceHttpProxy getAuthHttpProxy(ClientSecurityConfiguration clientSecurityConfiguration) {
		
		if( null == _host ) {
			BaseException e = new BaseException(this, "null == _host");
			throw e;
		}
		
		if( null != _authHttpProxy && _authHttpProxy.getAuthenticator().getSecurityConfiguration() == clientSecurityConfiguration ) {
			return _authHttpProxy;
		}

		NetworkAddress networkAddress = new NetworkAddress( _host, _port );
		HttpDispatcher httpDispatcher = new HttpDispatcher( networkAddress );
		Authenticator authenticator = new Authenticator(false, clientSecurityConfiguration );			
		_authHttpProxy = new ServiceHttpProxy( httpDispatcher, authenticator );

		return _authHttpProxy;
	}
	
	////////////////////////////////////////////////////////////////////////////
	// username, realm, password can be null
	public void initialize( String host, int port ) {
		
		_host = host;
		_port = port;

		_openHttpProxy = null;
		_authHttpProxy = null;
	}
	

}
