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

import com.friendconnect.xmlrpc.IAsyncCallback;

/**
 * Interface for the XMLRPCService
 *
 */
public interface IXMLRPCService {

	/**
	 * Issues an XML-RPC request
	 * @param <T>
	 * @param remoteMethod the server-side method to be called
	 * @param params {@link Object[]} representing the method parameters
	 * @param callback {@link IAsyncCallback} callback
	 * @param baseClazz the base Class of the object that should be deserialized
	 */
	public <T> void sendRequest(String remoteMethod, Object[] params, IAsyncCallback<T> callback, Class baseClazz);

}
