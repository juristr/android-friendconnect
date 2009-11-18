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

package com.friendconnect.server.tests.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.friendconnect.dao.IUserDao;
import com.friendconnect.model.User;

public class MockUserDao implements IUserDao {
	
	private Map<String, User> dummyUserStore = new HashMap<String, User>();
	
	
	@Override
	public void addFriend(String userId, String friendId) {
		User user = dummyUserStore.get(userId);
		if(user.getFriends() == null)
			user.setFriends(new ArrayList<String>());
		user.getFriends().add(friendId);
	}

	@Override
	public void addPendingFriend(String userId, String friendId) {
		User user = dummyUserStore.get(userId);
		if(user.getPendingFriends() == null)
			user.setPendingFriends(new ArrayList<String>());
		user.getPendingFriends().add(friendId);
	}

	@Override
	public List<User> getFriends(String userId) {
		return new ArrayList<User>();
	}

	@Override
	public List<User> getPendingFriends(String userId) {
		List<User> result = new ArrayList<User>();
		List<String> pendingFriends =  dummyUserStore.get(userId).getPendingFriends();
		
		if(pendingFriends == null)
			return result; //empty list
		
		for (String id : pendingFriends) {
			result.add(dummyUserStore.get(id));
		}
		
		return result;
	}

	@Override
	public User getUserByEmailAddress(String emailAddress) {
		for (User user : dummyUserStore.values()) {
			if(user.getEmailAddress().equals(emailAddress))
				return user;
		}
		return null;
	}

	@Override
	public User getUserById(String userId) {
		return dummyUserStore.get(userId);
	}

	@Override
	public void removeFriend(String userId, String friendId) {
		User user = dummyUserStore.get(userId);
		user.getFriends().remove(friendId);
	}

	@Override
	public void removePendingFriend(String userId, String friendId) {
		User user = dummyUserStore.get(userId);
		user.getPendingFriends().remove(friendId);
	}

	@Override
	public void removeUser(String userId) {
		dummyUserStore.remove(userId);
	}
	
	@Override
	public void updateUser(User user) {
		dummyUserStore.remove(user.getId());
		dummyUserStore.put(user.getId(), user);
	}

	@Override
	public void saveUser(User user) {
		dummyUserStore.put(user.getId(), user);
	}

	@Override
	public List<User> searchUsers(String searchText) {
		return new ArrayList<User>();
	}
}
