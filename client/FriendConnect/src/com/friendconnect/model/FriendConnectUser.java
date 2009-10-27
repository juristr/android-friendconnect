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

	public void setId(long id) {
		this.id = id;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		if (!this.emailAddress.equals(emailAddress)) {
			this.emailAddress = emailAddress;
			setChanged();
			notifyObservers();
		}
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		if (!this.firstname.equals(firstname)) {
			this.firstname = firstname;
			setChanged();
			notifyObservers();
		}
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		if (!this.surname.equals(surname)) {
			this.surname = surname;
			setChanged();
			notifyObservers();
		}
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		if (!this.statusMessage.equals(statusMessage)) {
			this.statusMessage = statusMessage;
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
