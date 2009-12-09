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
import java.util.Observable;
import java.util.Observer;

import com.friendconnect.annotations.NotSerializable;

public class FriendConnectUser extends User implements Observer {
	private List<User> friends;
	private List<POIAlert> poiAlerts;
	private List<User> pendingInvites;

	public FriendConnectUser() {
		this.friends = new ArrayList<User>();
		this.poiAlerts = new ArrayList<POIAlert>();
		this.pendingInvites = new ArrayList<User>();
	}

	// TODO bad, could be modified without being able to notice
	// and fire change events!!!
	@NotSerializable
	public List<User> getFriends() {
		return friends;
	}

	public void addFriend(User friend) {
		friends.add(friend);
		friend.addObserver(this);
		setChanged();
		notifyObservers();
	}

	public void removeFriend(User friend) {
		friends.remove(friend);
		friend.deleteObserver(this);
		setChanged();
		notifyObservers();
	}

	@NotSerializable
	public List<POIAlert> getPoiAlerts() {
		return poiAlerts;
	}

	public void addPoiAlert(POIAlert alert) {
		poiAlerts.add(alert);
		alert.addObserver(this);
		setChanged();
		notifyObservers();
	}

	public void removePoiAlert(POIAlert alert) {
		poiAlerts.remove(alert);
		alert.deleteObserver(this);
		setChanged();
		notifyObservers();
	}

	@NotSerializable
	public void setPoiAlerts(List<POIAlert> poiAlert) {
		this.poiAlerts = poiAlert;
		setChanged();
		notifyObservers();
	}

	@NotSerializable
	public List<User> getPendingInvites() {
		return pendingInvites;
	}

	public void addPendingInvite(User friend) {
		pendingInvites.add(friend);
		setChanged();
		notifyObservers();
	}

	public void removePendingInvite(User friend) {
		pendingInvites.remove(friend);
		setChanged();
		notifyObservers();
	}

	public void update(Observable observable, Object data) {
		setChanged();
		notifyObservers();
	}

}
