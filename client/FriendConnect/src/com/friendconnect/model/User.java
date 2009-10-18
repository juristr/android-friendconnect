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

package com.friendconnect.model;

import java.util.ArrayList;
import java.util.List;

public class User extends Person {
	private List<Person> friends;
	private List<POIAlert> poiAlert;
	
	public User() {
		this.friends = new ArrayList<Person>();
		this.poiAlert = new ArrayList<POIAlert>();
	}

	//TODO bad, could be modified without being able to notice
	//and fire change events!!!
	public List<Person> getFriends() {
		return friends;
	}

//	public void setFriends(List<Person> friends) {
//		this.friends = friends;
//	}
	
	public void addFriend(Friend friend){
		if(!this.friends.contains(friend)){
			this.friends.add(friend);
			setChanged();
			notifyObservers();
		}
	}

	public List<POIAlert> getPoiAlert() {
		return poiAlert;
	}

	public void setPoiAlert(List<POIAlert> poiAlert) {
		this.poiAlert = poiAlert;
	}

}
