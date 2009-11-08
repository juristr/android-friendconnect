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

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import com.friendconnect.model.User;
import com.friendconnect.server.tests.utils.PropsUtils;
import com.friendconnect.services.AuthenticationService;
import com.friendconnect.services.FriendService;
import com.friendconnect.services.IAuthenticationService;
import com.friendconnect.services.IFriendService;
import com.google.gdata.util.ServiceException;

public class FriendServiceTest extends TestCase {
	private IAuthenticationService authenticationService;
	private IFriendService friendService;
	private Properties properties;
	
	protected void setUp() throws Exception {
		super.setUp();
		authenticationService = new AuthenticationService();
		friendService = new FriendService();
		properties = PropsUtils.load("config.properties");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		authenticationService = null;
		friendService = null;
		properties = null;
	}
	
	public void testGetFriends() throws MalformedURLException, IOException, ServiceException {
		String username = properties.getProperty("username");
		String password = properties.getProperty("password");
				
		String token = authenticationService.authenticate(username, password); 
		
		assertNotNull("Token should not be null", token);
		assertTrue("Token should not be equal to empty string", !token.equals(""));
		
		List<User> friends = friendService.getFriends(username, token);
		
		assertNotNull("List should not be null", friends);		
	}
	
	public void testGetFriendConnectUser(){
		String emailAddress = properties.getProperty("username");
		
		User friendConnectUser = friendService.getFriendConnectUser(emailAddress);
		assertNotNull(friendConnectUser);
		assertEquals(emailAddress, friendConnectUser.getEmailAddress());
	}
	
	public void testRegisterFriendConnectUser(){
		fail("Not yet implemented!");
	}
}
