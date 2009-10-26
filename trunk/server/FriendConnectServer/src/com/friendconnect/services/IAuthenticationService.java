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

package com.friendconnect.services;

/**
 * Manages the user authentication
 *
 */
public interface IAuthenticationService {
	
	/**
	 * Accesses the Google server for authenticating the user
	 * @param username the username, i.e. the Google Account email of the user
	 * @param password the password, i.e. the Google Account password
	 * @return true, if the authentication was successful
	 */
	public boolean authenticate(String username, String password);
	
	/**
	 * Validates the received token against the username. This method will
	 * be called on subsequent calls.
	 * @param username
	 * @param token
	 * @return
	 */
	public boolean validateToken(String username, String token);
	
}
