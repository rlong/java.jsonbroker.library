// Copyright (c) 2014 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//


package jsonbroker.library.common.http.multi_part;

import java.io.InputStream;

import jsonbroker.library.common.auxiliary.InputStreamHelper;
import jsonbroker.library.common.auxiliary.MutableData;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.http.Entity;
import jsonbroker.library.common.log.Log;


/**
 * 
 * as per http://www.w3.org/Protocols/rfc1341/7_2_Multipart.html
 *
 */
public class MultiPartReader {

	final static int BUFFER_SIZE = 8 * 1024;
	
	
	private static final Log log = Log.getLog( MultiPartReader.class );
	
	
    ///////////////////////////////////////////////////////////////////////
    // boundary
    private String _boundary;

    
    ///////////////////////////////////////////////////////////////////////
    // content
    private InputStream _content;
    
    ///////////////////////////////////////////////////////////////////////
    // contentRemaining
    private long _contentRemaining;

    ///////////////////////////////////////////////////////////////////////
    // buffer
    private byte[] _buffer;

    
    ///////////////////////////////////////////////////////////////////////
    // currentOffset
    private int _currentOffset;


    ///////////////////////////////////////////////////////////////////////
    // bufferEnd
    private int _bufferEnd;


    
    ///////////////////////////////////////////////////////////////////////
    // 
    public MultiPartReader(String boundary, Entity entity) {
        _boundary = boundary;
        _content = entity.getContent();
        _contentRemaining = entity.getContentLength();

        _buffer = new byte[BUFFER_SIZE];
        _currentOffset = 0;
        _bufferEnd = 0;
    }
    
    
    private void fillBuffer() {
    	
    	
        _currentOffset = 0;

        long amountToRead = _contentRemaining;

        if (amountToRead > _buffer.length) {
            amountToRead = _buffer.length;
        }
        
        int bytesRead = InputStreamHelper.read( _content, _buffer, 0, (int)amountToRead, this);
        if (0 == bytesRead && 0 != amountToRead) // `0 == bytesRead` marks the end of the stream
        {
            BaseException e = new BaseException(this, "0 == bytesRead && 0 != amountToRead; amountToRead = %d; _contentRemaining = %d", amountToRead, _contentRemaining);
            throw e;
        }
        _contentRemaining -= bytesRead;
        _bufferEnd = bytesRead;
    }
    
    
    private byte readByte()
    {
        if (_currentOffset == _bufferEnd) {
            fillBuffer();
        }
        byte answer = _buffer[_currentOffset];
        _currentOffset++;
        return answer;
    }
    
    String readLine(MutableData stringBuffer) {
    	
        byte lastChar = 88; // 'X'


        stringBuffer.clear();
        
        while (true) {
        	
            byte currentChar = readByte();

            if (0x0d == lastChar) { // '\r'
            	
                if (0x0a == currentChar) { // `\n`
                	
                	return stringBuffer.getUtf8String(0, stringBuffer.getCount());
                	
                } else {
                	stringBuffer.append( lastChar); // add the previous '\r' 
                }

            }
            if (0x0d != currentChar) { // '\r'
            	stringBuffer.append(currentChar); 
            }
            
            lastChar = currentChar;
        }
    }
    
