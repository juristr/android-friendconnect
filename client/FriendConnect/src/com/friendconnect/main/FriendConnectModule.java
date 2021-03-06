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

import com.friendconnect.annotations.FriendConnectURL;
import com.friendconnect.services.IXMLRPCService;
import com.friendconnect.services.XMLRPCService;
import com.google.inject.AbstractModule;

/**
 * Module class for defining the interface->implementation
 * mapping for the Giuce DI's IoC container
 *
 */
public class FriendConnectModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IXMLRPCService.class).to(XMLRPCService.class);
		bind(IFriendConnectApplication.class).to(FriendConnectApplication.class);
//		bind(String.class).annotatedWith(FriendConnectURL.class).toInstance("http://10.0.2.2:8080/xmlrpc");
//		bind(String.class).annotatedWith(FriendConnectURL.class).toInstance("http://0-1.latest.android-friendconnect.appspot.com/xmlrpc");
		bind(String.class).annotatedWith(FriendConnectURL.class).toInstance("http://android-friendconnect.appspot.com/xmlrpc");	
	}
}
