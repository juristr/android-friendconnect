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

import java.io.Serializable;
import java.util.Observable;

public abstract class FriendConnectUser extends Observable implements ILoadable,
		ILocatable, Serializable {
	private static final long serialVersionUID = 1;
	protected long id;
	protected String emailAddress;
	protected String firstname;
	protected String surname;
	protected String statusMessage;
	protected Location position;
	
	public long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		String oldValue = this.emailAddress;
		this.emailAddress = emailAddress;
		
		if(oldValue == null || !oldValue.equals(this.emailAddress))
		{
			setChanged();
			notifyObservers();
		}
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		String oldValue = this.firstname;
		this.firstname = firstname;
		
		if(oldValue == null || !oldValue.equals(this.firstname)){
			setChanged();
			notifyObservers();
		}
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		String oldValue = this.surname;
		this.surname = surname;
		
		if(oldValue == null || !oldValue.equals(this.surname)){
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
		
		if(oldValue == null || !oldValue.equals(this.statusMessage)){
			setChanged();
			notifyObservers();
		}
	}

	public Location getPosition() {
		return this.position;
	}

	public void setPosition(Location location) {
		this.position = location;
	}

}
