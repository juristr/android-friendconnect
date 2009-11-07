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

package com.friendconnect.main;

import com.friendconnect.model.FriendConnectUser;
import com.google.inject.Singleton;

@Singleton
public class FriendConnectApplication implements IFriendConnectApplication {

	private boolean initialized = false;
	private FriendConnectUser friendConnectUser;

	/**
	 * this method will just set the model once. All subsequent
	 * calls will have no effect in order to guarantee the integrity
	 */
	public void initializeApplicationModel(FriendConnectUser user) {
		if(!initialized){
			this.friendConnectUser = user;
			initialized = true;
		}
	}
	
	public FriendConnectUser getApplicationModel() {
		return friendConnectUser;
	}
}
