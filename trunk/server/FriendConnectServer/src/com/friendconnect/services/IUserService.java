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
import java.util.List;

import javax.jdo.JDOException;

import com.friendconnect.model.User;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public interface IUserService {
	
	 /**
	 * Accesses the Google server for authenticating the user
	 * @param username the username, i.e. the Google Account email of the user
	 * @param password the password, i.e. the Google Account password
	 * @return user, if the authentication was successful, null otherwise
	 * @throws AuthenticationException
	 * @throws JDOException
	 */
	public User authenticate(String username, String password) throws AuthenticationException, JDOException;

	/**
	 * Validates the received token against the user id. This method will
	 * be called on subsequent calls.
	 * @param userId the id of the user
	 * @param token the user token
	 * @return
	 */
	public boolean validateToken(String userId, String token) throws JDOException;
	
	/**
	 * Returns the list of friends for a certain user
	 * @param userId the id of the user
	 * @return
	 */
	public List<User> getFriends(String userId) throws JDOException;
	
	/**
	 * Adds a friend to the friend list of a certain user
	 * @param userId the id of the user
	 * @param friendId the id of the friend to add
	 */
	public void addFriend(String userId, String friendId) throws JDOException;
	
	/**
	 * Removes a friend from the friend list of a certain user
	 * @param userId the id of the user
	 * @param friendId the id of the friend to remove
	 * @throws JDOException
	 */
	public void removeFriend(String userId, String friendId) throws JDOException;
	
	/**
	 * Updates the information of an existing FriendConnectUser
	 * @param user the {@link User} to be updated
	 */
	public void updateUser(User user);
	
	/**
	 * Fetches all the Google contacts for a certain user
	 * @param username the Google Account email of the user
	 * @param token the user token 
	 * @return
	 * @throws IOException
	 * @throws ServiceException
	 */
	public List<User> getGoogleContacts(String username, String token) throws IOException, ServiceException;
}