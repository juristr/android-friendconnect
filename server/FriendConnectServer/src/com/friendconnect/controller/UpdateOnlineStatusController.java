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

package com.friendconnect.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.friendconnect.model.User;
import com.friendconnect.services.IUserService;

/**
 * Controller that updates the online/offline status of registered user. It is
 * periodically invoked by a cron job.
 */
public class UpdateOnlineStatusController extends AbstractController {
	private IUserService userService;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
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
		return null;
	}
	
	/* Getters and setters */
	
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public IUserService getUserService() {
		return userService;
	}
}
