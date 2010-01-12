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
import android.util.Log;
import android.widget.BaseAdapter;

import com.friendconnect.R;
import com.friendconnect.main.IFriendConnectApplication;
import com.friendconnect.model.Constants;
import com.friendconnect.model.FriendConnectUser;
import com.friendconnect.model.LoginResult;
import com.friendconnect.model.RPCRemoteMappings;
import com.friendconnect.model.LoginResult.Result;
import com.friendconnect.services.IXMLRPCService;
import com.friendconnect.utils.Encrypter;
import com.friendconnect.xmlrpc.IAsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class LoginController extends AbstractController<LoginResult> {
	
	private IFriendConnectApplication application;
	private IXMLRPCService xmlRpcService;
	private Encrypter encrypter;
	
	public LoginController() {
		registerModel(new LoginResult());
	}
	
	public void login(final String emailAddress, String password){
		byte[] encrPwd = encrypter.encryptPassword(password);
		
		xmlRpcService.sendRequest(RPCRemoteMappings.LOGIN, new Object[]{emailAddress.toLowerCase(), encrPwd}, new IAsyncCallback<FriendConnectUser>() {

			public void onSuccess(FriendConnectUser result) {
				if(result == null)
					onFailure(new Exception("User was null when returning from server!"));
				
				application.setApplicationModel(result);
				
				notifyStopProgress();
				model.setLoginResult(Result.SUCCESS);
			}
			
			public void onFailure(Throwable throwable) {
				String errorMessage = throwable.getMessage();
				Log.e(LoginController.class.getCanonicalName(), errorMessage);
				notifyStopProgress();
				//Check whether the user needs to handle a captcha challenge
				if (errorMessage.contains(Constants.CAPTCHA_REQUIRED)) {
					model.setLoginResult(Result.CAPTCHA_REQUIRED);
				} else {
					notifyShowMessage(R.string.uiMessageLoginErrorMsg);
				}
			}
			
		}, FriendConnectUser.class);
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
	
	@Inject
	public void setEncrypter(Encrypter encrypter){
		this.encrypter = encrypter;
	}
	
}
