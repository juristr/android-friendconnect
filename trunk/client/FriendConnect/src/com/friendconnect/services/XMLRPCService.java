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

import java.net.URI;

import org.xmlrpc.android.IAsyncCallback;
import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCMethod;

import com.google.inject.Singleton;

@Singleton
public class XMLRPCService {
	private URI baseURI;
	private XMLRPCClient client;

	public XMLRPCService() {
		// String baseUrl =
		// Resources.getString(com.friendconnect.activities.R.string.friendConnectServerUrl);
		String baseUrl = "http://10.7.196.6/xmlrpc"; // TODO
																						// BAD,
																						// inject
																						// this
																						// later
		this.baseURI = URI.create(baseUrl);
		
		this.client = new XMLRPCClient(baseURI); // TODO BAD, inject this later
	}

	public void sendRequest(String remoteMethod, Object[] params, IAsyncCallback callback) {
		XMLRPCMethod method = new XMLRPCMethod(client,
				remoteMethod, callback);
		if (params != null)
			method.call(params);
		else
			method.call();
	}

	public URI getBaseURI() {
		return baseURI;
	}

	public void setBaseURI(URI baseURI) {
		this.baseURI = baseURI;
	}

	public XMLRPCClient getClient() {
		return client;
	}

	public void setClient(XMLRPCClient client) {
		this.client = client;
	}
}
