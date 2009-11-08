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

/**
 * Class containing the mappings of the server-side method
 * names that are being invoked by issuing the XML-RPC calls
 *
 */
public class RPCRemoteMappings {
	private static final String baseMapping = "XmlRpcGateway.";

	public static final String LOGIN = baseMapping + "login";
	public static final String GETFRIENDS =  baseMapping + "getFriends";
	public static final String INVITEFRIEND = baseMapping + "addFriend";
	public static final String REMOVEFRIEND = baseMapping + "removeFriend";

}
