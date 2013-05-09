package jsonbroker.library.common.http.headers;

import java.util.HashMap;

import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.log.Log;

public class MediaType {
	
	private static final Log log = Log.getLog(MediaType.class);
	
    ///////////////////////////////////////////////////////////////////////
    // type
    private String _type;

    public String getType() {
        return _type;
    }

    ///////////////////////////////////////////////////////////////////////
    // subtype
    private String _subtype;

    public String getSubtype() {
        return _subtype;
    }

    ///////////////////////////////////////////////////////////////////////
    // parameters
    private HashMap<String,String> _parameters;


    ///////////////////////////////////////////////////////////////////////
    // toString
    private String _toString;

    ///////////////////////////////////////////////////////////////////////
    // 
    private MediaType(String stringValue)
    {
        _toString = stringValue;
        _parameters = new HashMap<String, String>();
    }

    @Override
    public String toString()
    {
        return _toString;
    }

    public static MediaType buildFromString(String value) {
    	
    	
        int indexOfSlash = value.indexOf("/");
        
        if (-1 == indexOfSlash)
        {
            BaseException e = new BaseException(MediaType.class, "-1 == indexOfSlash; value = '%s'", value);
            throw e;
        }
        
        
        MediaType answer = new MediaType(value);
        
        String type = value.substring(0, indexOfSlash); // int beginIndex, int endIndex
        log.debug(type, "type");        
        answer._type = type;
        
        String remainder = value.substring(indexOfSlash+1);
        log.debug(remainder, "remainder");
        
        String subtype;
        {
        	int indexOfSemiColon = remainder.indexOf(";");
        	if (-1 == indexOfSemiColon) {
        		subtype = remainder.trim();
        	} else {
        		subtype = remainder.substring(0,indexOfSemiColon);  // int beginIndex, int endIndex
        		ParametersScanner scanner = new ParametersScanner(indexOfSemiColon, remainder);
        		String attribute;
        		while (null != (attribute = scanner.nextAttribute())) {
        			log.debug(attribute, "attribute");
        			String attributeValue = scanner.nextValue();
        			log.debug(attributeValue, "attributeValue");
        			answer._parameters.put( attribute, attributeValue);
        		}
        	}
        }
        
        log.debug(subtype, "subtype");
        answer._subtype = subtype;

    	return answer;
    }
    
    
    public String getParamaterValue(String key, String defaultValue) {
    	
    	String answer = _parameters.get( key );
    	
    	if( null == answer ) {
    		return defaultValue;    		
    	}
    	
    	return answer;
    	
    }
    
    

	
	

}
