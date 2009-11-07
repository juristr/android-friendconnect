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

public interface IFriendConnectApplication {

	/**
	 * Initializes the application model (after successful login)
	 * @param user
	 */
	public void initializeApplicationModel(FriendConnectUser user);
	
	/**
	 * Gets the main model instance of the application. There can
	 * just exist one instance per application instance
	 * @return
	 */
	public FriendConnectUser getApplicationModel();
	
}
