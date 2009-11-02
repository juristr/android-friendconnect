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

package com.friendconnect.dao;

import java.util.List;

import com.friendconnect.model.User;

/**
 * Interface of a DAO object to store and retrieve user data into/from a JDO Datastore
 */
public interface IUserDao {
	
	/**
	 * Saves a user into the JDO Datastore
	 * @param user
	 */
	public void saveUser(User user);
	
	/**
	 * Fetches a user from the JDO Datastore passing its id
	 * @param userId
	 * @param loadFriends
	 * @return
	 */
	public User getUserById(String userId, boolean loadFriends);
	
	/**
	 * Fetches a user from the JDO Datastore passing its email address
	 * @param emailAddress
	 * @param loadFriends
	 * @return
	 */
	public User getUserByEmailAddress(String emailAddress, boolean loadFriends);
	
	/**
	 * Deletes a user from the JDO Datastore
	 * @param userId
	 */
	public void removeUser(String userId);
	
	/**
	 * Returns all users from the JDO Datastore that match the specified search criteria
	 * @param searchText
	 * @return
	 */
	public List<User> searchUsers(String searchText);
}
