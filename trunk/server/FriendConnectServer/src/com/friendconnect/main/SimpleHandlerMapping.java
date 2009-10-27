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

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.AbstractReflectiveHandlerMapping;
import org.springframework.util.Assert;

public class SimpleHandlerMapping extends AbstractReflectiveHandlerMapping {

	public void setServices(Map<String, Object> services) throws XmlRpcException {
        // Check parameter
        Assert.notNull(services, "Services map should not be null.");

        SimpleRequestProcessorFactoryFactory factory = new SimpleRequestProcessorFactoryFactory();

        // Register all service beans with the factory
        for (Object serviceBean : services.values()) {
        	factory.setServiceBean(serviceBean.getClass(), serviceBean);
        }

        setRequestProcessorFactoryFactory(factory);

        // Loop through the set
        Iterator<Entry<String, Object>> it = services.entrySet().iterator();
        while (it.hasNext()) {
            // Fetch from the map
            Entry<String, Object> entry = it.next();
            String serviceName = entry.getKey();
            Object serviceBean = entry.getValue();

            // Register service in the handler mapping
            registerPublicMethods(serviceName, serviceBean.getClass());
        }
    }
}
