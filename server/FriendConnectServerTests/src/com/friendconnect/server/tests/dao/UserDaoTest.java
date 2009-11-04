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

package com.friendconnect.server.tests.dao;

import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManagerFactory;

import com.friendconnect.dao.UserDao;
import com.friendconnect.model.User;
import com.friendconnect.server.tests.utils.BaseTest;

public class UserDaoTest extends BaseTest {
	private PersistenceManagerFactory factory;
	private UserDao userDao;
	private String userEmailAddress;
	private String friendEmailAddress;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		factory = getPersistenceManagerFactory();
		userDao = new UserDao();
		userDao.setPersistenceManagerFactory(factory);
		
		userEmailAddress = "matthias.braunhofer@gmail.com";
		friendEmailAddress = "juri.strumpflohner@gmail.com";
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		factory = null;
		userDao = null;
		
		userEmailAddress = null;
		friendEmailAddress = null;
	}
	
	public void testSaveGetRemoveUser() {
		User user = new User();
		user.setEmailAddress(userEmailAddress);
		user.setName("Matthias");
		
		userDao.saveUser(user);
		
		User aUser = userDao.getUserById(user.getId());
		
		assertNotNull("User should not be null", aUser);
		assertEquals("Ids of users should be equal", user.getId(), aUser.getId());
		assertEquals("Names of users should be equal", user.getName(), aUser.getName());
		assertEquals("Emails of users should be equal", user.getEmailAddress(), aUser.getEmailAddress());
		
		User anotherUser = userDao.getUserByEmailAddress(user.getEmailAddress());
		
		assertNotNull("User should not be null", anotherUser);
		assertEquals("Ids of users should be equal", user.getId(), anotherUser.getId());
		assertEquals("Names of users should be equal", user.getName(), anotherUser.getName());
		assertEquals("Emails of users should be equal", user.getEmailAddress(), anotherUser.getEmailAddress());	
	
		userDao.removeUser(user.getId());
		boolean exceptionThrown = false;
		try {
			user = userDao.getUserById(user.getId());
		} catch (JDOObjectNotFoundException e) {
			exceptionThrown = true;
		}
		assertTrue("JDOObjectNotFoundException should be thrown", exceptionThrown);
	}
	
	public void testAddGetRemoveFriends() {
		User user = new User();
		user.setEmailAddress(userEmailAddress);
		user.setName("Matthias");
		userDao.saveUser(user);
		
		User friend = new User();
		friend.setEmailAddress(friendEmailAddress);
		friend.setName("Juri");
		userDao.saveUser(friend);
		
		userDao.addFriend(user.getId(), friend.getId());
		List<User> friends = userDao.getFriends(user.getId());
		assertTrue("Friend list should contain friend", contains(friends, friend.getId()));
		
		userDao.removeFriend(user.getId(), friend.getId());
		friends = userDao.getFriends(user.getId());
		assertFalse("Friend list should not contain friend", contains(friends, friend.getId()));	
	}
	
	public void testAddGetRemovePendingFriends() {
		User user = new User();
		user.setEmailAddress(userEmailAddress);
		user.setName("Matthias");
		userDao.saveUser(user);
		
		User pendingFriend = new User();
		pendingFriend.setEmailAddress(friendEmailAddress);
		pendingFriend.setName("Juri");
		userDao.saveUser(pendingFriend);
		
		userDao.addPendingFriend(user.getId(), pendingFriend.getId());
		List<User> pendingFriends = userDao.getPendingFriends(user.getId());
		assertTrue("Pending friend list should contain friend", contains(pendingFriends, pendingFriend.getId()));
		
		userDao.removePendingFriend(user.getId(), pendingFriend.getId());
		pendingFriends = userDao.getPendingFriends(user.getId());
		assertFalse("Pending friend list should not contain friend", contains(pendingFriends, pendingFriend.getId()));	
	}
	
	private boolean contains(List<User> users, String userId) {
		for (User u : users) {
			if (u.getId().equals(userId)) {
				return true;
			}
		}
		return false;
	}
}
