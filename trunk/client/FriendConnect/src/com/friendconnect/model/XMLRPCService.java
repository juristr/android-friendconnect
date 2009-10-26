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

import java.net.URI;

import org.xmlrpc.android.IAsyncCallback;
import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCMethod;

public class XMLRPCService {
	private URI baseURI;
	private XMLRPCClient client;

	public XMLRPCService() {
		// String baseUrl =
		// Resources.getString(com.friendconnect.activities.R.string.friendConnectServerUrl);
		String baseUrl = "http://0-1.latest.android-friendconnect.appspot.com/xmlrpc"; // TODO
																						// BAD,
																						// inject
																						// this
																						// later
		this.baseURI = URI.create(baseUrl);
		this.client = new XMLRPCClient(baseURI); // TODO BAD, inject this later
	}

	public void sendRequest(Object[] params, IAsyncCallback callback) {
		XMLRPCMethod method = new XMLRPCMethod(client,
				"XMLRPCGateway.getFirstname", callback);
		if (params != null)
			method.call(params);
		else
			method.call();
	}

}
