package jsonbroker.library.common.http.web_socket;

import java.io.InputStream;
import java.io.OutputStream;

import jsonbroker.library.common.auxiliary.InputStreamHelper;
import jsonbroker.library.common.auxiliary.OutputStreamHelper;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.log.Log;

/* from http://tools.ietf.org/html/rfc6455#section-5.2 ...

      0                   1                   2                   3
      0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
     +-+-+-+-+-------+-+-------------+-------------------------------+
     |F|R|R|R| opcode|M| Payload len |    Extended payload length    |
     |I|S|S|S|  (4)  |A|     (7)     |             (16/64)           |
     |N|V|V|V|       |S|             |   (if payload len==126/127)   |
     | |1|2|3|       |K|             |                               |
     +-+-+-+-+-------+-+-------------+ - - - - - - - - - - - - - - - +
     |     Extended payload length continued, if payload len == 127  |
     + - - - - - - - - - - - - - - - +-------------------------------+
     |                               |Masking-key, if MASK set to 1  |
     +-------------------------------+-------------------------------+
     | Masking-key (continued)       |          Payload Data         |
     +-------------------------------- - - - - - - - - - - - - - - - +
     :                     Payload Data continued ...                :
     + - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - +
     |                     Payload Data continued ...                |
     +---------------------------------------------------------------+

   FIN:  1 bit

      Indicates that this is the final fragment in a message.  The first
      fragment MAY also be the final fragment.

   RSV1, RSV2, RSV3:  1 bit each

      MUST be 0 unless an extension is negotiated that defines meanings
      for non-zero values.  If a nonzero value is received and none of
      the negotiated extensions defines the meaning of such a nonzero
      value, the receiving endpoint MUST _Fail the WebSocket
      Connection_.
       
 */
public class Frame {
	
	private static Log log = Log.getLog(Frame.class);

	private static final int FIN_BIT = 0x80;
	
	private static final int MASK_BIT = 0x80;
	//private static final int FIN_MASK = 0xFE;

//	private static final int OPCODE_CONTINUATION_FRAME = 0x00;
	public static final int OPCODE_TEXT_FRAME = 0x01;	
//	private static final int OPCODE_BINARY_FRAME = 0x02;
//	private static final int OPCODE_BINARY_RESERVED_3 = 0x03;
//	private static final int OPCODE_BINARY_RESERVED_4 = 0x04;
//	private static final int OPCODE_BINARY_RESERVED_5 = 0x05;
//	private static final int OPCODE_BINARY_RESERVED_6 = 0x06;
//	private static final int OPCODE_BINARY_RESERVED_7 = 0x07;
	public static final int OPCODE_CONNECTION_CLOSE = 0x08;
//	private static final int OPCODE_PING = 0x09;	
	private static final int OPCODE_PONG = 0x0a;
//	private static final int OPCODE_BINARY_RESERVED_B = 0x0b;
//	private static final int OPCODE_BINARY_RESERVED_C = 0x0c;
//	private static final int OPCODE_BINARY_RESERVED_D = 0x0d;
//	private static final int OPCODE_BINARY_RESERVED_E = 0x0e;
//	private static final int OPCODE_BINARY_RESERVED_F = 0x0f;

	
	
	
	byte[] _maskingKey = null;

	int _opCode;

	int _payloadLength;
	
	
	public Frame( int opCode, int payloadLength ) {
		_opCode = opCode;
		_payloadLength = payloadLength;
		
	}

	
	
	public void applyMask( byte[] bytes ) {
		
		if( null == _maskingKey ) {
			return;
		}
		
		int maskIndex = 0;
		for( int i = 0, count = bytes.length; i < count; i++ ) {
			
			bytes[i] ^= _maskingKey[maskIndex];
			
			maskIndex++;
			if( 4 == maskIndex  ) {
				maskIndex = 0;
			}
			
		}
	}



