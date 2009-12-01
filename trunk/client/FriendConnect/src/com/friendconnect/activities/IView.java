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

package com.friendconnect.activities;

import java.util.Observable;

/**
 * Defines the interfaces to the View part 
 * in the MVC relationship
 *
 */
public interface IView {
	
	/**
	 * Fired when the controller forwards changes from the model to
	 * update the registered views
	 * @param observable the {@link Observable} object
	 * @param data the data used for updating
	 */
	public void update(Observable observable, Object data);
	
	/**
	 * Event for listening to success notifications 
	 * @param successMessageId resource id of the success message
	 */
	public void onSuccess(int successMessageId);
	
	/**
	 * Event for listening for failure notifications
	 * @param failureMessageId resource id of the failure message
	 */
	public void onFailure(int failureMessageId);
	
	/**
	 * Event for listening to commands for stopping progress notifications
	 * on views
	 */
	public void stopProgess();
}
