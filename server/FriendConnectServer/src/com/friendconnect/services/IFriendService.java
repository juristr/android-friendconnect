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

package com.friendconnect.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.friendconnect.model.User;
import com.google.gdata.util.ServiceException;

public interface IFriendService {
	
	/**
	 * Retrieves the friends of the user identified by the given username and authentication token
	 * @param username the friendconnectuser requesting his friends
	 * @param token the authentication token
	 * @return {@link List} of friends
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ServiceException
	 */
	List<User> getFriends(String username, String token) throws MalformedURLException, IOException, ServiceException;
	
	/**
	 * Gets the current FriendConnect user by its email (i.e. the GAccount username).
	 * This is needed at login-time.
	 * @param emailAddress the email address of the user
	 * @return {@link User}
	 */
	User getFriendConnectUser(String emailAddress);
	
	/**
	 * Registers a new FriendConnect user. This registration is just inserting
	 * the user into the FriendConnect DB, since the authentication is outsourced to Google.
	 * @param emailAddress the email addres of the user
	 * @return the registered {@link User}
	 */
	User registerFriendConnectUser(String emailAddress);
	
	List<User> getDummyFriends();
}
