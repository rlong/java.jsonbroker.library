// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.security;

import jsonbroker.library.common.http.headers.request.Authorization;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.server.http.HttpErrorHelper;

public class Subject  {
	
	public static final String TEST_USER = "test";
	public static final String TEST_REALM = "test";
	public static final String TEST_PASSWORD = "12345678";

	public static final Subject TEST = new Subject( TEST_USER, TEST_REALM, TEST_PASSWORD, "Test User");

	private static final Log log = Log.getLog(Subject.class);
	
	////////////////////////////////////////////////////////////////////////////
    //
    private String _ha1;

	////////////////////////////////////////////////////////////////////////////
	//
	String _username;

	public String getUsername() {
		return _username;
	}

	public void setUsername( String username ) {
		_username = username;
	}

	////////////////////////////////////////////////////////////////////////////
	//
    String _realm;

	public String getRealm() {
		return _realm;
	}

	////////////////////////////////////////////////////////////////////////////
	//
	String _password;

	String getPassword() {
		return _password;
	}

	void setPassword( String password ) {
		_password = password;
	}

	////////////////////////////////////////////////////////////////////////////
	//
	String _label;

	public String getLabel() {
		return _label;
	}

	void setLabel( String label ) {
		_label = label;
	}

	////////////////////////////////////////////////////////////////////////////
	//
    private long _born;

    public long getBorn() {
		return _born;
	}

	////////////////////////////////////////////////////////////////////////////
	//
    public Subject( String userName, String usersRealm, String password, String label  ) {
    	
    	super();
    	
    	_username = userName;
    	_realm = usersRealm;
    	_password = password;
    	_label = label;

        //_born = DateTime.Now;
        _born = System.currentTimeMillis();

    }
    

	////////////////////////////////////////////////////////////////////////////
    // sections 3.2.2.1-3.2.2.3 of RFC-2617
    public String getHa1()
    {
        if (null == _ha1)
        {

            String a1 = String.format("%s:%s:%s", _username, _realm, _password);

            _ha1 = SecurityUtilities.md5HashOfString(a1);

            log.debug(_ha1, "_ha1");
        }
        return _ha1;
    }


    public void validateAuthorizationRequestHeader(Authorization authorizationRequestHeader)
    {
        String realm = authorizationRequestHeader.getRealm();

        if (null == realm)
        {
        	log.error( "null == realm" );
        	
            throw HttpErrorHelper.unauthorized401FromOriginator(this);
        }

        if (!_realm.equals(realm))
        {
        	log.errorFormat( "!_usersRealm.equals(realm); _usersRealm = '%s'; realm = '%s'", _realm, realm );
        	
            throw HttpErrorHelper.unauthorized401FromOriginator(this);
        }

        // username ...
        String username = authorizationRequestHeader.getUsername();

        if (null == username)
        {
        	log.error( "null == username" );
        	
            throw HttpErrorHelper.unauthorized401FromOriginator(this);
        }
        else if (!_username.equals(username)) // someone is switching user names 
        {
        	log.errorFormat( "!_username.equals(username); _username = '%s'; username = '%s'", _username, username );
        	
            throw HttpErrorHelper.unauthorized401FromOriginator(this);
        }
    }

}
