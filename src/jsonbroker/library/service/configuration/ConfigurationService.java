// Copyright (c) 2013 Richard Long & HexBeerium
//
// Released under the MIT license ( http://opensource.org/licenses/MIT )
//

package jsonbroker.library.service.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import jsonbroker.library.common.auxiliary.InputStreamHelper;
import jsonbroker.library.common.auxiliary.OutputStreamHelper;
import jsonbroker.library.common.auxiliary.StringHelper;
import jsonbroker.library.common.broker.BrokerMessage;
import jsonbroker.library.common.exception.BaseException;
import jsonbroker.library.common.json.JsonObject;
import jsonbroker.library.common.log.Log;
import jsonbroker.library.server.broker.DescribedService;
import jsonbroker.library.server.broker.ServiceDescription;
import jsonbroker.library.server.broker.ServiceHelper;


public class ConfigurationService implements DescribedService {

	private static final Log log = Log.getLog( ConfigurationService.class );

	public static final String SERVICE_NAME = "jsonbroker.ConfigurationService";
	public static final ServiceDescription SERVICE_DESCRIPTION = new ServiceDescription( SERVICE_NAME );
	
	private final HashMap<String, JsonObject> _bundles;
	

	////////////////////////////////////////////////////////////////////////////
	File _root;

	////////////////////////////////////////////////////////////////////////////
	public ConfigurationService( String rootFolder)  {
		
		_root = new File( rootFolder );
		_root.mkdirs();
		log.info( _root.getAbsolutePath(), "_root.getAbsolutePath()" );
		
		_bundles = new HashMap<String, JsonObject>();
	}
	
	
	public JsonObject getBundle( String bundleName ) {
		
		JsonObject answer = _bundles.get( bundleName );
		if( null != answer ) {
			return answer;
		}
		
		String relativePath = bundleName + ".json";
		
		File sourceFile = new File( _root,  relativePath );

		log.debug(sourceFile.getAbsolutePath(),"sourceFile.getAbsolutePath()");

		if( !sourceFile.exists() || !sourceFile.canRead() ) {
			return null;
		}
			
		int length = (int)sourceFile.length();
		
		FileInputStream fis = null;
		try { 
			try {
				fis = new FileInputStream( sourceFile );
			} catch (FileNotFoundException e) {
				throw new BaseException( this, e);		
			}

			answer = JsonObject.build( fis, length);
			_bundles.put(bundleName, answer);
			return answer;
		} finally {
			InputStreamHelper.close( fis, false, this  );
		}
		
	}

	private void saveBundle( String bundleName, JsonObject bundle ) {
		
		String bundleText = bundle.toString();
		
		String relativePath = bundleName + ".json";
		File bundleFile = new File( _root, relativePath );
		FileOutputStream fos = null;
		
		try {
			fos = new FileOutputStream( bundleFile );

			byte[] bundleBytes = StringHelper.toUtfBytes( bundleText);
			fos.write( bundleBytes );
		} catch (Exception e) {
			throw new BaseException( this, e);
		}
		finally {
			OutputStreamHelper.close( fos );
		}
	}

	
	public void setBundle( String bundleName, JsonObject bundle ) {
		
		_bundles.put( bundleName, bundle );
		
	}
	
	public void save_bundles() {
		
		log.enteredMethod();
		
		for (Map.Entry<String, JsonObject> entry : _bundles.entrySet()) {

			String bundleName = entry.getKey();
			JsonObject bundle = entry.getValue();
			saveBundle( bundleName, bundle);
		}		
	}
	
	
	
	
	@Override
	public final BrokerMessage process(BrokerMessage request) {
		
		String methodName = request.getMethodName();
		
		if( "save_bundles".equals( methodName ) ) {
			
			this.save_bundles();
			return BrokerMessage.buildResponse( request );
			
		}

		if( "saveBundles".equals( methodName ) ) {
			
			this.save_bundles();
			return BrokerMessage.buildResponse( request );			
		}

		
		if( "getBundle".equals( methodName ) ) {

			JsonObject associativeParamaters = request.getAssociativeParamaters();
			String bundleName = associativeParamaters.getString("bundle_name");
			JsonObject bundleValue = getBundle(bundleName);
			
			//BrokerMessage answer = BrokerMessage.buildResponse(request, bundleName, bundle);
			BrokerMessage answer = BrokerMessage.buildResponse( request );
			associativeParamaters = answer.getAssociativeParamaters();
			associativeParamaters.put( "bundle_name", bundleName);
			associativeParamaters.put( "bundle_value", bundleValue);
			
			return answer;

		}
		
		throw ServiceHelper.methodNotFound( this, request);
		
	}

	@Override
	public ServiceDescription getServiceDescription() {
		return SERVICE_DESCRIPTION;
	}


}
