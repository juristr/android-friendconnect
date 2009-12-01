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

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.friendconnect.adapters.FriendAdapter;
import com.friendconnect.main.IFriendConnectApplication;
import com.friendconnect.model.FriendConnectUser;
import com.friendconnect.model.RPCRemoteMappings;
import com.friendconnect.model.User;
import com.friendconnect.services.IXMLRPCService;
import com.friendconnect.xmlrpc.IAsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class PendingFriendListController extends AbstractController<FriendConnectUser> {
	private int layoutId;
	private IXMLRPCService xmlRPCService;
	
	public PendingFriendListController() {
	}
	
	public void retrievePendingInvites(){
		xmlRPCService.sendRequest(RPCRemoteMappings.RETRIEVEPENDINGINVITES, null, new IAsyncCallback<List<User>>() {
			public void onSuccess(List<User> result) {
				if(result == null){
					onFailure(new Exception("Result was null"));
				}
				
				for (User userInvite : result) {
					if(!modelContainsPendingInvite(userInvite))
						model.addPendingInvite(userInvite);
				}
				
				notifyStopProgress();
			}

			public void onFailure(Throwable throwable) {
				Log.e(PendingFriendListController.class.getCanonicalName(), "Problem retrieving pending invites:" + throwable.getMessage());
				notifyStopProgress();
			}
		}, User.class);
	}
	
	/**
	 * Verifies whether the invite already exists
	 * @param userInvite
	 * @return
	 */
	private boolean modelContainsPendingInvite(User userInvite) {
		for (User tmp : model.getPendingInvites()) {
			if(tmp.getId().equals(userInvite.getId()))
				return true;
		}
		
		return false;
	}
	
	public void acceptFriend(final String friendId){
		xmlRPCService.sendRequest(RPCRemoteMappings.ACCEPTINVITE, new Object[]{friendId}, new IAsyncCallback<Boolean>() {
			public void onSuccess(Boolean result) {
				if(result){
					//remove the friendId from the list of pending friends
					removeInvite(friendId);
					notifyStopProgress();
				}else{
					onFailure(new Exception("Boolean result was false!"));
				}
			}

			public void onFailure(Throwable throwable) {
				Log.e(PendingFriendListController.class.getCanonicalName(), "Problem accepting pending invite:" + throwable.getMessage());
				notifyStopProgress();
			}
		}, Boolean.class);
	}
	
	public void rejectFriend(final String friendId){
		xmlRPCService.sendRequest(RPCRemoteMappings.REJECTINVITE, new Object[]{friendId}, new IAsyncCallback<Boolean>() {
			public void onSuccess(Boolean result) {
				if(result){
					removeInvite(friendId);
					notifyStopProgress();
				}else{
					onFailure(new Exception("Boolean result was false!"));
				}
			}

			public void onFailure(Throwable throwable) {
				Log.e(PendingFriendListController.class.getCanonicalName(), "Problem rejecting pending invite:" + throwable.getMessage());
				notifyStopProgress();
			}
		}, Boolean.class);
	}
	
	private void removeInvite(final String friendId) {
		List<User> pendingInvites = model.getPendingInvites();
		for(int i=0; i<pendingInvites.size(); i++){
			User user = model.getPendingInvites().get(i);
			if(user.getId().equals(friendId)){
				model.removePendingInvite(user);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public FriendAdapter getAdapter(Context context) {
		return new FriendAdapter(context, this.layoutId, this.model.getPendingInvites());
	}
	
	public void setLayoutId(int layoutId){
		this.layoutId = layoutId;
	}
	
	@Inject
	public void setApplication(IFriendConnectApplication application) {
		this.registerModel(application.getApplicationModel());
	}

	@Inject
	public void setXmlRPCService(IXMLRPCService xmlRPCService) {
		this.xmlRPCService = xmlRPCService;
	}
}
