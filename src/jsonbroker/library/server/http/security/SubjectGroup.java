// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.http.security;

import java.util.Collection;
import java.util.HashMap;

import jsonbroker.library.common.log.Log;
import jsonbroker.library.common.security.Subject;
import jsonbroker.library.server.http.HttpErrorHelper;

public class SubjectGroup {
		
	private static final Log log = Log.getLog(SubjectGroup.class);
	

    ///////////////////////////////////////////////////////////////////////
	HashMap<String, Subject> _subjectDictionary;
	
    ///////////////////////////////////////////////////////////////////////
    public SubjectGroup()
    {
    	
        _subjectDictionary = new HashMap<String, Subject>();

    }
    
    public Collection<Subject> subjects() {
    	
    	Collection<Subject> answer = _subjectDictionary.values();
    	
    	return answer;
    }
    
    
    public int count()
    {
    	return _subjectDictionary.size();
    }
    
    public boolean contains(String username) {
    	
    	
    	if (_subjectDictionary.containsKey(username) ) {
    		return true;
    	}
    	return false;
    }
    
    // will throw an exception for a bad 'username'
    public Subject getSubject(String username )
    {

    	if (!_subjectDictionary.containsKey(username))
        {
    		
    		log.errorFormat( "!_subjectDictionary.containsKey(username); username = '%s'" , username);
            throw HttpErrorHelper.unauthorized401FromOriginator(this);
        }

    	Subject answer = _subjectDictionary.get(username);
    	
        return answer;
    }
    
    
    public void addSubject(Subject subject)
    {
    	String username = subject.getUsername();

    	_subjectDictionary.put(username, subject);
    }
    
    
    public void removeSubject(String username)
    {
    	_subjectDictionary.remove( username );

    }


}
