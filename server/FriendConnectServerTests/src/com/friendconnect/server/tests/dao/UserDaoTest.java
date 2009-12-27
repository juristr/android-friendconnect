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
	private POIAlert poiAlert;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		factory = getPersistenceManagerFactory();
		userDao = new UserDao();
		userDao.setPersistenceManagerFactory(factory);
		
		user = new User();
		user.setEmailAddress("matthias@friendconnect.com");
		user.setOnline(true);
		user.setName("Matthias");
		Location userLocation = new Location();
		userLocation.setLatitude(123.39);
		userLocation.setLongitude(12.3);
		user.setPosition(userLocation);
		
		friend = new User();
		friend.setEmailAddress("juri@friendconnect.com");
		friend.setOnline(false);
		friend.setName("Juri");
		Location friendLocation = new Location();
		friendLocation.setLatitude(0.0);
		friendLocation.setLongitude(0.0);
		friend.setPosition(friendLocation);
		
		poiAlert = new POIAlert();
		poiAlert.setActivated(true);
		poiAlert.setExpirationDate(new Date());
		poiAlert.setRadius(100);
		poiAlert.setTitle("Title");
		Location poiLocation = new Location();
		poiLocation.setLatitude(-10.0);
		poiLocation.setLongitude(20.0);
		poiAlert.setPosition(poiLocation);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		factory = null;
		userDao = null;
			
		user = null;
		friend = null;
	
		poiAlert = null;
	}
	
	public void testSaveGetRemoveUser() {
		//User id should be null, since the user object has not yet been persisted
		assertNull("User id should be null", user.getId());
		
		userDao.saveUser(user);
		
		//User id shouldn't be null anymore, since the user object has been persisted
		assertNotNull("User id should not be null", user.getId());
		
		//Retrieve user object from the JDO datastore by specifying its id
		User aUser = userDao.getUserById(user.getId());
		
		assertNotNull("User should not be null", aUser);
		assertEquals("Ids of users should be equal", user.getId(), aUser.getId());
		assertEquals("Names of users should be equal", user.getName(), aUser.getName());
		assertEquals("Emails of users should be equal", user.getEmailAddress(), aUser.getEmailAddress());
		assertEquals("Status of users should be equal", user.getOnline(), aUser.getOnline());
		assertNotNull("User's position should not be null", aUser.getPosition());
		assertEquals("Latitude of users should be equal", user.getPosition().getLatitude(), aUser.getPosition().getLatitude());
		assertEquals("Longitude of users should be equal", user.getPosition().getLongitude(), aUser.getPosition().getLongitude());
		
		//Retrieve user object from the JDO datastore by specifying its e-mail address
		User anotherUser = userDao.getUserByEmailAddress(user.getEmailAddress());
		
		assertNotNull("User should not be null", anotherUser);
		assertEquals("Ids of users should be equal", user.getId(), anotherUser.getId());
		assertEquals("Names of users should be equal", user.getName(), anotherUser.getName());
		assertEquals("Emails of users should be equal", user.getEmailAddress(), anotherUser.getEmailAddress());
		assertEquals("Status of users should be equal", user.getOnline(), anotherUser.getOnline());
		assertNotNull("User's position should not be null", anotherUser.getPosition());
		assertEquals("Latitude of users should be equal", user.getPosition().getLatitude(), anotherUser.getPosition().getLatitude());
		assertEquals("Longitude of users should be equal", user.getPosition().getLongitude(), anotherUser.getPosition().getLongitude());
		
		//Remove user from the JDO datastore
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
		
		//Initially friend list should be empty
		List<User> friends = userDao.getFriends(user.getId());
		assertNotNull("User's friend list should not be null", friends);
		assertTrue("User's friend list should be empty", friends.size() == 0);
		
		//Add a friend in the JDO datastore
		userDao.addFriend(user.getId(), friend.getId());
		friends = userDao.getFriends(user.getId());
		assertNotNull("User's friend list should not be null", friends);
		assertTrue("User's friend list should contain friend", contains(friends, friend.getId()));
		
		//Remove the friend from the JDO datastore
		userDao.removeFriend(user.getId(), friend.getId());
		friends = userDao.getFriends(user.getId());
		assertNotNull("User's friend list should not be null", friends);
		assertFalse("User's friend list should not contain friend", contains(friends, friend.getId()));		
	}
	
	public void testAddGetRemovePendingFriends() {
		userDao.saveUser(user);
		userDao.saveUser(friend);
		
		//Initially pending friend list should be empty
		List<User> pendingFriends = userDao.getPendingFriends(user.getId());
		assertNotNull("User's pending friend list should not be null", pendingFriends);
		assertTrue("User's pending friend list should be empty", pendingFriends.size() == 0);
		
		//Add a pending friend in the JDO datastore
		userDao.addPendingFriend(user.getId(), friend.getId());
		pendingFriends = userDao.getPendingFriends(user.getId());
		assertNotNull("User's pending friend list should not be null", pendingFriends);
		assertTrue("User's pending friend list should contain friend", contains(pendingFriends, friend.getId()));
		
		//Remove the pending friend from the JDO datastore
		userDao.removePendingFriend(user.getId(), friend.getId());
		pendingFriends = userDao.getPendingFriends(user.getId());
		assertNotNull("User's pending friend list should not be null", pendingFriends);
		assertFalse("User's pending friend list should not contain friend", contains(pendingFriends, friend.getId()));	
	}
	
	public void testUpdateUser(){
		userDao.saveUser(user);
		
		//Retrieve 
		User persistedUser = userDao.getUserById(user.getId());
		assertNotNull("User should not be null", persistedUser);
		assertEquals("Names of users should be the equal", user.getName(), persistedUser.getName());
		
		//Change name
		String newName = "Matthias Braunhofer";
		persistedUser.setName(newName);
		userDao.saveUser(persistedUser);
		persistedUser = null;
		
		//Retrieve again
		persistedUser = userDao.getUserById(user.getId());
		assertNotNull("User should not be null", persistedUser);
		assertEquals("Names should be equal", newName, persistedUser.getName());
	}
	
	public void testUpdateUserLocation(){
		userDao.saveUser(user);
		
		//Retrieve
		User persistedUser = userDao.getUserById(user.getId());
		assertNotNull("User should not be null", persistedUser);
		assertNotNull("User's position should not be null", persistedUser.getPosition());
		assertEquals("Latitude of users should be the equal", user.getPosition().getLatitude(), persistedUser.getPosition().getLatitude());
		assertEquals("Longitude of users should be the equal", user.getPosition().getLongitude(), persistedUser.getPosition().getLongitude());
		
		//Change location
		Location newLocation = new Location();
		newLocation.setLatitude(100.0);
		newLocation.setLongitude(100.0);
		user.setPosition(newLocation);		
		userDao.saveUser(user);
		persistedUser = null;
		
		//Retrieve again
		persistedUser = userDao.getUserById(user.getId());
		assertNotNull("User should not be null", persistedUser);
		assertNotNull("User's position should not be null", persistedUser.getPosition());
		assertEquals("Latitudes should be the equal", newLocation.getLatitude(), persistedUser.getPosition().getLatitude());
		assertEquals("Longitudes should be the equal", newLocation.getLongitude(), persistedUser.getPosition().getLongitude());
	}
	
	public void testSaveGetRemovePOIAlert() {
		userDao.saveUser(user);
		
		
		//Initially POI alert list should be empty
		List<POIAlert> poiAlerts = userDao.getPOIAlerts(user.getId());
		assertNotNull("User's POI alert list should not be null", poiAlerts);
		assertTrue("User's POI alert list should be empty", poiAlerts.size() == 0);
		
		//Add a POI alert in the JDO datastore
		userDao.savePOIAlert(user.getId(), poiAlert);
		poiAlerts = userDao.getPOIAlerts(user.getId());
		assertNotNull("User's POI alert list should not be null", poiAlerts);
		assertTrue("User's POI alert list should contain POI alert", contains(poiAlerts, poiAlert.getId()));
		
		POIAlert persistedPOIAlert = poiAlerts.get(0);
		
		assertEquals("POI alert's id should match", poiAlert.getId(), persistedPOIAlert.getId());
		assertEquals("POI alert's title should match", poiAlert.getTitle(), persistedPOIAlert.getTitle());
		assertEquals("POI alert's expiration date should match", poiAlert.getExpirationDate(), persistedPOIAlert.getExpirationDate());
		assertEquals("POI alert's radius should match", poiAlert.getRadius(), persistedPOIAlert.getRadius());
		assertEquals("POI alert's activation status should match", poiAlert.getActivated(), persistedPOIAlert.getActivated());
		assertEquals("POI alert's latitude should match", poiAlert.getPosition().getLatitude(), persistedPOIAlert.getPosition().getLatitude());
		assertEquals("POI alert's longitude should match", poiAlert.getPosition().getLongitude(), persistedPOIAlert.getPosition().getLongitude());
		
		//Remove the POI alert from the JDO datastore
		userDao.removePOIAlert(poiAlert.getId());
		poiAlerts = userDao.getPOIAlerts(user.getId());
		assertNotNull("User's POI alert list should not be null", poiAlerts);
		assertFalse("User's POI alert list should not contain POI alert", contains(poiAlerts, poiAlert.getId()));		
	}
	
	public void testUpdatePOIAlert() {
		userDao.saveUser(user);
		userDao.savePOIAlert(user.getId(), poiAlert);
		
		//Retrieve
		POIAlert persistedPOIAlert = userDao.getPOIAlert(poiAlert.getId());
		assertNotNull("POI alert should not be null", persistedPOIAlert);
		assertEquals("Title should be the same", poiAlert.getTitle(), persistedPOIAlert.getTitle());
		
		//Change title
		String newTitle = "New Title";
		persistedPOIAlert.setTitle(newTitle);
		userDao.savePOIAlert(user.getId(), persistedPOIAlert);
		persistedPOIAlert = null;
		
		//Retrieve again
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
