// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.http.headers.response;

import jsonbroker.library.common.http.headers.AuthenticationHeaderScanner;
import jsonbroker.library.common.http.headers.HttpHeader;
import jsonbroker.library.common.log.Log;

public class AuthenticationInfo implements HttpHeader {
	
	private static final Log log = Log.getLog(AuthenticationInfo.class);

	
    ///////////////////////////////////////////////////////////////////////
    String _cnonce;



    protected String getCnonce() {
		return _cnonce;
	}

	public void setCnonce(String cnonce) {
		_cnonce = cnonce;
	}



	///////////////////////////////////////////////////////////////////////
    String _nextnonce;

	public String getNextnonce() {
		return _nextnonce;
	}

	public void setNextnonce(String nextnonce) {
		_nextnonce = nextnonce;
	}


    ///////////////////////////////////////////////////////////////////////
    long _nc;

	protected long getNc() {
		return _nc;
	}

	public void setNc(long nc) {
		_nc = nc;
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
    String _rspauth;

	public String getRspauth() {
		return _rspauth;
	}

	public void setRspauth(String rspauth) {
		_rspauth = rspauth;
	}


    ///////////////////////////////////////////////////////////////////////

    public AuthenticationInfo()
    {
        _qop = "auth-int";
    }
    
    public static AuthenticationInfo buildFromString(String authInfo)
    {
        AuthenticationInfo answer = new AuthenticationInfo();
        AuthenticationHeaderScanner authenticationHeaderScanner = new AuthenticationHeaderScanner(authInfo);
        authenticationHeaderScanner.scanPastDigestString();
        String name = authenticationHeaderScanner.scanName();

        while (null != name)
        {
            if ("cnonce".equals(name))
            {
                String value = authenticationHeaderScanner.scanQuotedValue();
                answer._cnonce = value;
            }
            else if ("nextnonce".equals(name))
            {
                String value = authenticationHeaderScanner.scanQuotedValue();
                answer._nextnonce = value;
            }
            else if ("nc".equals(name))
            {
                long value = authenticationHeaderScanner.scanHexUInt32();
                answer._nc = value;
            }
            else if ("qop".equals(name))
            {
                String value = authenticationHeaderScanner.scanValue();
                answer._qop = value;
            }
            else if ("rspauth".equals(name))
            {
                String value = authenticationHeaderScanner.scanQuotedValue();
                answer._rspauth = value;

            } else {

                // 'auth-param' is not permitted according to 3.2.3 of RFC-2617
                // 'auth-param' in section 3.2.1 of RFC-2617 says ... 
                // Any unrecognized directive MUST be ignored.
                //
                // For consistency, we mimic the behaviour specified in sections 3.2.1 & 3.2.2 in relation to 'auth-param'

                String value = authenticationHeaderScanner.scanQuotedValue();

                String warning = String.format("unrecognised name-value pair. name = '%s', value = '%s'", name, value);
                log.warn(warning);

            }

            name = authenticationHeaderScanner.scanName();

        }

        return answer;

    }
    
    public String getName() {
		return "Authentication-Info";
	}
    
    public String getValue()
    {

        String answer = String.format("nextnonce=\"%s\", qop=%s, rspauth=\"%s\", cnonce=\"%s\", nc=%08x", _nextnonce, _qop, _rspauth, _cnonce, _nc);

        return answer;
    }



}
