// Copyright (c) 2014 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//


package jsonbroker.library.common.http.multi_part;

import java.util.HashMap;

import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.http.headers.HttpHeader;
import jsonbroker.library.common.http.headers.ParametersScanner;
import jsonbroker.library.common.log.Log;

public class ContentDisposition implements HttpHeader {

	private static final Log log = Log.getLog( ContentDisposition.class );
	
    ///////////////////////////////////////////////////////////////////////
    // dispExtensionToken ... can be null, see http://www.w3.org/Protocols/rfc2616/rfc2616-sec19.html#sec19.5.1
    private String _dispExtensionToken;

	public String getDispExtensionToken() {
		return _dispExtensionToken;
	}

    ///////////////////////////////////////////////////////////////////////
    // value
    private String _value;

	@Override
	public String getValue() {
		return _value;
	}

    ///////////////////////////////////////////////////////////////////////
    // dispositionParameters 
	private HashMap<String, String> _dispositionParameters;
	
    private ContentDisposition(String value)
    {
        _value = value;
        _dispositionParameters = new HashMap<String, String>();
    }

	@Override
	public String getName() {
        return "Content-Disposition";
	}
	
    public String getDispositionParameter(String parameterName, String defaultValue) {
    	
    	String answer = _dispositionParameters.get( parameterName );
    	
    	if( null == answer ) {
    		return defaultValue;
    	}
    	
    	return answer;
    }
    
    public static ContentDisposition buildFromString(String value) {
    	
        // see http://www.w3.org/Protocols/rfc2616/rfc2616-sec19.html#sec19.5.1 for rules

        ParametersScanner scanner = new ParametersScanner(0, value);
        String firstAttribute = scanner.nextAttribute();
        if (null == firstAttribute) {
            BaseException e = new BaseException(ContentDisposition.class, "null == firstAttribute; value = '%s'", value);
            throw e;
        }
        
        String dispExtensionToken = null;
        if ("attachment".equals(firstAttribute)) {
            // we leave _dispExtensionToken as null
        }
        else {
            dispExtensionToken = firstAttribute;            
        }

        ContentDisposition answer = new ContentDisposition(value);
        
        answer._dispExtensionToken = dispExtensionToken;

        for( String attribute = scanner.nextAttribute(); null != attribute; attribute = scanner.nextAttribute() ) {
        	
            log.debug(attribute, "attribute");
            String attributeValue = scanner.nextValue();
            log.debug(attributeValue, "attributeValue");
            answer._dispositionParameters.put(attribute, attributeValue);

        }

        return answer;
    	
    }





}
