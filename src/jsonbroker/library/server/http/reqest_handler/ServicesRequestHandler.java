// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.http.reqest_handler;

import jsonbroker.library.common.auxiliary.Data;
import jsonbroker.library.common.broker.BrokerMessage;
import jsonbroker.library.common.broker.BrokerMessageType;
import jsonbroker.library.common.broker.Serializer;
import jsonbroker.library.common.defaults.Defaults;
import jsonbroker.library.common.http.DataEntity;
import jsonbroker.library.common.http.Entity;
import jsonbroker.library.common.http.HttpStatus;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.server.broker.DescribedService;
import jsonbroker.library.server.broker.ServicesRegistery;
import jsonbroker.library.server.http.HttpErrorHelper;
import jsonbroker.library.server.http.HttpMethod;
import jsonbroker.library.server.http.HttpRequest;
import jsonbroker.library.server.http.HttpResponse;
import jsonbroker.library.server.http.RequestHandler;


public class ServicesRequestHandler implements RequestHandler {
	
	private static final Log log = Log.getLog(ServicesRequestHandler.class);

	ServicesRegistery _servicesRegistery;
	
	private static int _MAXIMUM_REQUEST_ENTITY_LENGTH;
	
	static {
		Defaults defaults = Defaults.getDefaults( "jsonbroker.ServicesRequestHandler" );
		_MAXIMUM_REQUEST_ENTITY_LENGTH = defaults.getInt( "MAXIMUM_REQUEST_ENTITY_LENGTH" , 32 * 1024);
	}

	public ServicesRequestHandler(){
		
		_servicesRegistery = new ServicesRegistery();
	}

	
	public ServicesRequestHandler( ServicesRegistery servicesRegistery ){
		
		_servicesRegistery = servicesRegistery;
	}
	
    public void addService(DescribedService service)
    {
        _servicesRegistery.addService(service);
    }
    
    public void removeService( DescribedService service) {
        log.debugFormat("removing service '%s'", service.getServiceDescription().getServiceName());
        _servicesRegistery.removeService(service);    	
    }
    

	
	private static Data getData( Entity entity ) {
		if( entity instanceof DataEntity ) {
			
			DataEntity dataEntity = (DataEntity)entity;
			return dataEntity.getData();
		}
		
		Data answer = new Data( entity.getContent(), (int)entity.getContentLength()  );
		return answer;
		
	}
	
	private static BrokerMessage process(ServicesRegistery servicesRegistery, BrokerMessage request) {
		
		try {
			
			return servicesRegistery.process( request );
			
		} catch( Throwable t ) { 
			log.error( t );
			return BrokerMessage.buildFault( request, t);
		}
	}

	
	static HttpResponse processPostRequest(ServicesRegistery servicesRegistery, HttpRequest request) {
		
		
		if( HttpMethod.POST !=  request.getMethod() ) {
			log.errorFormat( "unsupported method; request.getMethod() = '%s'", request.getMethod());
			throw HttpErrorHelper.badRequest400FromOriginator( ServicesRequestHandler.class );
		}
		
		Entity entity = request.getEntity();
        if (_MAXIMUM_REQUEST_ENTITY_LENGTH < entity.getContentLength())
        {
            log.errorFormat("_MAXIMUM_REQUEST_ENTITY_LENGTH < entity.getContentLength(); _MAXIMUM_REQUEST_ENTITY_LENGTH = %d; entity.getContentLength() = %d", _MAXIMUM_REQUEST_ENTITY_LENGTH, entity.getContentLength());
            throw HttpErrorHelper.requestEntityTooLarge413FromOriginator(ServicesRequestHandler.class);
        }

			
		Data data = getData( entity );
		
		BrokerMessage call = Serializer.deserialize( data );		
		BrokerMessage response = process( servicesRegistery, call);
		
		HttpResponse answer;
		{
			
			if( call.getMessageType() == BrokerMessageType.ONEWAY ) {
				answer = new HttpResponse( HttpStatus.NO_CONTENT_204 );
			}else {
				Data responseData = Serializer.serialize(response);
				Entity responseBody = new DataEntity( responseData );
				
				answer = new HttpResponse( HttpStatus.OK_200,responseBody);				
			}
		}
		
		return answer;
		
	}
	
	@Override
    public HttpResponse processRequest(HttpRequest request)
    {
        return processPostRequest(_servicesRegistery, request);
    }
	
	@Override
	public String getProcessorUri() {
		return "/services";
	}
	
	
	public void debug() {
		_servicesRegistery.debug();
	}



}
