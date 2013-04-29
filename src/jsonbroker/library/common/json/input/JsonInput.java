// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.common.json.input;


public interface JsonInput {
	

	public boolean hasNextByte();
	public byte nextByte();
	public byte currentByte();
	public String getPositionInformation();
	public MutableDataPool getMutableDataPool();


}
