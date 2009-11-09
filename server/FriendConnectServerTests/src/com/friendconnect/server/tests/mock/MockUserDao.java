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
import java.util.List;

import com.friendconnect.dao.IUserDao;
import com.friendconnect.model.User;

public class MockUserDao implements IUserDao {

	@Override
	public void addFriend(String userId, String friendId) {
	}

	@Override
	public void addPendingFriend(String userId, String friendId) {
	}

	@Override
	public List<User> getFriends(String userId) {
		return new ArrayList<User>();
	}

	@Override
	public List<User> getPendingFriends(String userId) {
		return new ArrayList<User>();
	}

	@Override
	public User getUserByEmailAddress(String emailAddress) {
		return null;
	}

	@Override
	public User getUserById(String userId) {
		return null;
	}

	@Override
	public void removeFriend(String userId, String friendId) {
	}

	@Override
	public void removePendingFriend(String userId, String friendId) {
	}

	@Override
	public void removeUser(String userId) {
	}

	@Override
	public void saveUser(User user) {
	}

	@Override
	public List<User> searchUsers(String searchText) {
		return new ArrayList<User>();
	}
}
