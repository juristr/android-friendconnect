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

package com.friendconnect.server.tests.services;

import junit.framework.TestCase;

import com.friendconnect.services.AuthenticationService;
import com.friendconnect.services.IAuthenticationService;
import com.google.gdata.util.AuthenticationException;

public class AuthenticationServiceTest extends TestCase {
	private IAuthenticationService authService;
	
	
	protected void setUp() throws Exception {
		super.setUp();
		authService = new AuthenticationService();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		authService = null;
	}
	
	public void testAuthenticate() throws AuthenticationException{
		String username = "";
		String password = "";
				
		fail("provide some username + password for this test to succeed!");
		
		String token = authService.authenticate(username, password);
		
		assertNotNull(token);
		assertTrue(!token.equals(""));
		
		username = "someImgUser@gmail.com";
		password = "someImaginedPass";
			
		token = authService.authenticate(username, password);
		assertNull(token);
	}

}
