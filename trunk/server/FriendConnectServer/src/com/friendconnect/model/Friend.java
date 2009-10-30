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

public class Friend extends FriendConnectUser {

	public Friend(){
		
	}
	
	public Friend(int id, String name, String phone, String emailAddress, String website, String statusMessage) {
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.emailAddress = emailAddress;
		this.website = website;
		this.statusMessage = statusMessage;
	}
}
