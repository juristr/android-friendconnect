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

import java.util.Comparator;

public class UserComparator implements Comparator<User> {

	public int compare(User user1, User user2) {
		final int BEFORE = -1;
		final int EQUAL = 0;
		final int AFTER = 1;

		// optimization
		if (user1 == user2)
			return EQUAL;

		// compare the user2s' string representations if they have the same
		// status
		if (user1.getOnline() == user2.getOnline()) {
			return user1.toString().compareToIgnoreCase(user2.toString());
		} else {
			return user1.getOnline() ? BEFORE : AFTER;
		}
	}

}