    // used for testing only 
    DelimiterIndicator skipToNextDelimiterIndicator() {
    	
        DelimiterDetector detector = new DelimiterDetector(_boundary);

        if (_currentOffset == _bufferEnd) {
            fillBuffer();
        }

        DelimiterIndicator indicator = detector.update(_buffer, _currentOffset, _bufferEnd);
        
        if (null == indicator) {
            return null;
        }

        if( indicator instanceof DelimiterFound ) {
        	DelimiterFound delimiterFound = (DelimiterFound)indicator;
        	_currentOffset = delimiterFound.getEndOfDelimiter();
        }
        
        return indicator;

    }
    
    
    // can return null if not indicator was found (partial or complete)
    private DelimiterFound findFirstDelimiterIndicator(DelimiterDetector detector) {
    	
        if (_currentOffset == _bufferEnd) {
            fillBuffer();
        }

        DelimiterIndicator indicator = detector.update(_buffer, _currentOffset, _bufferEnd);
        
        if (null == indicator)
        {
            throw new BaseException(this, "null == indicator, could not find first delimiter; _boundary = '%s'", _boundary);
        }
        
        if (!(indicator instanceof DelimiterFound) ) {
        	
            log.error("unimplemented: support for `DelimiterIndicator` types that are not `DelimiterFound`");
            throw new BaseException(this, "!(indicator instanceof DelimiterFound); indicator.getClass().getName() = '%s'", indicator.getClass().getName());
        }
        
        return (DelimiterFound)indicator;

    }
    
    
    // will accept `null` values 
    private void writePartialDelimiter(PartialDelimiterMatched partialDelimiterMatched, PartHandler partHandler)
    {
        if (null == partialDelimiterMatched) {
            return;
        }
        
        byte[] previouslyMatchingBytes = partialDelimiterMatched.getMatchingBytes();
        partHandler.handleBytes(previouslyMatchingBytes, 0, previouslyMatchingBytes.length);

    }
    
    
    private DelimiterFound tryProcessPart(PartHandler partHandler, DelimiterDetector detector) {
    	
        MutableData stringBuffer = new MutableData();
        String line = readLine(stringBuffer);
        
        while ( 0 != line.length() ) {
        	
        	int firstColon = line.indexOf(":");
            if (-1 == firstColon) {
                throw new BaseException(this, "-1 == firstColon; line = '%s'", line);
            }
            
            String name = line.substring(0, firstColon).toLowerCase(); // headers are case insensitive
            String value = line.substring(firstColon + 1).trim();
            
            partHandler.handleHeader(name, value);
            
            
            line = readLine(stringBuffer);
        	
        }

        PartialDelimiterMatched partialDelimiterMatched = null;

        boolean partCompleted = false;
        while (!partCompleted) {
        	
        	DelimiterIndicator delimiterIndicator = detector.update(_buffer, _currentOffset, _bufferEnd);
        	
        	// nothing detected ? 
        	if (null == delimiterIndicator) {
        		
                // write existing partial match (if it exists)
                {
                    writePartialDelimiter(partialDelimiterMatched, partHandler);
                    partialDelimiterMatched = null;
                }

                int length = _bufferEnd - _currentOffset;
                partHandler.handleBytes(_buffer, _currentOffset, length);
                fillBuffer();
                continue;

        	}
        	
        	
            if (delimiterIndicator instanceof DelimiterFound) {
            	
            	
                DelimiterFound delimiterFound = (DelimiterFound)delimiterIndicator;
                // more content to add ? 
                if (!delimiterFound.completesPartialMatch())
                {
                    // write existing partial match (if it exists)
                    {
                        writePartialDelimiter(partialDelimiterMatched, partHandler);
                        partialDelimiterMatched = null;
                    }

                    int length = delimiterFound.getStartOfDelimiter() - _currentOffset;
                    partHandler.handleBytes(_buffer, _currentOffset, length);
                }
                else // delimiterFound completesPartialMatch
                {
                    partialDelimiterMatched = null;
                }

                _currentOffset = delimiterFound.getEndOfDelimiter();

                partHandler.partCompleted();
                partCompleted = true; // not required, but signalling intent
                return delimiterFound;

            }
 
            if (delimiterIndicator instanceof PartialDelimiterMatched)
            {
                // write existing partial match (if it exists)
                {
                    writePartialDelimiter(partialDelimiterMatched, partHandler);                        
                }
                partialDelimiterMatched = (PartialDelimiterMatched)delimiterIndicator;
                byte[] matchingBytes = partialDelimiterMatched.getMatchingBytes();
                int startOfMatch = _bufferEnd - matchingBytes.length;
                if (startOfMatch < _currentOffset)
                {
                    // can happen when the delimiter straddles 2 distinct buffer reads of size `BUFFER_SIZE`
                    startOfMatch = _currentOffset;
                }
                else
                {
                    int length = startOfMatch - _currentOffset;
                    partHandler.handleBytes(_buffer, _currentOffset, length);
                }
                fillBuffer();
            }
            

        }
        
        // will never happen ... we hope
        throw new BaseException(this, "unexpected code path followed");

    	
    }
        
    private DelimiterFound processPart(MultiPartHandler multiPartHandler, DelimiterDetector detector)
    {

        PartHandler partHandler = multiPartHandler.foundPartDelimiter();

        try {
            return tryProcessPart(partHandler, detector);
        }
        catch (Exception e) {
        	
            partHandler.handleException(e);
            
            if( e instanceof BaseException ) {
            	throw (BaseException)e;
            }
            throw new BaseException( this, e);
        }
    }
    
    
    private void tryProcess(MultiPartHandler multiPartHandler, boolean skipFirstCrNl) {
    	
        DelimiterDetector detector = new DelimiterDetector(_boundary);

        if (skipFirstCrNl) {
            byte[] crnl = new byte[]{0x0d,0x0a}; // 0x0d = cr; 0x0a = nl
            detector.update(crnl, 0, 2);
        }

        DelimiterIndicator indicator = findFirstDelimiterIndicator(detector);

        if (null == indicator) {
            BaseException e = new BaseException(this, "null == indicator; expected delimiter at start of stream");
            throw e;
        }
        
        if (!(indicator instanceof DelimiterFound)) {
            log.error("unimplemented: support for `DelimiterIndicator` types that are not `DelimiterFound`");
            throw new BaseException(this, "!(indicator instanceof DelimiterFound); indicator.getClass().getName() = '%s'", indicator.getClass().getName());
        }


        DelimiterFound delimiterFound = (DelimiterFound)indicator;

        _currentOffset = delimiterFound.getEndOfDelimiter();


        while (!delimiterFound.isCloseDelimiter() ) {
            delimiterFound = processPart(multiPartHandler, detector);
        }

        multiPartHandler.foundCloseDelimiter();

        while (0 != _contentRemaining) {
            fillBuffer();
        }


    	
    }

    public void process( MultiPartHandler multiPartHandler, boolean skipFirstCrNl ) 
    {

        try {
            tryProcess(multiPartHandler, skipFirstCrNl);                
        }
        catch (Exception e) {
            multiPartHandler.handleException(e);
        }
        
    }
    
    
    public void Process(MultiPartHandler multiPartHandler) {
        this.process(multiPartHandler, false);
    }

	
	
}
