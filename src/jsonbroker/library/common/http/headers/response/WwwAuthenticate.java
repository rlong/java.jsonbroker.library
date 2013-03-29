// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.http.headers.response;

import jsonbroker.library.common.http.headers.AuthenticationHeaderScanner;
import jsonbroker.library.common.http.headers.HttpHeader;
import jsonbroker.library.common.log.Log;

//see section 3.2.1 of RFC-2617
public class WwwAuthenticate implements HttpHeader {

	private static final Log log = Log.getLog(WwwAuthenticate.class);

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

	protected void setQop(String qop) {
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
    
    
    public WwwAuthenticate()
    {
        
        _qop = "auth,auth-int";
    }

    public static WwwAuthenticate buildFromString(String challenge)
    {
        WwwAuthenticate answer = new WwwAuthenticate();

        AuthenticationHeaderScanner authenticationHeaderScanner = new AuthenticationHeaderScanner(challenge);
        authenticationHeaderScanner.scanPastDigestString();
        String name = authenticationHeaderScanner.scanName();

        while (null != name) 
        {
            if ("nonce".equals(name))
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
                String value = authenticationHeaderScanner.scanQuotedValue();
                answer._qop = value;

            }
            else if ("realm".equals(name))
            {
                String value = authenticationHeaderScanner.scanQuotedValue();
                answer._realm = value;

            }
            else
            {
                // 'auth-param' in section 3.2.1 of RFC-2617 says ... 
                // Any unrecognized directive MUST be ignored.

                String value = authenticationHeaderScanner.scanValue();

                String warning = String.format("unrecognised name-value pair. name = '%s', value = '%s'", name, value);
                log.warn(warning);

            }
            name = authenticationHeaderScanner.scanName();

        }

        return answer;
    }

    
    public String getName() {
    	return "WWW-Authenticate";
    }
    
    public String getValue() {
        /*
    	WWW-Authenticate: Digest
    	realm="testrealm@host.com", 
    	qop="auth,auth-int", 
    	nonce="dcd98b7102dd2f0e8b11d0f600bfb0c093", 
    	opaque="5ccc069c403ebaf9f0171e9517f40e41"
    	*/

        String answer = String.format("Digest realm=\"%s\", nonce=\"%s\", opaque=\"%s\", qop=\"%s\"", _realm, _nonce, _opaque, _qop) ;

        return answer;
    }
    

	
}
