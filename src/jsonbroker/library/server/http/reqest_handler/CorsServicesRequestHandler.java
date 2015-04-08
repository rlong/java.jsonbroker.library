package jsonbroker.library.server.http.reqest_handler;

import jsonbroker.library.common.http.HttpStatus;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.broker.server.DescribedService;
import jsonbroker.library.broker.server.ServicesRegistery;
import jsonbroker.library.server.http.HttpMethod;
import jsonbroker.library.server.http.HttpRequest;
import jsonbroker.library.server.http.HttpResponse;
import jsonbroker.library.server.http.RequestHandler;

public class CorsServicesRequestHandler implements RequestHandler {
	
	
	
	private static final Log log = Log.getLog(CorsServicesRequestHandler.class);

	ServicesRegistery _servicesRegistery;

	
    public CorsServicesRequestHandler() {
        _servicesRegistery = new ServicesRegistery();
    }

    public CorsServicesRequestHandler(ServicesRegistery servicesRegistery) {
        _servicesRegistery = servicesRegistery;
    }
    
    public void addService(DescribedService service)
    {
        log.infoFormat("adding service '%s'", service.getServiceDescription().getServiceName());
        _servicesRegistery.addService(service);
    }

    private HttpResponse buildOptionsResponse(HttpRequest request) {
    	
    	
    	HttpResponse answer = new HttpResponse(HttpStatus.NO_CONTENT_204);
    	
        // vvv http://www.w3.org/TR/cors/#access-control-allow-methods-response-header
        answer.putHeader("Access-Control-Allow-Methods", "OPTIONS, POST");
        // ^^^ http://www.w3.org/TR/cors/#access-control-allow-methods-response-header
        
        String accessControlAllowOrigin = request.getHttpHeader("origin");
        if (null == accessControlAllowOrigin) {
            accessControlAllowOrigin = "*";
        }
        answer.putHeader("Access-Control-Allow-Origin", accessControlAllowOrigin);

        String accessControlRequestHeaders = request.getHttpHeader("access-control-request-headers");
        if (null != accessControlRequestHeaders)
        {
            answer.putHeader("Access-Control-Allow-Headers", accessControlRequestHeaders);
        }

    	return answer;
    	
    }

	@Override
    public HttpResponse processRequest(HttpRequest request) {
		
        // vvv `chrome` (and possibly others) preflights any CORS requests
        if (HttpMethod.OPTIONS == request.getMethod() ) {
        	return buildOptionsResponse(request);
        }
        // ^^^ `chrome` (and possibly others) preflights any CORS requests
        
        HttpResponse answer = ServicesRequestHandler.processPostRequest(_servicesRegistery, request);
        
     // vvv without echoing back the 'origin' for CORS requests, chrome (and possibly others) complains "Origin http://localhost:8081 is not allowed by Access-Control-Allow-Origin."
        {
        	String origin = request.getHttpHeader("origin");
        	
        	if( null != origin ) {
        		
        		answer.putHeader("Access-Control-Allow-Origin", origin);
        	}
        }
        // ^^^ without echoing back the 'origin' for CORS requests, chrome (and possibly others) complains "Origin http://localhost:8081 is not allowed by Access-Control-Allow-Origin."

        return answer;

		
    }

	@Override
	public String getProcessorUri() {
		
		return "/cors_services";
	}

}
