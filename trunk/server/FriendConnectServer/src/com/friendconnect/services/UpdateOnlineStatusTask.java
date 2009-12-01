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

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import com.friendconnect.model.User;
import com.google.apphosting.api.ApiProxy;
import com.google.apphosting.api.ApiProxy.Environment;

/**
 * Task that can be scheduled to update the online/offline status of registered users
 */
public class UpdateOnlineStatusTask extends TimerTask {
	private IUserService userService;
	private Environment environment;
	
	public UpdateOnlineStatusTask() {
		environment = ApiProxy.getCurrentEnvironment();
	}

	@Override
	public void run() {
		ApiProxy.setEnvironmentForCurrentThread(environment);
		Date now = new Date();
		List<User> users = userService.getOnlineUsers();
		for (User user : users) {
			long diff = now.getTime() - user.getLastAccess().getTime();
			long diffSeconds = diff / 1000;
			//set status to offline if user did not access the server in the last 30 seconds
			if (diffSeconds > 30) {
				user.setOnline(false);
				userService.updateUser(user);
			}
		}
	}

	/* Getters and setters */
	
	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
}
