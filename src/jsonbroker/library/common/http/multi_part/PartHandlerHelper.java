// Copyright (c) 2014 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.http.multi_part;

import jsonbroker.library.common.http.headers.MediaType;


public class PartHandlerHelper {

	
    // can return null
    public static ContentDisposition getContentDisposition(String name, String value)
    {
        if ("content-disposition".equals(name))
        {
            return ContentDisposition.buildFromString(value);
        }

        return null;
    }


    // can return null
    public static MediaType getContentType(String name, String value)
    {
        if ("content-type".equals(name))
        {
            return MediaType.buildFromString(value);
        }

        return null;
    }

}
