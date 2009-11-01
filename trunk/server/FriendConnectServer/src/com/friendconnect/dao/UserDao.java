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

import com.friendconnect.model.User;

public class UserDao implements IUserDao {

	@Override
	public User getUserById(String userId, boolean loadFriends) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		User user = pm.getObjectById(User.class, userId);
		if (loadFriends) {
			loadFriends(user);
		}
		pm.close();
		return user;
	}
	
	private void loadFriends(User user) {
		user.setFriends(user.getFriends());
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public User getUserByEmailAddress(String emailAddress, boolean loadFriends) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(User.class);
	    query.setFilter("emailAddress == emailAddressParam");
	    query.declareParameters("String emailAddressParam");
	    List<User> result = (List<User>) query.execute(emailAddress);
		if (result == null || result.isEmpty()) {
			return null;
		}
		User user = result.get(0);
		if (loadFriends) {
			loadFriends(user);
		}
		pm.close();
		return user;
	}

	@Override
	public void removeUser(String userId) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		User user = pm.getObjectById(User.class, userId);
		if (user != null) {
			pm.deletePersistent(user);
		}
		pm.close();
	}

	@Override
	public void saveUser(User user) throws Exception {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(user);
		pm.close();
	}

	@Override
	public List<User> searchUsers(String searchText) throws Exception {
		//TODO implement search
		return null;
	}
}
