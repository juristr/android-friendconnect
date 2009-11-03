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

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.springframework.orm.jdo.support.JdoDaoSupport;

import com.friendconnect.model.User;

public class UserDao extends JdoDaoSupport implements IUserDao {
	
	@Override
	public User getUserById(String userId) {
//		PersistenceManager pm = getPersistenceManager();
//		User user = pm.getObjectById(User.class, userId);
//		return pm.detachCopy(user);
		return (User)getJdoTemplate().getObjectById(User.class, userId);
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
		return pm.detachCopy(result.get(0));
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
		return pm.detachCopy(user.getFriends());
	}
	
	@Override
	public List<User> getPendingFriendRequests(String userId) {
		PersistenceManager pm = getPersistenceManager();
		User user = pm.getObjectById(User.class, userId);
		return pm.detachCopy(user.getFriends());
	}
	
	@Override
	public void addFriend(String userId, String friendId) {
		PersistenceManager pm = getPersistenceManager();
		User user = pm.getObjectById(User.class, userId);
		User friend = pm.getObjectById(User.class, friendId);
		user.getFriends().add(friend);
		pm.makePersistent(user);
	}
	
	@Override
	public void removeFriend(String userId, String friendId) {
		PersistenceManager pm = getPersistenceManager();
		User user = pm.getObjectById(User.class, userId);
		User friend = pm.getObjectById(User.class, friendId);
		user.getFriends().remove(friend);
		pm.makePersistent(user);
	}
	
	@Override
	public void addPendingFriendRequest(String userId, String friendId) {
		PersistenceManager pm = getPersistenceManager();
		User user = pm.getObjectById(User.class, userId);
		User friend = pm.getObjectById(User.class, friendId);
		user.getFriends().add(friend);
		pm.makePersistent(user);
	}
	
	@Override
	public void removePendingFriendRequest(String userId, String friendId) {
		PersistenceManager pm = getPersistenceManager();
		User user = pm.getObjectById(User.class, userId);
		User friend = pm.getObjectById(User.class, friendId);
		user.getFriends().remove(friend);
		pm.makePersistent(user);
	}

	@Override
	public List<User> searchUsers(String searchText) {
		//TODO implement search
		return null;
	}
}
