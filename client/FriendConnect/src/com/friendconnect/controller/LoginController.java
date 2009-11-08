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
import com.friendconnect.model.User;
import com.friendconnect.services.IXMLRPCService;
import com.friendconnect.xmlrpc.IAsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class LoginController extends AbstractController<LoginResult> {
	
	private IFriendConnectApplication application;
	private IXMLRPCService xmlRpcService;
	
	public LoginController() {
	}
	
	public void init() {
		registerModel(new LoginResult());
	}
	
	public void login(final String emailAddress, String password){
		xmlRpcService.sendRequest(RPCRemoteMappings.LOGIN, new Object[]{emailAddress, password}, new IAsyncCallback<User>() {

			public void onSuccess(User result) {
				FriendConnectUser user = new FriendConnectUser();
				user.setId(result.getId());
				user.setName(result.getName());
				user.setToken(result.getToken());
				user.setEmailAddress(result.getEmailAddress());
				user.setPhone(result.getPhone());
				user.setWebsite(result.getWebsite());
				user.setPosition(result.getPosition());
				user.setStatusMessage("Fake stat msg");
				application.initializeApplicationModel(user);
				
				notifyStopProgress();
				
				model.setLoginSucceeded(true);
			}
			
			public void onFailure(Throwable throwable) {
				notifyStopProgress();
			}
			
		}, User.class);
	}
	
	@SuppressWarnings("unchecked")
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
