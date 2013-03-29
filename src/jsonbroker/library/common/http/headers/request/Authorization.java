// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.http.headers.request;

import jsonbroker.library.common.http.headers.AuthenticationHeaderScanner;
import jsonbroker.library.common.http.headers.HttpHeader;
import jsonbroker.library.common.log.Log;

// as  3.2.1 of RFC-2617
public class Authorization implements HttpHeader {

	private static final Log log = Log.getLog(Authorization.class);

	
	
    ///////////////////////////////////////////////////////////////////////
    String _cnonce;


    public String getCnonce() {
		return _cnonce;
	}


	public void setCnonce(String cnonce) {
		_cnonce = cnonce;
	}


	///////////////////////////////////////////////////////////////////////
    long _nc;

	public long getNc() {
		return _nc;
	}


	public void setNc(long nc) {
		_nc = nc;
	}
	
    ///////////////////////////////////////////////////////////////////////
    String _nonce;

	public String getNonce() {
		return _nonce;
	}


	public void setNonce(String nonce) {
		_nonce = nonce;
	}

    ///////////////////////////////////////////////////////////////////////
    String _opaque;

	public String getOpaque() {
		return _opaque;
	}


	public void setOpaque(String opaque) {
		_opaque = opaque;
	}


    ///////////////////////////////////////////////////////////////////////
    String _qop;

	public String getQop() {
		return _qop;
	}


	public void setQop(String qop) {
		_qop = qop;
	}


    ///////////////////////////////////////////////////////////////////////
    String _realm;

	public String getRealm() {
		return _realm;
	}


	public void setRealm(String realm) {
		_realm = realm;
	}

    ///////////////////////////////////////////////////////////////////////
    String _response;

	public String getResponse() {
		return _response;
	}


	public void setResponse(String response) {
		_response = response;
	}

    ///////////////////////////////////////////////////////////////////////
    String _uri;

	public String getUri() {
		return _uri;
	}


	public void setUri(String uri) {
		_uri = uri;
	}

    ///////////////////////////////////////////////////////////////////////
    String _username;

	public String getUsername() {
		return _username;
	}


	public void setUsername(String username) {
		_username = username;
	}


    ///////////////////////////////////////////////////////////////////////

    public Authorization()
    {
        _qop = "auth-int";
    }

    
    public static Authorization buildFromString( String credentials ) {

        Authorization answer = new Authorization();

        AuthenticationHeaderScanner authenticationHeaderScanner = new AuthenticationHeaderScanner(credentials);

        authenticationHeaderScanner.scanPastDigestString();

        String name = authenticationHeaderScanner.scanName();

        while (null != name)
        {
            if ("cnonce".equals(name))
            {
                String value = authenticationHeaderScanner.scanQuotedValue();

                answer._cnonce = value;
            }
            else if ("nc".equals(name))
            {
                long value = authenticationHeaderScanner.scanHexUInt32();
                answer._nc = value;
            }
            else if ("nonce".equals(name))
            {
                String value = authenticationHeaderScanner.scanQuotedValue();

                answer._nonce = value;

            }
            else if ("opaque".equals(name))
            {
                String value = authenticationHeaderScanner.scanQuotedValue();

                answer._opaque = value;
            }
            else if ("qop".equals(name))
            {
                String value = authenticationHeaderScanner.scanValue();
                answer._qop = value;
            }
            else if ("realm".equals(name))
            {
                String value = authenticationHeaderScanner.scanQuotedValue();
                answer._realm = value;

            }
            else if ("response".equals(name))
            {
                String value = authenticationHeaderScanner.scanQuotedValue();
                answer._response = value;
            }
            else if ("uri".equals(name))
            {
                String value = authenticationHeaderScanner.scanQuotedValue();
                answer._uri = value;
            }
            else if ("username".equals(name))
            {

                String value = authenticationHeaderScanner.scanQuotedValue();
                log.debug(value, "value");
                answer._username = value;
            }
            else
            {
                // 'auth-param' is permitted according to 3.2.2 of RFC-2617
                // 'auth-param' in section 3.2.1 of RFC-2617 says ... 
                // Any unrecognized directive MUST be ignored.
                // 

                String value = authenticationHeaderScanner.scanValue();
                String warning = String.format("unrecognised name-value pair. name = '%s', value = '%s'", name, value);
                log.warn(warning);

            }

            name = authenticationHeaderScanner.scanName();

        }

        return answer;

    }
    
    
    
    
    
    public String toString()
    {

        String answer = String.format("Digest username=\"%s\", realm=\"%s\", nonce=\"%s\", uri=\"%s\", response=\"%s\", cnonce=\"%s\", opaque=\"%s\", qop=%s, nc=%08x", _username, _realm, _nonce, _uri, _response, _cnonce, _opaque, _qop, _nc);
        return answer;
    }
    
    
	public String getName() {
		return "Authorization";
	}
	
	public String getValue() {
		return this.toString();
	}




}
