// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.server.http.reqest_handler;

import jsonbroker.library.common.auxiliary.Data;
import jsonbroker.library.common.broker.BrokerMessage;
import jsonbroker.library.common.broker.BrokerMessageType;
import jsonbroker.library.common.broker.Serializer;
import jsonbroker.library.common.http.DataEntity;
import jsonbroker.library.common.http.Entity;
import jsonbroker.library.common.http.HttpStatus;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.server.broker.ServicesRegistery;
import jsonbroker.library.server.http.HttpErrorHelper;
import jsonbroker.library.server.http.RequestHandler;
import jsonbroker.library.server.http.HttpRequest;
import jsonbroker.library.server.http.HttpResponse;

public class ServicesProcessor implements RequestHandler {
	
	private static final Log log = Log.getLog(ServicesProcessor.class);

	ServicesRegistery _servicesRegistery;
	
	
	public ServicesProcessor( ServicesRegistery servicesRegistery ){
		
		_servicesRegistery = servicesRegistery;
	}
	
	@Override
	public String getProcessorUri() {
		return "/services";
	}

	
	private Data getData( Entity entity ) {
		if( entity instanceof DataEntity ) {
			
			DataEntity dataEntity = (DataEntity)entity;
			return dataEntity.getData();
		}
		
		Data answer = new Data( entity.getContent(), (int)entity.getContentLength()  );
		return answer;
		
	}
	
	private BrokerMessage process(BrokerMessage request) {
		
		try {
			
			return _servicesRegistery.process( request );
			
		} catch( Throwable t ) { 
			log.error( t );
			return BrokerMessage.buildFault( request, t);
		}
	}

	
	@Override
	public HttpResponse processRequest(HttpRequest request) {
		
		
		if( !HttpRequest.METHOD_POST.equals( request.getMethod().toUpperCase()) ) {
			log.errorFormat( "unsupported method; request.getMethod() = '%s'", request.getMethod());
			throw HttpErrorHelper.badRequest400FromOriginator( this );
		}
		
		Entity entity = request.getEntity();
        if (64 * 1024 < entity.getContentLength())
        {
            log.errorFormat("64 * 1024 < entity.getContentLength(); entity.getContentLength() = %d", entity.getContentLength());
            throw HttpErrorHelper.requestEntityTooLarge413FromOriginator(this);
        }

			
		Data data = getData( entity );
		
		BrokerMessage call = Serializer.deserialize( data );		
		BrokerMessage response = process(call);
		
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


}
