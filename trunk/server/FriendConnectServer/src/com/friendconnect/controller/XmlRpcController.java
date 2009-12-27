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

package com.friendconnect.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xmlrpc.server.XmlRpcHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.XmlRpcServletServer;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Simple controller class that handles web requests (i.e. XML-RPC calls)
 * from Spring's DispatcherServlet by delegating them to the XmlRpcServletServer.
 */
public class XmlRpcController extends AbstractController {

	private XmlRpcServletServer server = new XmlRpcServletServer();
	
	/**
	 * Initialization method.
	 */
	public void initialize(){
		XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl) server.getConfig();
		serverConfig.setEnabledForExtensions(true);
		serverConfig.setEnabledForExceptions(true);
	}
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {	
		server.execute(request, response);
		return null;
	}
	
	@Required
	public void setMapping(XmlRpcHandlerMapping mapping){
		server.setHandlerMapping(mapping);
	}

}
