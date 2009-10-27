/*   **********************************************************************  **
 **   Copyright notice                                                       **
 **                                                                          **
 **   (c) 2009, FriendConnect			                       				 **
 **   All rights reserved.                                                   **
 **                                                                          **
 **	  This program and the accompanying materials are made available under   **
 **   the terms of the GPLv3 license which accompanies this	    			 **
 **   distribution. A copy is found in the textfile LICENSE.txt				 **
 **                                                                          **
 **   This copyright notice MUST APPEAR in all copies of the file!           **
 **                                                                          **
 **   Main developers:                                                       **
 **     Juri Strumpflohner		http://blog.js-development.com	             **
 **     Matthias Braunhofer		http://matthias.jimdo.com	                 **
 **                                                                          **
 **  **********************************************************************  */

package com.friendconnect.main;

import java.util.HashMap;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.server.RequestProcessorFactoryFactory;

public class SimpleRequestProcessorFactoryFactory implements RequestProcessorFactoryFactory {

	private Map<Class, Object> serviceBeans = new HashMap<Class, Object>();
	
	@Override
	public RequestProcessorFactory getRequestProcessorFactory(Class clazz)
			throws XmlRpcException {
		
		final Object serviceBean = serviceBeans.get(clazz);
		
		if(serviceBean == null){
			throw new XmlRpcException("Handler service with name " + clazz.getCanonicalName() + " not found!");
		}
		
		return new RequestProcessorFactory() {
            public Object getRequestProcessor(XmlRpcRequest pRequest) throws XmlRpcException {
                return serviceBean;
            }
        };

	}
	
	public void setServiceBean(Class clazz, Object object){
		serviceBeans.put(clazz, object);
	}

}