	private static byte readByte( InputStream inputStream ) {
		
		int answer = InputStreamHelper.readByte( inputStream, Frame.class);
		if( -1 == answer ) {
			throw new BaseException( Frame.class, "-1 == answer" );
		}
		return (byte)answer;

	}

	
	public static Frame tryReadFrame( InputStream inputStream) {

		byte byte0;
		{
			int byteRead = InputStreamHelper.readByte( inputStream, Frame.class);
			if( -1 == byteRead ) {
				return null;
			}
			byte0 = (byte)byteRead;
		}
		
		int opCode;
		{
			opCode = 0x7f & byte0; // mask of the 'FIN_BIT'
//			log.debug( opCode, "opCode" );
						
			if( OPCODE_TEXT_FRAME == opCode ) {
				// ok
			} else if( OPCODE_CONNECTION_CLOSE == opCode ) {
				// ok
			} else if( OPCODE_PONG == opCode ) {
	            
	            
	            // vvv [RFC 6455 - The WebSocket Protocol](https://tools.ietf.org/html/rfc6455#section-5.5.3)
	            
//	            A Pong frame MAY be sent unsolicited.  This serves as a
//	            unidirectional heartbeat.  A response to an unsolicited Pong frame is
//	            not expected.
	            
	            // ^^^ [RFC 6455 - The WebSocket Protocol](https://tools.ietf.org/html/rfc6455#section-5.5.3)
	            
	            // ok
				log.debug( "OPCODE_PONG" );

			} else {
				throw new BaseException( Frame.class, "unhandled opcode; opCode = %d", opCode );
			}
		}

		byte byte1 = readByte( inputStream );
		
		int length = (0x7F)&byte1; // mask out the mask bit
//		log.debug( length, "length" );

		if( length == 126 ) {
			
			byte extendPayloadLengthMsb = readByte( inputStream );
			int extendedLength = 0xFF&extendPayloadLengthMsb;
			extendedLength <<= 8;
			
			byte extendPayloadLengthLsb = readByte( inputStream );
			extendedLength |= 0xFF&extendPayloadLengthLsb;
			
			length += extendedLength;
		} else if( 127 == length ) {
			throw new BaseException( Frame.class, "127 == length; unsupported");
		}

		Frame answer = new Frame(opCode,length);

		if( MASK_BIT == (byte1&MASK_BIT) ) {
			answer._maskingKey = new byte[4];
			InputStreamHelper.read( inputStream, answer._maskingKey, Frame.class );
		}

		return answer;
	}
	
	public static Frame readFrame( InputStream inputStream) {
		
		try {
			Frame answer = null;
			do {
				answer = tryReadFrame(inputStream);
			}
			while( null != answer && Frame.OPCODE_PONG == answer._opCode );
			return answer;
		} catch( Exception e ) {
			log.error( e );
			return null;
		}
	}
	
	void write( OutputStream outputStream ) {
		
		byte byte0 = (byte)(FIN_BIT|_opCode);
		byte byte1;
		byte[] extendedLength = null;
		
		
		if( 126 > _payloadLength ) {

			byte1 = (byte)_payloadLength;
			
		} else if ( 126 + 0xFFFF > _payloadLength) { // greater than 125 but less than '126 + 0xFFFF' (65,661)
			
			byte1 = 126;
			
			extendedLength = new byte[2];
			
			int payloadLength = _payloadLength;
			
			// network byte order (big endian) ... 
			extendedLength[1] = (byte)(payloadLength&0xFF); // Least Significant Byte 
			payloadLength >>= 8;
			extendedLength[0] = (byte)(payloadLength&0xFF); // Most Significant Byte
			
		} else { // greater than '126 + 0xFFFF' (65,661)
			throw new BaseException( this, "unhandled payload length (too large); _payloadLength = %d", _payloadLength );
		}
		
		OutputStreamHelper.write( byte0, outputStream, this);
		
		if( null != _maskingKey ) {
			byte1 |= MASK_BIT;
		}
		
		OutputStreamHelper.write( byte1, outputStream, this);
		
		if( null != extendedLength ) {
			
			OutputStreamHelper.write( outputStream, extendedLength, this);
		}
		
		if( null != _maskingKey ) {
			OutputStreamHelper.write( outputStream, _maskingKey, this);
		}
		
	}
	
	
}

