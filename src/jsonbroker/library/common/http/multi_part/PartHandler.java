// Copyright (c) 2014 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//


package jsonbroker.library.common.http.multi_part;

public interface PartHandler {

	
    void handleHeader(String name, String value);
    void handleBytes(byte[] bytes, int offset, int length);

    void handleException(Exception e);

    void partCompleted();

}
