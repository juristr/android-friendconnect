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

import android.util.Log;

import com.friendconnect.main.IFriendConnectApplication;
import com.friendconnect.model.FriendConnectUser;
import com.friendconnect.xmlrpc.IAsyncCallback;
import com.friendconnect.xmlrpc.XMLRPCMethod;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class XMLRPCService implements IXMLRPCService {
	private URI baseURI;
	private XMLRPCClient client;
	private IFriendConnectApplication application;

	public XMLRPCService() {
		// Resources.getString(com.friendconnect.activities.R.string.friendConnectServerUrl);
//		String baseUrl = "http://10.0.2.2:8080/xmlrpc";  //works always
		String baseUrl = "http://0-1.latest.android-friendconnect.appspot.com/xmlrpc";
		
		this.baseURI = URI.create(baseUrl);
		
		this.client = new XMLRPCClient(baseURI); // TODO BAD, inject this later
	}

	public <T> void sendRequest(String remoteMethod, Object[] params, IAsyncCallback<T> callback, Class clazz) {
		XMLRPCMethod<T> method = new XMLRPCMethod<T>(client,
				remoteMethod, callback, clazz);
		
		Log.i(XMLRPCService.class.getCanonicalName(), "Invoking remote method '" + remoteMethod + "'");
		
		Object[] callParams = addUserCredentials(params);
		method.call(callParams);
	}
	
	/**
	 * Adds the user credentials to the parameters that are being
	 * sent to the server.
	 * E.g.: params = {"test", 2} -> {userid, token, "test", 2}
	 * @param params the original array of parameters
	 * @return
	 */
	private Object[] addUserCredentials(Object[] params){
		FriendConnectUser user = application.getApplicationModel();
		if(user == null)
			return params;
		
		if(params == null || params.length == 0){
			return new Object[]{user.getId(), user.getToken()};
		}else{
			int size = params.length + 2;	
			Object[] result = new Object[size];
			result[0] = user.getId();
			result[1] = user.getToken();
			for(int i=2; i<size; i++){
				result[i] = params[i-2];
			}
			
			return result;
		}
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

	@Inject
	public void setApplication(IFriendConnectApplication application) {
		this.application = application;
	}
	
}
