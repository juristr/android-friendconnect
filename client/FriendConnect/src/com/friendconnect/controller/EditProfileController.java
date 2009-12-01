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
import com.friendconnect.model.FriendConnectUser;
import com.friendconnect.model.RPCRemoteMappings;
import com.friendconnect.services.IXMLRPCService;
import com.friendconnect.xmlrpc.IAsyncCallback;
import com.friendconnect.xmlrpc.ObjectSerializer;
import com.google.inject.Inject;

public class EditProfileController extends AbstractController<FriendConnectUser> {
	private IFriendConnectApplication application;
	private IXMLRPCService xmlRpcService;
	private ObjectSerializer serializer;
	
	public EditProfileController() {
		super();
	}

	@Override
	public <X extends BaseAdapter> X getAdapter(Context context) {
		return null;
	}

	public void saveProfile(String name, String phone, String website, String statusMessage){
		try {
			getModel().setName(name);
			getModel().setPhone(phone);
			getModel().setWebsite(website);
			getModel().setStatusMessage(statusMessage);
			Object serializedUser = serializer.serialize(getModel());
			xmlRpcService.sendRequest(RPCRemoteMappings.UPDATEPROFILE, new Object[]{serializedUser}, new IAsyncCallback<Boolean>() {

				public void onSuccess(Boolean result) {
					notifyStopProgress();
					notifyShowMessage(R.string.uiMessageEditProfileSuccessMsg);
				}
				
				public void onFailure(Throwable throwable) {
					Log.e(EditProfileController.class.getCanonicalName(), throwable.getMessage());
					notifyStopProgress();
					notifyShowMessage(R.string.uiMessageEditProfileErrorMsg);
				}
				
			}, Boolean.class);
		} catch (Exception e) {
			Log.e(EditProfileController.class.getCanonicalName(), e.getMessage());
		}
	}
	
	@Inject
	public void setXmlRpcService(IXMLRPCService xmlRpcService) {
		this.xmlRpcService = xmlRpcService;
	}

	@Inject
	public void setApplication(IFriendConnectApplication application) {
		this.application = application;
		this.registerModel(this.application.getApplicationModel());
	}

	@Inject
	public void setSerializer(ObjectSerializer serializer) {
		this.serializer = serializer;
	}
}
