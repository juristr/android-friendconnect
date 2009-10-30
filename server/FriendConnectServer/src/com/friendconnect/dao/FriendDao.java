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

package com.friendconnect.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.friendconnect.model.Friend;

public class FriendDao implements IFriendDao {
	private Map<String, Friend> dummyFriends;
	
	public FriendDao() {
		this.dummyFriends = new HashMap<String, Friend>();
		initWithSomeFriends();
	}
	
	private void initWithSomeFriends(){
		dummyFriends.put("matthias.braunhofer@gmail.com", new Friend(1, "Matthias", "", "matthias.braunhofer@gmail.com", "", ""));
		dummyFriends.put("stephi050385@gmail.com", new Friend(2, "Steffi","", "stephi050385@gmail.com", "", ""));
	}
	
	@Override
	public List<Friend> readAllFriends() {
		return new ArrayList<Friend>(dummyFriends.values());
	}
}
