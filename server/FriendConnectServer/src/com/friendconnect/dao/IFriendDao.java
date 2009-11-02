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

import com.friendconnect.model.Friend;

/**
 * Interface of a DAO object to store and retrieve friend data into/from a JDO Datastore
 */
public interface IFriendDao {
	
	/**
	 * Fetches a friend from the JDO Datastore passing its id
	 * @param friendId
	 * @return
	 */
	public Friend getFriend(String friendId);
	
	/**
	 * Fetches all friends for a certain user from the JDO Datastore
	 * @param userId
	 * @return
	 */
	public List<Friend> getFriends(String userId);
	
	/**
	 * Adds a friend to a certain user in the JDO Datastore
	 * @param userId
	 * @param friend
	 */
	public void addFriend(String userId, Friend friend);
	
	/**
	 * Removes a friend from a certain user in the JDO Datastore
	 * @param userId
	 * @param friendId
	 */
	public void removeFriend(String userId, String friendId);
	
}
