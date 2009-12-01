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

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.springframework.orm.jdo.support.JdoDaoSupport;

import com.friendconnect.model.User;

public class UserDao extends JdoDaoSupport implements IUserDao {
	
	@Override
	public void saveUser(User user) {
		if (user.getId() == null) {
			getPersistenceManager().makePersistent(user);
		} else {
			updateUser(user);
		}
	}
	
	private void updateUser(User user) {
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.currentTransaction().begin();
			User storedUser = pm.getObjectById(User.class, user.getId());
			storedUser.setEmailAddress(user.getEmailAddress());
			storedUser.setName(user.getName());
			storedUser.setPhone(user.getPhone());
			storedUser.setWebsite(user.getWebsite());
			storedUser.setToken(user.getToken());
			storedUser.setStatusMessage(user.getStatusMessage());
			storedUser.setOnline(user.getOnline());
			storedUser.setPosition(user.getPosition());
			pm.currentTransaction().commit();
		} finally {
			if (pm.currentTransaction().isActive()) {
				pm.currentTransaction().rollback();
			}
		}
	}
	
	@Override
	public User getUserById(String userId) {
		PersistenceManager pm = getPersistenceManager();
		User user = pm.getObjectById(User.class, userId);
		return pm.detachCopy(user);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public User getUserByEmailAddress(String emailAddress) {
		PersistenceManager pm = getPersistenceManager();
		Query query = pm.newQuery(User.class);
	    query.setFilter("emailAddress == emailAddressParam");
	    query.declareParameters("String emailAddressParam");
	    List<User> result = (List<User>) query.execute(emailAddress);
		if (result == null || result.isEmpty()) {
			return null;
		}
		return pm.detachCopy(result.get(0));
	}

	@Override
	public void removeUser(String userId) {
		PersistenceManager pm = getPersistenceManager();
		User user = pm.getObjectById(User.class, userId);
		pm.deletePersistent(user);
	}
	
	@Override
	public List<User> getFriends(String userId) {
		PersistenceManager pm = getPersistenceManager();
		User user = pm.getObjectById(User.class, userId);
		List<User> friends = new ArrayList<User>();
		if (user.getFriends() != null) {
			for (String friendId : user.getFriends()) {
				User friend = pm.getObjectById(User.class, friendId);
				friend.setToken(null);
				friends.add(friend);
			}
		}
		return friends;
	}
	
	@Override
	public List<User> getPendingFriends(String userId) {
		PersistenceManager pm = getPersistenceManager();
		User user = pm.getObjectById(User.class, userId);
		List<User> pendingFriends = new ArrayList<User>();
		if (user.getPendingFriends() != null) {
			for (String pendingFriendEmailAddress : user.getPendingFriends()) {
				User pendingFriend = pm.getObjectById(User.class, pendingFriendEmailAddress);
				pendingFriend.setToken(null);
				pendingFriends.add(pendingFriend);
			}
		}
		return pendingFriends;
	}
	
	@Override	
	public void addFriend(String userId, String friendId) {
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.currentTransaction().begin();
			User user = pm.getObjectById(User.class, userId);
			List<String> friends;
			if (user.getFriends() == null) {
				friends = new ArrayList<String>();
			} else {
				friends = user.getFriends();
			}
			friends.add(friendId);
			user.setFriends(friends);
			pm.currentTransaction().commit();
		} finally {
			if (pm.currentTransaction().isActive()) {
				pm.currentTransaction().rollback();
			}
		}
	}
	
	@Override
	public void removeFriend(String userId, String friendId) {
		removeFriendFrom(userId, friendId);
		removeFriendFrom(friendId, userId);
	}
	
	private void removeFriendFrom(String userId, String friendId) {
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.currentTransaction().begin();
			User user = pm.getObjectById(User.class, userId);
			if (user.getFriends() != null) {
				List<String> friends = user.getFriends();
				friends.remove(friendId);
				user.setFriends(friends);
			}
			pm.currentTransaction().commit();
		} finally {
			if (pm.currentTransaction().isActive()) {
				pm.currentTransaction().rollback();
			}
		}
	}
	
	@Override
	public void addPendingFriend(String userId, String friendId) {
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.currentTransaction().begin();
			User user = pm.getObjectById(User.class, userId);
			List<String> pendingFriends;
			if (user.getPendingFriends() == null) {
				pendingFriends = new ArrayList<String>();
			} else {
				pendingFriends = user.getPendingFriends();
			}
			pendingFriends.add(friendId);
			user.setPendingFriends(pendingFriends);
			pm.currentTransaction().commit();
		} finally {
			if (pm.currentTransaction().isActive()) {
				pm.currentTransaction().rollback();
			}
		}
	}
	
	@Override
	public void removePendingFriend(String userId, String friendId) {
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.currentTransaction().begin();
			User user = pm.getObjectById(User.class, userId);
			if (user.getPendingFriends() != null) {
				List<String> pendingFriends = user.getPendingFriends();
				pendingFriends.remove(friendId);
				user.setPendingFriends(pendingFriends);
			}
			pm.currentTransaction().commit();
		} finally {
			if (pm.currentTransaction().isActive()) {
				pm.currentTransaction().rollback();
			}
		}
	}

	@Override
	public List<User> searchUsers(String searchText) {
		//TODO implement search
		return null;
	}
}
