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

import java.util.List;

import com.friendconnect.dao.FriendDao;
import com.friendconnect.dao.IFriendDao;
import com.friendconnect.model.Friend;

public class FriendService implements IFriendService {

	private IFriendDao friendDao; //TODO inject
	
	public FriendService() {
		friendDao = new FriendDao();
	}
	
	
	@Override
	public List<Friend> getFriends() {
		return friendDao.readAllFriends();
	}

}