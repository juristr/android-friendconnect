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

import com.friendconnect.dao.IUserDao;
import com.friendconnect.model.User;
import com.friendconnect.server.tests.mock.MockUserDao;
import com.friendconnect.server.tests.utils.PropsUtils;
import com.friendconnect.services.UserService;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class UserServiceTest extends TestCase {
	private UserService userService;
	private IUserDao mockUserDao;
	private Properties properties;
	private String applicationName = "FriendConnect";
	private String baseURL = "http://www.google.com/m8/feeds/contacts";
	private String projection = "thin";
	
	protected void setUp() throws Exception {
		super.setUp();
		mockUserDao = new MockUserDao();
		userService = new UserService();
		userService.setUserDao(mockUserDao);
		userService.setApplicationName(applicationName);
		userService.setBaseURL(baseURL);
		userService.setProjection(projection);
		properties = PropsUtils.load("config.properties");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		mockUserDao = null;
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
		
		user = null;
		try {
			user = userService.authenticate(emailAddress, password);
		} catch (AuthenticationException e) {
			//ignore exception
		}
		assertNull("User should be null", user);
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
