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

import java.util.Date;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManagerFactory;

import com.friendconnect.dao.UserDao;
import com.friendconnect.model.IIdentity;
import com.friendconnect.model.Location;
import com.friendconnect.model.POIAlert;
import com.friendconnect.model.User;
import com.friendconnect.server.tests.utils.BaseTest;

public class UserDaoTest extends BaseTest {
	private PersistenceManagerFactory factory;
	private UserDao userDao;
	private User user;
	private User friend;
	private Location location;
	private POIAlert poiAlert;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		factory = getPersistenceManagerFactory();
		userDao = new UserDao();
		userDao.setPersistenceManagerFactory(factory);
		
		user = new User();
		user.setEmailAddress("matthias.braunhofer@gmail.com");
		user.setOnline(true);
		user.setName("Matthias");
		
		friend = new User();
		friend.setEmailAddress("juri.strumpflohner@gmail.com");
		friend.setOnline(false);
		friend.setName("Juri");
		
		location = new Location();
		location.setLatitude(123.39);
		location.setLongitude(12.3);
		
		poiAlert = new POIAlert();
		poiAlert.setActivated(true);
		poiAlert.setAddress("POI alert address");
		poiAlert.setExpirationDate(new Date());
		poiAlert.setRadius(100);
		poiAlert.setTitle("Title");
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		factory = null;
		userDao = null;
		
		user = null;
		friend = null;
		
		location = null;
		poiAlert = null;
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
		userDao.saveUser(persistedUser);
		persistedUser = null;
		
		//retrieve again
		persistedUser = userDao.getUserById(user.getId());
		assertNotNull(persistedUser);
		assertEquals("Matthias Braunhofer", persistedUser.getName());
		
	}
	
	public void testUpdateUserLocation(){
		userDao.saveUser(user);
		
		assertNull("The user's position should be null initially.",user.getPosition());
		
		user.setPosition(location);		
		userDao.saveUser(user);
		
		User persisted = userDao.getUserById(user.getId());
		assertNotNull(persisted);
		assertNotNull("The location shouldn't be null", persisted.getPosition());
		assertEquals("The location's latitude should match", location.getLatitude(), persisted.getPosition().getLatitude());
		assertEquals("The location's longitude should match", location.getLongitude(), persisted.getPosition().getLongitude());		
	}
	
	public void testGetOnlineUsers() {
		List<User> result = userDao.getOnlineUsers();
		
		assertNotNull("The result list shouldn't be null", result);
		assertEquals("The result list should contain no entry", result.size(), 0);
		
		userDao.saveUser(user);
		userDao.saveUser(friend);
		
		result = userDao.getOnlineUsers();
		
		assertNotNull("The result list shouldn't be null", result);
		assertEquals("The result list should contain one entry", result.size(), 1);	
	}
	
	public void testSaveGetRemovePOIAlert() {
		userDao.saveUser(user);
		
		poiAlert.setPosition(location);
		
		userDao.savePOIAlert(user.getId(), poiAlert);
		
		List<POIAlert> poiAlerts = userDao.getPOIAlerts(user.getId());
		assertTrue("User's POI alert list should contain POI alert", contains(poiAlerts, poiAlert.getId()));
		
		POIAlert persistedPOIAlert = poiAlerts.get(0);
		
		assertEquals("POI alert's id should match", poiAlert.getId(), persistedPOIAlert.getId());
		assertEquals("POI alert's title should match", poiAlert.getTitle(), persistedPOIAlert.getTitle());
		assertEquals("POI alert's expiration date should match", poiAlert.getExpirationDate(), persistedPOIAlert.getExpirationDate());
		assertEquals("POI alert's radius should match", poiAlert.getRadius(), persistedPOIAlert.getRadius());
		assertEquals("POI alert's activation status should match", poiAlert.getActivated(), persistedPOIAlert.getActivated());
		assertEquals("POI alert's address should match", poiAlert.getAddress(), persistedPOIAlert.getAddress());
		assertEquals("POI alert's latitude should match", poiAlert.getPosition().getLatitude(), persistedPOIAlert.getPosition().getLatitude());
		assertEquals("POI alert's longitude should match", poiAlert.getPosition().getLongitude(), persistedPOIAlert.getPosition().getLongitude());
		
		userDao.removePOIAlert(poiAlert.getId());
		poiAlerts = userDao.getPOIAlerts(user.getId());
		assertFalse("User's POI alert list should not contain POI alert", contains(poiAlerts, poiAlert.getId()));		
	}
	
	public void testUpdatePOIAlert() {
		userDao.saveUser(user);
		poiAlert.setPosition(location);
		userDao.savePOIAlert(user.getId(), poiAlert);
		
		//retrieve it from DB
		POIAlert persistedPOIAlert = userDao.getPOIAlert(poiAlert.getId());
		assertNotNull("POI alert should not be null", persistedPOIAlert);
		assertEquals("Title should be the same", poiAlert.getTitle(), persistedPOIAlert.getTitle());
		
		//change title
		String newTitle = "New Title";
		persistedPOIAlert.setTitle(newTitle);
		userDao.savePOIAlert(user.getId(), persistedPOIAlert);
		persistedPOIAlert = null;
		
		//retrieve again
		persistedPOIAlert = userDao.getPOIAlert(poiAlert.getId());
		assertNotNull("POI alert should not be null", persistedPOIAlert);
		assertEquals("Title should be the same", newTitle, persistedPOIAlert.getTitle());
	}
	
	private <T extends IIdentity> boolean contains(List<T> elements, String elementId) {
		for (T element : elements) {
			if (element.getId().equals(elementId)) {
				return true;
			}
		}
		return false;
	}
}
