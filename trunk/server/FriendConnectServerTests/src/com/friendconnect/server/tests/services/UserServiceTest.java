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
import com.friendconnect.services.IUserService;
import com.friendconnect.services.UserService;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class UserServiceTest extends TestCase {
	private IUserService userService;
	private Properties properties;
	
	protected void setUp() throws Exception {
		super.setUp();
		userService = new UserService();
		properties = PropsUtils.load("config.properties");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		userService = null;
		properties = null;
	}
	
	public void testAuthenticate() throws AuthenticationException{
		String emailAddress = properties.getProperty("emailAddress");
		String password = properties.getProperty("password");
		
		User user = userService.authenticate(emailAddress, password);
		
		assertNotNull("User should not be null", user);
		assertNotNull("Token should not be null", user.getToken());
		assertTrue("Token should not be equal to empty string", !user.getToken().equals(""));
		
		emailAddress = "someImgUser@gmail.com";
		password = "someImaginedPass";
		
		user = userService.authenticate(emailAddress, password);
		assertNull("User should be null", user);
		assertNull("User should be null", user.getToken());
	}
	
	
	public void testGetGoogleContacts() throws MalformedURLException, IOException, ServiceException {
		String emailAddress = properties.getProperty("emailAddress");
		String password = properties.getProperty("password");
				
		User user = userService.authenticate(emailAddress, password); 
		
		assertNotNull("User should not be null", user);
		assertNotNull("Token should not be null", user.getToken());
		assertTrue("Token should not be equal to empty string", !user.getToken().equals(""));
		
		List<User> friends = userService.getGoogleContacts(emailAddress, user.getToken());
		
		assertNotNull("List should not be null", friends);		
	}
}
