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

import java.util.Observable;
import java.util.Observer;

import com.friendconnect.xmlrpc.ComplexSerializableType;

public class User extends Observable implements Observer {
	protected String id;
	protected String emailAddress;
	protected String phone;
	protected String website;
	protected String name;
	protected String token;
	protected String statusMessage;
	protected Location position;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		String oldValue = this.emailAddress;
		this.emailAddress = emailAddress;

		if (oldValue == null || !oldValue.equals(this.emailAddress)) {
			setChanged();
			notifyObservers();
		}
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		String oldValue = this.phone;
		this.phone = phone;

		if (oldValue == null || !oldValue.equals(this.phone)) {
			setChanged();
			notifyObservers();
		}
	}

	public String getName() {
		return name;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		String oldValue = this.website;
		this.website = website;

		if (oldValue == null || !oldValue.equals(this.website)) {
			setChanged();
			notifyObservers();
		}
	}

	public void setName(String name) {
		String oldValue = this.name;
		this.name = name;

		if (oldValue == null || !oldValue.equals(this.name)) {
			setChanged();
			notifyObservers();
		}
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		String oldValue = this.statusMessage;
		this.statusMessage = statusMessage;

		if (oldValue == null || !oldValue.equals(this.statusMessage)) {
			setChanged();
			notifyObservers();
		}
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@ComplexSerializableType(clazz = Location.class)
	public Location getPosition() {
		return this.position;
	}

	@ComplexSerializableType(clazz = Location.class)
	public void setPosition(Location location) {
		this.position = location;
		if (location != null) {
			location.addObserver(this);
		}
	}
	
	public String toString(){
		if(name == null || name.equals(""))
			return emailAddress;
		
		return name;
	}

	public void update(Observable observable, Object data) {
		setChanged();
		notifyObservers();
	}
}
