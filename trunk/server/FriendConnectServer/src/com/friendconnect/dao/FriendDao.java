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

import org.springframework.orm.jdo.support.JdoDaoSupport;

import com.friendconnect.model.Friend;
import com.friendconnect.model.User;

public class FriendDao extends JdoDaoSupport implements IFriendDao {
	private IUserDao userDao;
	
	public FriendDao() {
	}

	@Override
	public Friend getFriend(String friendId) {
		PersistenceManager pm = getPersistenceManager();
		Friend friend = pm.getObjectById(Friend.class, friendId);
		User user = userDao.getUserByEmailAddress(friend.getEmailAddress(), false);
		if (user != null) {
			friend.setPosition(user.getPosition());
			friend.setStatusMessage(user.getStatusMessage());
			friend.setOnline(user.isOnline());
		}
		return pm.detachCopy(friend);
	}
	
	@Override
	public List<Friend> getFriends(String userId) {
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
	public void addFriend(String userId, Friend friend) {
		User user = userDao.getUserById(userId, true);
		user.getFriends().add(friend);
		getPersistenceManager().makePersistent(user);
	}
	
	@Override
	public void removeFriend(String userId, String friendId) {
		PersistenceManager pm = getPersistenceManager();
		User user = pm.getObjectById(User.class, userId);
		Friend friend = pm.getObjectById(Friend.class, friendId);
		user.getFriends().remove(friend);
		pm.makePersistent(user);
	}

	public IUserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}
}
