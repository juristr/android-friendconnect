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

import com.friendconnect.R;
import com.friendconnect.adapters.FriendAdapter;
import com.friendconnect.main.IFriendConnectApplication;
import com.friendconnect.model.FriendConnectUser;
import com.friendconnect.model.RPCRemoteMappings;
import com.friendconnect.model.User;
import com.friendconnect.services.IXMLRPCService;
import com.friendconnect.utils.ObjectHelper;
import com.friendconnect.xmlrpc.IAsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Controller for handling the Friend lists
 *
 */
@Singleton
public class FriendListController extends AbstractController<FriendConnectUser> {
	private int layoutId;
	private IXMLRPCService xmlRPCService;
	private ObjectHelper objectHelper;
	private LocationController locationController;

	public FriendListController() {
		super();
	}

	/**
	 * This method will be called by the FriendUpdateService for actualizing
	 * the list of friends and their according attributes such as possible
	 * status message changes or changes in their position.
	 */
	public void updateFriendList() {
		xmlRPCService.sendRequest(RPCRemoteMappings.GETFRIENDS, null, new IAsyncCallback<List<User>>() {
			public void onSuccess(List<User> result) {
				int indexLocal = 0;
				int indexServer = 0;
				while (indexLocal < model.getFriends().size() && indexServer < result.size()) {
					User localFriend = model.getFriends().get(indexLocal);
					User serverFriend = result.get(indexServer);
					int compareToResult = localFriend.toString().compareToIgnoreCase(serverFriend.toString());
					//friendNames are equal
					if (compareToResult == 0) {
						if (localFriend.getId().equals(serverFriend.getId())) {
							//sync properties
							try {
								objectHelper.syncObjectGraph(localFriend, serverFriend);
							} catch (Exception ex) {
								onFailure(ex);
							}
						} else {
							//replace local friend with server friend
							model.removeFriend(localFriend);
							model.addFriend(indexLocal, serverFriend);
						}
						//Advance both pointers
						indexLocal++;
						indexServer++;
					}
					//localFriend precedes serverFriend
					else if (compareToResult < 0) {
						//Remove localFriend
						model.removeFriend(localFriend);
					} 
					//localFriend succeeds serverFriend
					else {
						//Add remoteFriend before localFriend; advance both pointers
						model.addFriend(indexLocal, serverFriend);
						indexLocal++;
						indexServer++;
					}
				}
				
				if (indexServer < result.size()) {
					for (; indexServer < result.size(); indexServer++) {
						model.addFriend(result.get(indexServer));
					}
				} else if (indexLocal < model.getFriends().size()) {
					for (int i = model.getFriends().size() - 1; i >= indexLocal; i--) {
						model.removeFriend(model.getFriends().get(i));
					}
				}
		
				//update the distances
				locationController.updateFriendDistances();
				
				notifyStopProgress();
			}

			public void onFailure(Throwable throwable) {
				Log.e(FriendListController.class.getCanonicalName(), "Problem updating friendlist:" + throwable.getMessage());
				notifyStopProgress();
			}
		}, User.class);
	}
	
	/**
	 * This method will be called by the FriendListActivity for inviting
	 * another user.
	 */
	public void inviteFriend(String inviteeEmailAddress) {
		Object[] params = {inviteeEmailAddress};
		xmlRPCService.sendRequest(RPCRemoteMappings.INVITEFRIEND, params, new IAsyncCallback<Boolean>() {

			public void onSuccess(Boolean result) {
				notifyStopProgress();
			}
			
			public void onFailure(Throwable throwable) {			
				notifyStopProgress();
			}
			
		}, Boolean.class);
	}
	
	/**
	 * This method will be called by the FriendListActivity for removing
	 * a friend from the friend list.
	 */
	public void removeFriend(String friendId) {
		Object[] params = {friendId};
		xmlRPCService.sendRequest(RPCRemoteMappings.REMOVEFRIEND, params, new IAsyncCallback<Boolean>() {

			public void onFailure(Throwable throwable) {
				notifyStopProgress();
			}

			public void onSuccess(Boolean result) {
				notifyStopProgress();
			}
		}, Boolean.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public FriendAdapter getAdapter(Context context) {
		return new FriendAdapter(context, this.layoutId, this.model.getFriends());
	}

	public void addFriend(User friend) {
		this.model.addFriend(friend);
	}

	public User getFriend(int position) {
		return this.model.getFriends().get(position);
	}


	public void setLayoutId(int layoutId) {
		this.layoutId = layoutId;
	}

	@Inject
	public void setXmlRPCService(IXMLRPCService xmlRPCService) {
		this.xmlRPCService = xmlRPCService;
	}

	@Inject
	public void setObjectHelper(ObjectHelper objectHelper) {
		this.objectHelper = objectHelper;
	}

	@Inject
	public void setApplication(IFriendConnectApplication application) {
		this.registerModel(application.getApplicationModel());
	}

	@Inject
	public void setLocationController(LocationController locationController) {
		this.locationController = locationController;
	}
	
}
