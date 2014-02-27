package jsonbroker.library.server.http;

public class HttpMethod {

    public static final HttpMethod GET = new HttpMethod( "GET" );
    public static final HttpMethod POST = new HttpMethod( "POST" );
    public static final HttpMethod OPTIONS = new HttpMethod( "OPTIONS" );


    ///////////////////////////////////////////////////////////////////////
    // name
    private String _name;
    

    public String getName() {
		return _name;
	}


    private HttpMethod(String name)
    {
        _name = name;
    }
    
    
    public boolean matches(String method)
    {
        if( _name.equals( method ) )
        { 
            return true;
        }
        return false;
    }


}
