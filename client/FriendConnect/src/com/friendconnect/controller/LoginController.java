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

import com.friendconnect.model.LoginResult;
import com.friendconnect.model.RPCRemoteMappings;
import com.friendconnect.services.XMLRPCService;
import com.friendconnect.xmlrpc.IAsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import android.content.Context;
import android.widget.BaseAdapter;

@Singleton
public class LoginController extends AbstractController<LoginResult> {
	
	private XMLRPCService xmlRpcService; 
	
	public LoginController() {
		registerModel(new LoginResult());
	}
	
	public void login(String username, String password){
		xmlRpcService.sendRequest(RPCRemoteMappings.LOGIN, new Object[]{username, password}, new IAsyncCallback<Object>() {

			public void onSuccess(Object result) {
				@SuppressWarnings("unused")
				String token = (String)result;
				
				model.setLoginSucceeded(true);
			}
			
			public void onFailure(Throwable throwable) {
				
			}
			
		});
	}
	
	@Override
	public BaseAdapter getAdapter(Context context) {
		return null;
	}

	@Inject
	public void setXmlRpcService(XMLRPCService xmlRpcService) {
		this.xmlRpcService = xmlRpcService;
	}

	
}
