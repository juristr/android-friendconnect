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
import com.friendconnect.model.Location;
import com.friendconnect.model.User;
import com.friendconnect.server.tests.utils.BaseTest;

public class UserDaoTest extends BaseTest {
	private PersistenceManagerFactory factory;
	private UserDao userDao;
	private User user;
	private User friend;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		factory = getPersistenceManagerFactory();
		userDao = new UserDao();
		userDao.setPersistenceManagerFactory(factory);
		
		user = new User();
		user.setEmailAddress("matthias.braunhofer@gmail.com");
		user.setName("Matthias");
		
		friend = new User();
		friend.setEmailAddress("juri.strumpflohner@gmail.com");
		friend.setName("Juri");
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		factory = null;
		userDao = null;
		
		user = null;
		friend = null;
	}
	
	public void testSaveGetRemoveUser() {
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
		userDao.saveUser(user);
		userDao.saveUser(friend);
		
		userDao.addFriend(user.getId(), friend.getId());
		List<User> friends = userDao.getFriends(user.getId());
		assertTrue("User's friend list should contain friend", contains(friends, friend.getId()));
		
		userDao.removeFriend(user.getId(), friend.getId());
		friends = userDao.getFriends(user.getId());
		assertFalse("User's friend list should not contain friend", contains(friends, friend.getId()));		
	}
	
	public void testAddGetRemovePendingFriends() {
		userDao.saveUser(user);
		userDao.saveUser(friend);
		
		userDao.addPendingFriend(user.getId(), friend.getId());
		List<User> pendingFriends = userDao.getPendingFriends(user.getId());
		assertTrue("Pending friend list should contain friend", contains(pendingFriends, friend.getId()));
		
		userDao.removePendingFriend(user.getId(), friend.getId());
		pendingFriends = userDao.getPendingFriends(user.getId());
		assertFalse("Pending friend list should not contain friend", contains(pendingFriends, friend.getId()));	
	}
	
	public void testUpdateUser(){
		userDao.saveUser(user);
		
		//retrieve it from DB
		User persistedUser = userDao.getUserById(user.getId());
		assertNotNull(persistedUser);
		assertEquals("name should be the same", user.getName(), persistedUser.getName());
		
		//change name
		persistedUser.setName("Matthias Braunhofer");
		userDao.updateUser(persistedUser);
		persistedUser = null;
		
		//retrieve again
		persistedUser = userDao.getUserById(user.getId());
		assertNotNull(persistedUser);
		assertEquals("Matthias Braunhofer", persistedUser.getName());
		
	}
	
	public void testUpdateUserLocation(){
		userDao.saveUser(user);
		
		assertNull("The user's position should be null initially.",user.getPosition());
		
		Location location = new Location();
		location.setLatitude(123.39);
		location.setLongitude(12.3);
		user.setPosition(location);		
		userDao.updateUser(user);
		
		User persisted = userDao.getUserById(user.getId());
		assertNotNull(persisted);
		assertNotNull("The location shouldn't be null", persisted.getPosition());
		assertEquals("The location's latitude should match", location.getLatitude(), persisted.getPosition().getLatitude());
		assertEquals("The location's longitude should match", location.getLongitude(), persisted.getPosition().getLongitude());		
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
