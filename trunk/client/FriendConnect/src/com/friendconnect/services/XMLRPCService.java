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

import org.xmlrpc.android.XMLRPCClient;

import com.friendconnect.xmlrpc.IAsyncCallback;
import com.friendconnect.xmlrpc.XMLRPCMethod;
import com.google.inject.Singleton;

@Singleton
public class XMLRPCService {
	private URI baseURI;
	private XMLRPCClient client;

	public XMLRPCService() {
		// Resources.getString(com.friendconnect.activities.R.string.friendConnectServerUrl);
		String baseUrl = "http://10.0.2.2:8080/xmlrpc";  //works always
//		String baseUrl = "http://0-1.latest.android-friendconnect.appspot.com/xmlrpc";
		
		this.baseURI = URI.create(baseUrl);
		
		this.client = new XMLRPCClient(baseURI); // TODO BAD, inject this later
	}

	public <T> void sendRequest(String remoteMethod, Object[] params, IAsyncCallback<T> callback, Class clazz) {
		XMLRPCMethod<T> method = new XMLRPCMethod<T>(client,
				remoteMethod, callback, clazz);
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
