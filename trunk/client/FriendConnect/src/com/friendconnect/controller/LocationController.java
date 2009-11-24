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

import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.widget.BaseAdapter;

import com.friendconnect.main.IFriendConnectApplication;
import com.friendconnect.model.FriendConnectUser;
import com.friendconnect.model.Location;
import com.friendconnect.model.RPCRemoteMappings;
import com.friendconnect.model.User;
import com.friendconnect.services.IXMLRPCService;
import com.friendconnect.xmlrpc.IAsyncCallback;
import com.friendconnect.xmlrpc.ObjectSerializer;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class LocationController extends AbstractController<FriendConnectUser> {
	private IXMLRPCService xmlRPCService;
	private ObjectSerializer serializer;

	@SuppressWarnings("unchecked")
	@Override
	public BaseAdapter getAdapter(Context context) {
		return null;
	}

	public void sendLocationUpdateToServer(Location location) {
			model.setPosition(location);
			
			Map<String, Object> locationData;
			try {
				locationData = serializer.serialize(location);

				xmlRPCService.sendRequest(
						RPCRemoteMappings.UPDATE_USERLOCATION,
						new Object[] { locationData },
						new IAsyncCallback<Boolean>() {
							public void onFailure(Throwable throwable) {
								Log.e(LocationController.class
										.getCanonicalName(), throwable
										.getMessage());
							}

							public void onSuccess(Boolean result) {
								if(result){
									updateFriendDistances();
								}else {
									onFailure(new Exception(
											"Sending of location was successful!"));
								}
							}
						}, Boolean.class);
			} catch (Exception e) {
				Log.e(LocationController.class.getCanonicalName(),
						"Serialization error:" + e.getMessage());
			}
	}
	
	public void updateFriendDistances(){
		if(model.getFriends() != null){
			for (User friend : model.getFriends()) {
				float distance = model.getPosition().convertToAndroidLocation().distanceTo(friend.getPosition().convertToAndroidLocation());
				friend.setDistanceToFriendConnectUser(distance);
			}
		}
	}

	/* Setters */
	@Inject
	public void setApplication(IFriendConnectApplication application) {
		registerModel(application.getApplicationModel());
	}

	@Inject
	public void setXmlRPCService(IXMLRPCService xmlRPCService) {
		this.xmlRPCService = xmlRPCService;
	}

	@Inject
	public void setSerializer(ObjectSerializer serializer) {
		this.serializer = serializer;
	}

}
