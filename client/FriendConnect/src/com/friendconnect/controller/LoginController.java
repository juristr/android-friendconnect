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

package com.friendconnect.controller;

import android.content.Context;
import android.widget.BaseAdapter;

import com.friendconnect.main.IFriendConnectApplication;
import com.friendconnect.model.FriendConnectUser;
import com.friendconnect.model.LoginResult;
import com.friendconnect.model.RPCRemoteMappings;
import com.friendconnect.services.IXMLRPCService;
import com.friendconnect.xmlrpc.IAsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class LoginController extends AbstractController<LoginResult> {
	
	private IFriendConnectApplication application;
	private IXMLRPCService xmlRpcService;
	
	public LoginController() {
		registerModel(new LoginResult());
	}
	
	public void login(final String username, String password){
		xmlRpcService.sendRequest(RPCRemoteMappings.LOGIN, new Object[]{username, password}, new IAsyncCallback<String>() {

			public void onSuccess(String result) {
				@SuppressWarnings("unused")
				String token = result;
				
				model.setLoginSucceeded(true);
				
				//TODO initialize the application user...should login respond with FriendConnectUser object??
				FriendConnectUser user = new FriendConnectUser();
				user.setEmailAddress(username);
				user.setLoginToken(token);
				user.setStatusMessage("");
				application.initializeApplicationModel(user);				
				notifyStopProgress();
			}
			
			public void onFailure(Throwable throwable) {
				notifyStopProgress();
				model.setLoginSucceeded(false);
			}
			
		}, String.class);
	}
	
	@Override
	public BaseAdapter getAdapter(Context context) {
		return null;
	}

	@Inject
	public void setXmlRpcService(IXMLRPCService xmlRpcService) {
		this.xmlRpcService = xmlRpcService;
	}

	@Inject
	public void setApplication(IFriendConnectApplication application) {
		this.application = application;
	}
	
}
