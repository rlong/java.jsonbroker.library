// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.auxiliary;

import java.util.Random;

public class RandomUtilities {


    ////////////////////////////////////////////////////////////////////////////

    // The initial state (that is, the seed) is partially based on the current time of day in milliseconds (for android)
    static final Random _randy = new Random();

    ////////////////////////////////////////////////////////////////////////////

    
    public static void random( byte[] bytes ) {
    	_randy.nextBytes( bytes );
    }
    
    
    public static String generateUuid()
    {

        byte[] uuid = new byte[16];

        _randy.nextBytes( uuid );
        
        return StringHelper.toHexString( uuid );
    }

}
