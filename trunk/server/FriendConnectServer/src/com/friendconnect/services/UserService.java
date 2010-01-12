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

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.friendconnect.dao.IUserDao;
import com.friendconnect.exceptions.CaptchaException;
import com.friendconnect.model.Location;
import com.friendconnect.model.POIAlert;
import com.friendconnect.model.User;
import com.google.gdata.client.GoogleService;
import com.google.gdata.client.GoogleAuthTokenFactory.UserToken;
import com.google.gdata.client.GoogleService.CaptchaRequiredException;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.util.AuthenticationException;

public class UserService implements IUserService {
	private IUserDao userDao;
	private String applicationName;
	
	@Override
	public User authenticate(String username, String password) throws AuthenticationException, CaptchaException {
		try {
			GoogleService contactsService = new ContactsService(applicationName);
			contactsService.setUserCredentials(username, password);
			UserToken auth_token = (UserToken) contactsService.getAuthTokenFactory().getAuthToken();
			String token = auth_token.getValue();
			User user = userDao.getUserByEmailAddress(username);
			if (user == null) {
				user = new User();
				user.setEmailAddress(username);
			} 
			user.setToken(token);
			user.setLastAccess(new Date());
			user.setOnline(true);
			userDao.saveUser(user);
			return user;
		} catch (CaptchaRequiredException e) {
			throw new CaptchaException(e.getCaptchaUrl());
		}
	}

	@Override
	public boolean validateToken(String userId, String token) {
		User user = userDao.getUserById(userId);
		if (user.getToken().equals(token)) {
			return true;
		}
		return false;
	}

	@Override
	public void addFriendInvite(String userId, String friendEmailAddress) {
		User friend = userDao.getUserByEmailAddress(friendEmailAddress);
		if (friend != null) {
			if(!isFriendAlreadyInvited(getPendingInvites(friend.getId()), userId))
				userDao.addPendingFriend(friend.getId(), userId);
		}
	}
	
	/**
	 * Helper method that determines whether a friend has already been invited.
	 * @param pendingInvites
	 * @param userId
	 * @return
	 */
	private boolean isFriendAlreadyInvited(List<User> pendingInvites, String userId){
		for (User invitedUser : pendingInvites) {
			if(invitedUser.getId().equals(userId))
				return true;
		}
		
		return false;
	}

	@Override
	public void rejectFriendInvite(String userId, String friendId) {
		userDao.removePendingFriend(userId, friendId);
	}

	@Override
	public void acceptFriendInvite(String userId, String friendId) {
		userDao.removePendingFriend(userId, friendId);

		// add both as friends
		userDao.addFriend(userId, friendId);
		userDao.addFriend(friendId, userId);
	}

	@Override
	public List<User> getFriends(String userId) {
		User user = userDao.getUserById(userId);
		user.setLastAccess(new Date());
		user.setOnline(true);
		userDao.saveUser(user);
		
		List<User> friends = userDao.getFriends(userId);
		return friends;
	}
	
	@Override
	public List<User> getPendingInvites(String userId) {
		List<User> pendingFriends = userDao.getPendingFriends(userId);
		Collections.sort(pendingFriends);
		return pendingFriends;
	}

	@Override
	public void removeFriend(String userId, String friendId) {
		userDao.removeFriend(userId, friendId);
		userDao.removeFriend(friendId, userId);
	}

	@Override
	public void updateUser(User user) {
		userDao.saveUser(user);
	}
	
	@Override
	public void updateUserLocation(String userId, Location userLocation){
		User user = userDao.getUserById(userId);
		user.setPosition(userLocation);
		
		userDao.saveUser(user);
	}
	
	@Override
	public List<User> getOnlineUsers() {
		return userDao.getOnlineUsers();
	}
	
	@Override
	public void addPOIAlert(String userId, POIAlert poiAlert) {
		userDao.savePOIAlert(userId, poiAlert);
	}

	@Override
	public List<POIAlert> getPOIAlerts(String userId) {
		return userDao.getPOIAlerts(userId);
	}

	@Override
	public void removePOIAlert(String poiAlertId) {
		userDao.removePOIAlert(poiAlertId);
	}

	@Override
	public void updatePOIAlert(String userId, POIAlert poiAlert) {
		userDao.savePOIAlert(userId, poiAlert);
	}

	/* Getters and setters */

	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	public IUserDao getUserDao() {
		return userDao;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
}
