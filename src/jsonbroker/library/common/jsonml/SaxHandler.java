package jsonbroker.library.common.jsonml;

import java.util.Stack;

import jsonbroker.library.common.json.JsonDocumentHandler;
import jsonbroker.library.common.log.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxHandler extends DefaultHandler {
	
	private static final Log log = Log.getLog( SaxHandler.class );

	
	////////////////////////////////////////////////////////////////////////////
	//
	int _currentIndex;

	////////////////////////////////////////////////////////////////////////////
	//
	StringBuffer _elementText;

	////////////////////////////////////////////////////////////////////////////
	//
	boolean _ignoreEmptyStrings = true;

	////////////////////////////////////////////////////////////////////////////
	//
	Stack<Integer> _indices;

	////////////////////////////////////////////////////////////////////////////
	//
	JsonDocumentHandler _jsonHandler;


	
	////////////////////////////////////////////////////////////////////////////
	//
	boolean _trim = true;

	public SaxHandler( JsonDocumentHandler jsonHandler ) {
		_elementText = new StringBuffer();
		_indices = new Stack<Integer>();
		_jsonHandler = jsonHandler;
	}
	

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		
		_elementText.append( ch, start, length);

	}
	
	private final void flushElementText() {
		
		if( 0 == _elementText.length() ) {
			return; // no-op
		}
		
		String string = _elementText.toString();
		_elementText.setLength( 0 ); // http://stackoverflow.com/questions/5192512/how-to-clear-empty-java-stringbuilder
		
		if( _trim ) {
			string = string.trim();
		}
		
		
		if( 0 == string.length() && _ignoreEmptyStrings ) {
			return;
		}

		log.debug( string, "string");

		_jsonHandler.onString( _currentIndex, string );
		_currentIndex++;
		
		
	}


	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		flushElementText();

		if( 0 == _indices.size() ) {
			_jsonHandler.onArrayDocumentEnd();
		} else {
			_currentIndex = _indices.pop(); // 'pop'
			_jsonHandler.onArrayEnd( _currentIndex );
		}
		
	}


	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {

		// root element ?
		if( 0 == _indices.size() ) {
			
			_jsonHandler.onArrayDocumentStart();
			
		} else {
			
			flushElementText();
			_jsonHandler.onArrayStart( _currentIndex );

		}

		// 'push'
		{
			_indices.push( _currentIndex ); 
			_currentIndex = 0;
		}

		log.debug( qName, "qName" );
		
		_jsonHandler.onString( _currentIndex, qName );
		
		_currentIndex = 1;
		
		int count = atts.getLength();
		if( 0 < count ) {
			
			_jsonHandler.onObjectStart( _currentIndex );

			for( int i = 0; i < count; i++ ) {
				String attQName = atts.getQName( i );
				String attValue = atts.getValue( i );
				_jsonHandler.onString( attQName, attValue );			
			}		
			_jsonHandler.onObjectEnd( _currentIndex );
			_currentIndex = 2;		
		}
	}



}
