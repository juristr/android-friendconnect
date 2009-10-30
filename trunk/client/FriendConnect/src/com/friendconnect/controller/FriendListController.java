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

import com.friendconnect.adapters.FriendAdapter;
import com.friendconnect.model.Friend;
import com.friendconnect.model.RPCRemoteMappings;
import com.friendconnect.model.User;
import com.friendconnect.services.XMLRPCService;
import com.friendconnect.xmlrpc.IAsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class FriendListController extends AbstractController<User> {
	private int layoutId;
	private XMLRPCService xmlRPCService;

	public FriendListController() {
		super();
		registerModel(new User());
	}
	
	public void loadFriends() {
		xmlRPCService.sendRequest(RPCRemoteMappings.GETFRIENDS, null, new IAsyncCallback<List<Friend>>() {

			public void onSuccess(List<Friend> result) {
				if(result == null){
					//TODO show a message that nothing has been fetched!
					return;
				}
				
				for (Friend friend : result) {
					model.addFriend(friend);
				}
			}
			
			public void onFailure(Throwable throwable) {
				//TODO react properly
			}
			
		}, Friend.class);
	}
	

	/**
	 * This method will be called by the FriendUpdateService for actualizing
	 * the list of friends and their according attributes such as possible
	 * status message changes or changes in their position.
	 */
	public void updateFriendList() {
		xmlRPCService.sendRequest(RPCRemoteMappings.GETFRIENDS, null, new IAsyncCallback<List<Friend>>() {

			public void onSuccess(List<Friend> result) {
				//TODO sync the received list with the already existing Friend collection
			}
			
			public void onFailure(Throwable throwable) {
				// TODO Auto-generated method stub
				
			}
			
			
		}, Friend.class);
	}

	@Override
	public FriendAdapter getAdapter(Context context) {
		return new FriendAdapter(context, this.layoutId, this.model
				.getFriends());
	}

	public void addFriend(Friend friend) {
		this.model.addFriend(friend);
	}

	public Friend getFriend(int position) {
		return this.model.getFriends().get(position);
	}


	public void setLayoutId(int layoutId) {
		this.layoutId = layoutId;
	}

	@Inject
	public void setXmlRPCService(XMLRPCService xmlRPCService) {
		this.xmlRPCService = xmlRPCService;
	}
}
