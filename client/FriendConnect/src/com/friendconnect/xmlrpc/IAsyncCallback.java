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

package com.friendconnect.xmlrpc;

/**
 * Callback for notifying about the outcome
 * of asynchronous calls (to the server)
 *
 * @param <T>
 */
public interface IAsyncCallback<T> {
	
	/**
	 * On successful outcome
	 * @param result the result object
	 */
	public void onSuccess(T result);
	
	/**
	 * In case of an error
	 * @param throwable the exception
	 */
	public void onFailure(Throwable throwable);
}
