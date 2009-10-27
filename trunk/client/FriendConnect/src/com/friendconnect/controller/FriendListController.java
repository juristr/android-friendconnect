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

import org.xmlrpc.android.IAsyncCallback;
import org.xmlrpc.android.ObjectSerializer;

import android.content.Context;

import com.friendconnect.adapters.FriendAdapter;
import com.friendconnect.model.Friend;
import com.friendconnect.model.RPCRemoteMappings;
import com.friendconnect.model.User;
import com.friendconnect.model.XMLRPCService;

public class FriendListController extends AbstractController<User> {
	private int layoutId; // TODO find better solution of passing this value
	private XMLRPCService xmlRPCService;

	public FriendListController(int layoutId) {
		super();
		this.layoutId = layoutId;
		
		//TODO inject
		xmlRPCService = new XMLRPCService();
	}
	
	public void loadFriends(){
		xmlRPCService.sendRequest(RPCRemoteMappings.GETFRIENDS, null, new IAsyncCallback<Object>() {

			public void onSuccess(Object result) {
				if(result == null){
					//TODO ???
				}
				
				//TODO change to get ArrayList<byte[]> or something similar
				//assuming to get a HashMap<String, byte[]>
				ObjectSerializer<Friend> friendSerializer = new ObjectSerializer<Friend>();
				
				Map<String, byte[]> encodedDataMap = (Map<String, byte[]>)result;
				for (Map.Entry<String, byte[]> entry :encodedDataMap.entrySet()) {
					Friend friend = null;
					try {
						friend = friendSerializer.deserialize(entry.getValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					if(friend != null){
						model.addFriend(friend);
					}
				}
			}
			
			public void onFailure(Throwable throwable) {
				//TODO react properly
			}
			
		});
	}

	// TODO just dummy
	public void initializeUserWithDummyFriends() {
		this.model.addFriend(new Friend(1, "Juri", "Juri", "Strumpflohner",
				"Hello World!"));
		this.model.addFriend(new Friend(2, "Matthias", "Matthias", "Braunhofer",
				"Hello FriendConnect!"));
	}

	@Override
	public FriendAdapter getAdapter(Context context) {
		return new FriendAdapter(context, this.layoutId, this.model
				.getFriends());
	}

	public void addFriend(Friend friend) {
		this.model.addFriend(friend);
	}

	public void simulateFriendsStatusMessageChange(String string) {
		this.model.getFriends().get(1).setStatusMessage(string);
	}

	public Friend getFriend(int position) {
		return this.model.getFriends().get(position);
	}
}
