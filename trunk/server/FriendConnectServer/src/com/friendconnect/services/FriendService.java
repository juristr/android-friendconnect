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

import com.friendconnect.dao.FriendDao;
import com.friendconnect.dao.IFriendDao;
import com.friendconnect.model.Friend;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.extensions.Name;
import com.google.gdata.util.ServiceException;

public class FriendService implements IFriendService {
	private final String applicationName = "FriendConnect";//TODO inject
	private final String baseURL = "http://www.google.com/m8/feeds/contacts"; //TODO inject
	private final String projection = "thin";
	
	private IFriendDao friendDao; //TODO inject
	
	public FriendService() {
		friendDao = new FriendDao();
	}
	
	/**
	 * Converts a ContactEntry object into a Friend object
	 * @param contact to convert
	 * @return friend
	 */
	private Friend convertToFriend(ContactEntry contact) {
		Friend friend = new Friend();
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
       
        System.out.println("Email: " + email);
        System.out.println("Website: " + website);
        System.out.println("Phone: " + phone);
        System.out.println("Name: " + name);
	    
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
	private List<Friend> readFriendsFromFeed(ContactsService service, ContactFeed feed, URL feedUri) throws IOException, ServiceException {
		List<Friend> friends = new ArrayList<Friend>();
		Friend friend;
		for (ContactEntry entry : feed.getEntries()) {
			friend = convertToFriend(entry);
			//TODO read further friend information from DB
			friends.add(friend);
		}
		while (feed.getNextLink() != null) {
			feedUri = new URL(feed.getNextLink().getHref());
			feed = service.getFeed(feedUri, ContactFeed.class);
			for (ContactEntry entry : feed.getEntries()) {
				friend = convertToFriend(entry);
				//TODO read further friend information from DB
				friends.add(friend);
			}
		}
		return friends;
	}
	
	/**
	 * Returns all friends of a certain user
	 */
	@Override
	public List<Friend> getFriends(String username, String token) throws IOException, ServiceException {
		ContactsService service = new ContactsService(applicationName);
		service.setUserToken(token);
		URL feedUri = new URL(baseURL + "/" + username + "/" + projection);
		ContactFeed contactFeed = service.getFeed(feedUri, ContactFeed.class);
		return readFriendsFromFeed(service, contactFeed, feedUri);
	}
	
	public List<Friend> getDummyFriends() {
		return friendDao.readAllFriends();
	}

}
