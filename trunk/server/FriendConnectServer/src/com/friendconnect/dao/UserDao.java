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
	public User getUserById(String userId) {
		return getPersistenceManager().getObjectById(User.class, userId);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public User getUserByEmailAddress(String emailAddress) {
		PersistenceManager pm = getPersistenceManager();
		Query query = pm.newQuery(User.class);
	    query.setFilter("emailAddress == emailAddressParam");
	    query.declareParameters("String emailAddressParam");
	    List<User> result = (List<User>) query.execute(emailAddress);
		if (result.isEmpty()) {
			return null;
		}
		return result.get(0);
	}

	@Override
	public void removeUser(String userId) {
		PersistenceManager pm = getPersistenceManager();
		User user = pm.getObjectById(User.class, userId);
		pm.deletePersistent(user);
	}

	@Override
	public void saveUser(User user) {
		getPersistenceManager().makePersistent(user);
	}
	
	@Override
	public List<User> getFriends(String userId) {
		PersistenceManager pm = getPersistenceManager();
		User user = pm.getObjectById(User.class, userId);
		List<User> friends = new ArrayList<User>();
		if (user.getFriends() != null) {
			for (String friendId : user.getFriends()) {
				User friend = pm.getObjectById(User.class, friendId);
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
			for (String pendingFriendId : user.getPendingFriends()) {
				User pendingFriend = pm.getObjectById(User.class, pendingFriendId);
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
		PersistenceManager pm = getPersistenceManager();
		try {
			pm.currentTransaction().begin();
			User user = pm.getObjectById(User.class, userId);
			if (user.getFriends() != null) {
				List<String> friends = user.getFriends();
				friends.remove(friends.indexOf(friendId));
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
				pendingFriends.remove(pendingFriends.indexOf(friendId));
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
