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

/**
 * Represents the model part for the Login Activity. Contains
 * information about the successful outcome of a login
 * action.
 *
 */
public class LoginResult extends Observable {

	private boolean loginSucceeded = false;

	public LoginResult() {

	}

	public boolean isLoginSucceeded() {
		return loginSucceeded;
	}

	public void setLoginSucceeded(boolean loginSucceeded) {
		this.loginSucceeded = loginSucceeded;
		setChanged();
		notifyObservers();
	}

}
