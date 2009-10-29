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

package com.friendconnect.server.tests.xmlrpc;

import junit.framework.TestCase;

import com.friendconnect.services.AuthenticationService;
import com.friendconnect.services.XmlRpcService;
import com.google.gdata.util.AuthenticationException;

public class XmlRpcServiceTest extends TestCase {
	private AuthenticationService authService;
	private XmlRpcService xmlRpcService;
	
	
	protected void setUp() throws Exception {
		super.setUp();
		
		authService = new AuthenticationService();
		xmlRpcService = new XmlRpcService();
		xmlRpcService.setAuthService(authService);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		
		xmlRpcService = null;
		authService = null;
	}
	
	
	public void testAuthentication() throws AuthenticationException{
		String username = "";
		String password = "";
		
		fail("Provide appropriate passwords!");
		
		String token = xmlRpcService.login(username, password);
		
		assertNotNull(token);
	}

}
