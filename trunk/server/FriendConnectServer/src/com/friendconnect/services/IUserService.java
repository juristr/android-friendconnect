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

import java.util.List;

import javax.jdo.JDOException;

import com.friendconnect.exceptions.CaptchaException;
import com.friendconnect.model.Location;
import com.friendconnect.model.POIAlert;
import com.friendconnect.model.User;
import com.google.gdata.util.AuthenticationException;

public interface IUserService {
	
	 /**
	 * Accesses the Google server for authenticating the user
	 * @param username the username, i.e. the Google Account email of the user
	 * @param password the password, i.e. the Google Account password
	 * @return user, if the authentication was successful, null otherwise
	 * @throws AuthenticationException
	 * @throws CaptchaException
	 */
	public User authenticate(String username, String password) throws AuthenticationException, CaptchaException;

	/**
	 * Validates the received token against the user id. This method will
	 * be called on subsequent calls.
	 * @param userId the id of the user
	 * @param token the user token
	 * @return true, if the token matches the user id, false otherwise
	 */
	public boolean validateToken(String userId, String token);
	
	/**
	 * Returns the list of friends for a certain user
	 * @param userId the id of the user
	 * @return
	 */
	public List<User> getFriends(String userId);
	
	/**
	 * Adds a friend to the friend list of a certain user
	 * @param userId the id of the user
	 * @param friendId the id of the friend to add
	 */
	public void acceptFriendInvite(String userId, String friendId);
	
	/**
	 * Removes a friend from the friend list of a certain user
	 * @param userId the id of the user
	 * @param friendId the id of the friend to remove
	 */
	public void removeFriend(String userId, String friendId);

	/**
	 * Adds a new friend to the list of pending invites 
	 * @param userId the user that will be added as pending friend invite
	 * @param friendEmailAddress the friend to which the user with id userId will be added
	 */
	public void addFriendInvite(String userId, String friendEmailAddress);
	
	/**
	 * Removes a friend from the list of pending invites
	 * @param userId the user on which the pending invite should be deleted
	 * @param friendId the friend which should be removed from the list of pending invites
	 */
	public void rejectFriendInvite(String userId, String friendId);
	
	/**
	 * Updates the information of an existing FriendConnectUser
	 * @param user the {@link User} to be updated
	 */
	public void updateUser(User user);
	
	/**
	 * Updates the user location of a user
	 * @param userId the id of the user
	 * @param userLocation the current {@link Location} of the user
	 */
	public void updateUserLocation(String userId, Location userLocation);
	
	/**
	 * Returns the list of pending invites for a certain user
	 * @param userId the id of the user
	 * @return
	 */
	public List<User> getPendingInvites(String userId);
	
	/** 
	 * Returns all users whose status is set to online
	 * @return
	 */
	public List<User> getOnlineUsers();
	
	/**
	 * Returns the list of POI alerts for a certain user
	 * @param userId the id of the user
	 * @return
	 */
	public List<POIAlert> getPOIAlerts(String userId);
	
	/**
	 * Removes a POI alert from the POI alert list of a certain user
	 * @param poiAlertId the id of the POI alert to remove
	 * @throws JDOException
	 */
	public void removePOIAlert(String poiAlertId);
	
	/**
	 * Adds a new POI alert to the list of POI alerts 
	 * @param userId the user that creates the POI alert
	 * @param poiAlert the new POI alert
	 */
	public void addPOIAlert(String userId, POIAlert poiAlert);
	
	/**
	 * Updates a POI alert
	 * @param userId the user that created the POI alert
	 * @param poiAlert the updated POI alert
	 */
	public void updatePOIAlert(String userId, POIAlert poiAlert);
}
