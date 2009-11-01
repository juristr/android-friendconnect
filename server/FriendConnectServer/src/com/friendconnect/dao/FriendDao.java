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

import com.friendconnect.model.Friend;
import com.friendconnect.model.User;

public class FriendDao implements IFriendDao {
	private IUserDao userDao;
	
	public FriendDao() {
	}

	@Override
	public Friend getFriend(String friendId) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Friend friend = pm.getObjectById(Friend.class, friendId);
		if (friend != null) {
			User user = userDao.getUserByEmailAddress(friend.getEmailAddress(), false);
			if (user != null) {
				friend.setPosition(user.getPosition());
				friend.setStatusMessage(user.getStatusMessage());
				friend.setOnline(user.isOnline());
			}
		}
		pm.close();
		return friend;
	}
	
	@Override
	public List<Friend> getFriends(String userId) throws Exception {
		User user = userDao.getUserById(userId, true);
		List<Friend> friends = user.getFriends();
		for (Friend friend : friends) {
			User friendUser = userDao.getUserByEmailAddress(friend.getEmailAddress(), false);
			if (friendUser != null) {
				friend.setPosition(friendUser.getPosition());
				friend.setStatusMessage(friendUser.getStatusMessage());
				friend.setOnline(friendUser.isOnline());
			}
		}
		return friends;
	}
	
	@Override
	public void addFriend(String userId, Friend friend) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		User user = userDao.getUserById(userId, true);
		user.getFriends().add(friend);
		pm.makePersistent(user);
		pm.close();
	}
	
	@Override
	public void removeFriend(String userId, String friendId) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		User user = pm.getObjectById(User.class, userId);
		Friend friend = pm.getObjectById(Friend.class, friendId);
		if (user != null && friend != null) {
			user.getFriends().remove(friend);
			pm.makePersistent(user);
		}
		pm.close();
	}

	public IUserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}
}
