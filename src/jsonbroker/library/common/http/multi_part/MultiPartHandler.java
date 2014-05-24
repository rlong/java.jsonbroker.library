// Copyright (c) 2014 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//


package jsonbroker.library.common.http.multi_part;

public interface MultiPartHandler {

	
    PartHandler foundPartDelimiter();

    void handleException(Exception e);

    void foundCloseDelimiter();

}
