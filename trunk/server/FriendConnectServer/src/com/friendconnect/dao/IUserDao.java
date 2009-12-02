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

import com.friendconnect.model.POIAlert;
import com.friendconnect.model.User;

/**
 * Interface of a DAO object to store and retrieve user data into/from a JDO Datastore
 */
public interface IUserDao {
	
	/**
	 * Saves or updates a user into the JDO Datastore
	 * @param user
	 */
	public void saveUser(User user);
	
	/**
	 * Fetches a user from the JDO Datastore passing its user id
	 * @param userId
	 * @return
	 */
	public User getUserById(String userId);
	
	/**
	 * Fetches a user from the JDO Datastore passing its email address
	 * @param emailAddress
	 * @return
	 */
	public User getUserByEmailAddress(String emailAddress);
	
	
	/**
	 * Deletes a user from the JDO Datastore
	 * @param userId
	 */
	public void removeUser(String userId);
	
	/**
	 * Fetches all friends of a certain user from the JDO Datastore
	 * @param userId
	 * @return
	 */
	public List<User> getFriends(String userId);
	
	/**
	 * Fetches all pending friends of a certain user from the JDO Datastore
	 * @param userId
	 * @return
	 */
	public List<User> getPendingFriends(String userId);
	
	/**
	 * Adds a friend to a user in the JDO Datastore
	 * @param userId
	 * @param friendId
	 */
	public void addFriend(String userId, String friendId);
	
	/**
	 * Removes a friend from a user in the JDO Datastore
	 * @param userId
	 * @param friendId
	 */
	public void removeFriend(String userId, String friendId);
	
	/**
	 * Adds a pending friend to a user in the JDO Datastore
	 * @param userId
	 * @param friendId
	 */
	public void addPendingFriend(String userId, String friendId);
	
	/**
	 * Removes a pending friend from a user in the JDO Datastore
	 * @param userId
	 * @param friendId
	 */
	public void removePendingFriend(String userId, String friendId);
	
	/**
	 * Returns all users from the JDO Datastore that match the specified search criteria
	 * @param searchText
	 * @return
	 */
	public List<User> searchUsers(String searchText);
	
	/**
	 * Returns all users whose status is set to online from the JDO Datastore
	 * @return
	 */
	public List<User> getOnlineUsers();
	
	/**
	 * Saves or updates a POI alert for a certain user into the JDO Datastore
	 * @param userId
	 * @param poiAlert
	 */
	public void savePOIAlert(String userId, POIAlert poiAlert);
	
	/**
	 * Fetches all POI alerts for of a certain user from the JDO Datastore
	 * @param userId
	 * @return
	 */
	public List<POIAlert> getPOIAlerts(String userId);
	
	/**
	 * Fetches a POI alert from the JDO Datastore passing its POI alert id
	 * @param poiAlertId
	 * @return
	 */
	public POIAlert getPOIAlert(String poiAlertId);
	
	/**
	 * Deletes a POI alert from the JDO Datastore
	 * @param poiAlertId
	 */
	public void removePOIAlert(String poiAlertId);	
}
