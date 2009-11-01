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

import com.friendconnect.dao.IUserDao;
import com.google.gdata.client.GoogleService;
import com.google.gdata.client.GoogleAuthTokenFactory.UserToken;
import com.google.gdata.client.GoogleService.InvalidCredentialsException;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.util.AuthenticationException;

public class AuthenticationService implements IAuthenticationService {
	private final String applicationName = "FriendConnect";//TODO inject
	private IUserDao userDao;
	
	@Override
	public String authenticate(String username, String password) throws AuthenticationException {
		GoogleService contactsService = new ContactsService(applicationName);
		try {
			contactsService.setUserCredentials(username, password);
			UserToken auth_token = (UserToken) contactsService.getAuthTokenFactory().getAuthToken();
			String token = auth_token.getValue();	
			//TODO save username and token into DB
			return token;
		} catch (InvalidCredentialsException e) {
			return null;
		}
	}

	@Override
	public boolean validateToken(String username, String token) {
		//TODO check in DB whether username has given token
		return true;
	}

	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	public IUserDao getUserDao() {
		return userDao;
	}
}