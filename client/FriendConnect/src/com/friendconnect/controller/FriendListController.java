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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.friendconnect.adapters.FriendAdapter;
import com.friendconnect.model.Friend;
import com.friendconnect.model.RPCRemoteMappings;
import com.friendconnect.model.User;
import com.friendconnect.services.XMLRPCService;
import com.friendconnect.xmlrpc.IAsyncCallback;
import com.friendconnect.xmlrpc.ObjectSerializer;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class FriendListController extends AbstractController<User> {
	private int layoutId;
	private XMLRPCService xmlRPCService;
	private ObjectSerializer friendSerializer = new ObjectSerializer(); //TODO inject this

	public FriendListController() {
		super();
	}
	
	public void loadFriends() {
		xmlRPCService.sendRequest(RPCRemoteMappings.GETFRIENDS, null, new IAsyncCallback<Object>() {

			@SuppressWarnings("unchecked")
			public void onSuccess(Object result) {
				if(result == null){
					//TODO show a message that nothing has been fetched!
					return;
				}
				
				List<Friend> friends = processSerializedFriendList((Object[])result);
				for (Friend friend : friends) {
					model.addFriend(friend);
				}
			}
			
			public void onFailure(Throwable throwable) {
				//TODO react properly
			}
			
		});
	}
	

	/**
	 * This method will be called by the FriendUpdateService for actualizing
	 * the list of friends and their according attributes such as possible
	 * status message changes or changes in their position.
	 */
	public void updateFriendList() {
		xmlRPCService.sendRequest(RPCRemoteMappings.GETFRIENDS, null, new IAsyncCallback<Object>() {

			@SuppressWarnings("unchecked")
			public void onSuccess(Object result) {
				if(result == null){
					//TODO show a message that nothing has been fetched!
					return;
				}
				
				List<Friend> friends = processSerializedFriendList((Object[])result);
				//TODO synch changes in the existing list of friends
			}
			
			public void onFailure(Throwable throwable) {
				//TODO react properly
			}
			
		});
	}
	
	private List<Friend> processSerializedFriendList(Object[] list){
		List<Friend> result = new ArrayList<Friend>();
		
		for (Object entry : list) {
			Friend friend = null;
			try {
				friend = friendSerializer.deSerialize((Map<String, Object>) entry, Friend.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(friend != null){
				result.add(friend);
			}
		}
		
		return result;
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
