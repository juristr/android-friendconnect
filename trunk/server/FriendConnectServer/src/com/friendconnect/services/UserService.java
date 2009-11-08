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

package com.friendconnect.services;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOException;

import com.friendconnect.dao.IUserDao;
import com.friendconnect.model.User;
import com.google.gdata.client.GoogleService;
import com.google.gdata.client.GoogleAuthTokenFactory.UserToken;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.extensions.Name;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class UserService implements IUserService {
	private IUserDao userDao;
	private String applicationName;
	private String baseURL;
	private String projection;

	@Override
	public User authenticate(String username, String password) throws AuthenticationException, JDOException {
		GoogleService contactsService = new ContactsService(applicationName);
		contactsService.setUserCredentials(username, password);
		UserToken auth_token = (UserToken) contactsService.getAuthTokenFactory().getAuthToken();
		String token = auth_token.getValue();
		User user = userDao.getUserByEmailAddress(username);
		if (user == null) {
			user = new User();
			user.setEmailAddress(username);
		} 
		user.setToken(token);
		user.setOnline(true);
		userDao.saveUser(user);
		return user;
	}

	@Override
	public boolean validateToken(String userId, String token) throws JDOException {
		User user = userDao.getUserById(userId);
		if (user.getToken().equals(token)) {
			return true;
		}
		return false;
	}
	
	@Override
	public void addFriend(String userId, String friendEmailAddress) throws JDOException {
		User friend = userDao.getUserByEmailAddress(friendEmailAddress);
		if (friend != null) {
			userDao.addFriend(userId, friend.getId());
		}
	}

	@Override
	public List<User> getFriends(String userId) throws JDOException {
		return userDao.getFriends(userId);
	}

	@Override
	public void removeFriend(String userId, String friendId) throws JDOException {
		userDao.removeFriend(userId, friendId);
	}
	
	@Override
	public void updateUser(User user) {
		userDao.saveUser(user);
	}
	
	@Override
	public List<User> getGoogleContacts(String username, String token) throws IOException, ServiceException {
		ContactsService service = new ContactsService(applicationName);
		service.setUserToken(token);
		URL feedUri = new URL(baseURL + "/" + username + "/" + projection);
		ContactFeed contactFeed = service.getFeed(feedUri, ContactFeed.class);
		return readFriendsFromFeed(service, contactFeed, feedUri);
	}
	
	/**
	 * Converts a ContactEntry object into a Friend object
	 * @param contact to convert
	 * @return friend
	 */
	private User convertToFriend(ContactEntry contact) {
		User friend = new User();
		String email = "";
		String website = "";
		String phone = "";
		String name = "";
		if (contact.hasWebsites()) {
			website = contact.getWebsites().get(0).getHref();
		}
		if (contact.hasPhoneNumbers()) {
			phone = contact.getPhoneNumbers().get(0).getPhoneNumber();
		}
		
		Name contactName = contact.getName();
        if (contactName.hasFullName()) {
        	name = contactName.getFullName().getValue();
        }
        
        if (contact.hasEmailAddresses()) {
            email = contact.getEmailAddresses().get(0).getAddress();
        }	
        
        friend.setName(name);
        friend.setEmailAddress(email);
        friend.setWebsite(website);
        friend.setPhone(phone);
        
		return friend;
	}
	
	/**
	 * Reads all friends from a given feed
	 * @param service
	 * @param feed
	 * @param feedUri
	 * @return list of friends
	 * @throws IOException
	 * @throws ServiceException
	 */
	private List<User> readFriendsFromFeed(ContactsService service, ContactFeed feed, URL feedUri) throws IOException, ServiceException {
		List<User> friends = new ArrayList<User>();
		User friend;
		for (ContactEntry entry : feed.getEntries()) {
			friend = convertToFriend(entry);
			friends.add(friend);
		}
		while (feed.getNextLink() != null) {
			feedUri = new URL(feed.getNextLink().getHref());
			feed = service.getFeed(feedUri, ContactFeed.class);
			for (ContactEntry entry : feed.getEntries()) {
				friend = convertToFriend(entry);
				friends.add(friend);
			}
		}
		return friends;
	}
	
	/* Getters and setters */
	
	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	public IUserDao getUserDao() {
		return userDao;
	}
	
	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getBaseURL() {
		return baseURL;
	}
	
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	public String getProjection() {
		return projection;
	}
	
	public void setProjection(String projection) {
		this.projection = projection;
	}
}
