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

package com.friendconnect.activities;

import java.util.Observable;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.friendconnect.R;
import com.friendconnect.adapters.FriendAdapter;
import com.friendconnect.controller.FriendListController;
import com.friendconnect.main.IoC;
import com.friendconnect.model.FriendConnectUser;
import com.friendconnect.model.User;
import com.friendconnect.model.UserComparator;
import com.friendconnect.services.FriendUpdateService;
import com.friendconnect.services.ILocationService;
import com.friendconnect.services.LocationService;
import com.friendconnect.utils.ActivityUtils;

public class FriendListActivity extends Activity implements IView {
	private static final int ADD_FRIEND = Menu.FIRST;
	private static final int REMOVE_FRIEND = Menu.FIRST + 1;
	private static final int PENDINGINVITES_LIST = Menu.FIRST + 2;
	private static final int PROFILE = Menu.FIRST + 3;
	private static final int MAP_VIEW = Menu.FIRST + 4;

	private boolean lock = false;

	private UserComparator userComparator = new UserComparator();
	private Handler handler;
	private FriendListController controller;
	private ListView listViewFriends;
	private ProgressDialog progressDialog;
	private BaseAdapter adapter;
	private User selectedUser;

	static final private int FRIENDDETAILS_DIALOG = 10;
	static final private int ADDFRIEND_DIALOG = 20;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friendlist);
		handler = new Handler();
		listViewFriends = (ListView) findViewById(R.id.listViewFriends);

		progressDialog = new ProgressDialog(this);

		controller = IoC.getInstance(FriendListController.class);
		controller.setLayoutId(R.layout.friendlistrowitem);
		controller.registerView(this);

		FriendConnectUser user = controller.getModel();
		showFriendConnectUserInfo(user);

		adapter = controller.getAdapter(this);

		listViewFriends.setAdapter(this.adapter);
		listViewFriends.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectedUser = controller.getFriend(position);
				showDialog(FRIENDDETAILS_DIALOG);
			}
		});

		registerForContextMenu(listViewFriends);

		startService(new Intent(this, FriendUpdateService.class));
		
		ILocationService locationService = IoC.getInstance(LocationService.class);
		locationService.setSystemService(getSystemService(Context.LOCATION_SERVICE));
		locationService.startLocationTracking();
	}

	/**
	 * Shows information of the logged-in user
	 * @param user
	 */
	private void showFriendConnectUserInfo(FriendConnectUser user) {
		((TextView) findViewById(R.id.textViewMyUsername)).setText(user.toString());
		((TextView) findViewById(R.id.textViewMyStatus)).setText(user.getStatusMessage());
	}

	@Override
	public Dialog onCreateDialog(int id) {
		switch (id) {
			case (FRIENDDETAILS_DIALOG): {
				return ActivityUtils.createViewDialog(this, R.layout.frienddetailsview, R.string.details, R.drawable.icon);
			}
			case (ADDFRIEND_DIALOG): {
				return ActivityUtils.createViewDialog(this, R.layout.friendinvite, R.string.invitation, R.drawable.icon);
			}
		}
		return super.onCreateDialog(id);
	}

	@Override
	public void onPrepareDialog(int id, final Dialog dialog) {
		switch (id) {
			case (FRIENDDETAILS_DIALOG): {
				((TextView) dialog.findViewById(R.id.textViewName)).setText(selectedUser.getName());
				((TextView) dialog.findViewById(R.id.textViewPhone)).setText(selectedUser.getPhone());
				((TextView) dialog.findViewById(R.id.textViewEmail)).setText(selectedUser.getEmailAddress());
				((TextView) dialog.findViewById(R.id.textViewWebsite)).setText(selectedUser.getWebsite());
				((TextView) dialog.findViewById(R.id.textViewStatusmessage)).setText(selectedUser.getStatusMessage());
				break;
			}
			case (ADDFRIEND_DIALOG): {
				((Button) dialog.findViewById(R.id.buttonInvite)).setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							EditText editTextEmailAddress = (EditText) dialog.findViewById(R.id.editTextInviteeEmail);
							String emailAddress = editTextEmailAddress.getText().toString().trim();
							if (!emailAddress.equals("") && emailAddress.contains("@")) {
								if (!emailAddress.equals(controller.getModel().getEmailAddress())) {
									showProgressDialog(getText(R.string.uiMessageSendingInvite));
									controller.inviteFriend(emailAddress);
									editTextEmailAddress.setText("");
									editTextEmailAddress.requestFocus();
									dialog.dismiss();
								} else {
									ActivityUtils.showToast(FriendListActivity.this, R.string.uiMessageCannotInviteYourself, Toast.LENGTH_LONG);
								}

							} else {
								ActivityUtils.showToast(FriendListActivity.this, R.string.uiMessageProvideEmailAddress, Toast.LENGTH_LONG);
							}
						}
					});
				break;
			}
		}
	}

	/** 
	 * Displays a progress dialog 
	 */
	private void showProgressDialog(CharSequence message) {
		progressDialog.setMessage(message);
		progressDialog.show();
	}

	public void update(final Observable observable, final Object data) {
		handler.post(new Runnable() {
			public void run() {
				showFriendConnectUserInfo((FriendConnectUser) observable);

				while (lock);
				
				((FriendAdapter)adapter).sort(userComparator);
				adapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);

		int index = listViewFriends.getSelectedItemPosition();
		MenuItem itemRemoveFriend = menu.findItem(REMOVE_FRIEND);
		itemRemoveFriend.setVisible(index >= 0);

		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Create and add new menu items.
		MenuItem itemAddFriend = menu.add(0, ADD_FRIEND, Menu.NONE, this.getString(R.string.menuAddFriend));
		MenuItem itemRemoveFriend = menu.add(0, REMOVE_FRIEND, Menu.NONE, this.getString(R.string.menuRemoveFriend));
		MenuItem itemPendingInvitesList = menu.add(2, PENDINGINVITES_LIST, Menu.NONE, this.getString(R.string.menuPendingInvitesListView));
		MenuItem itemProfile = menu.add(3, PROFILE, Menu.NONE, this.getString(R.string.menuEditProfile));
		MenuItem itemMapView = menu.add(4, MAP_VIEW, Menu.NONE, this.getString(R.string.menuMapView));

		// Assign icons
		itemAddFriend.setIcon(R.drawable.menu_invite);
		itemRemoveFriend.setIcon(R.drawable.menu_delete);
		itemPendingInvitesList.setIcon(R.drawable.menu_pendinginvites);
		itemProfile.setIcon(R.drawable.menu_preferences);
		itemMapView.setIcon(R.drawable.menu_mapmode);

		// Allocate shortcuts to each of them.
		itemAddFriend.setShortcut('0', 'a');
		itemRemoveFriend.setShortcut('1', 'r');
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
			case (ADD_FRIEND): {
				showDialog(ADDFRIEND_DIALOG);
				return true;
			}
			case (REMOVE_FRIEND): {
				doRemoveFriendActions(listViewFriends.getSelectedItemPosition());
				return true;
			}
			case (PENDINGINVITES_LIST): {
				startActivity(new Intent(FriendListActivity.this, PendingInvitesListActivity.class));
				return true;
			}
			case (PROFILE): {
				startActivity(new Intent(FriendListActivity.this, EditProfileActivity.class));
				return true;
			}
			case (MAP_VIEW): {
				startActivity(new Intent(FriendListActivity.this, FriendMapActivity.class));
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, REMOVE_FRIEND, Menu.NONE, R.string.menuRemoveFriend);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);

		AdapterView.AdapterContextMenuInfo menuInfo;
		menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int index = menuInfo.position;

		switch (item.getItemId()) {
			case (REMOVE_FRIEND): {
				doRemoveFriendActions(index);
				return true;
			}
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * Does preparative actions for calling the controller's removeFriend method
	 */
	private void doRemoveFriendActions(int index) {
		final User friend = getSelectedFriend(index);
		AlertDialog.Builder ad = ActivityUtils.createConfirmationDialog(this, getString(R.string.dialogRemoveFriendTitle), String.format(getString(R.string.dialogRemoveFriendMessage), friend.toString()));
		ad.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (friend != null) {
					showProgressDialog(getText(R.string.uiMessageRemovingFriend));
					controller.removeFriend(friend.getId());
				}
			}
		});
		ad.setNegativeButton(R.string.cancel, null);
		ad.setCancelable(true);
		ad.show();
	}

	/**
	 * Locks the friend-list to be updated and retrieves the friend object
	 * that is being selected by the user
	 * 
	 * @return the friend to remove
	 */
	private User getSelectedFriend(int index) {
		User user;
		lock = true;
		try {
			user = (User) listViewFriends.getItemAtPosition(index);
		} finally {
			lock = false;
		}

		return user;
	}
	
	public void stopProgress() {
		progressDialog.cancel();
	}
	
	public void showMessage(int messageId) {
		ActivityUtils.showToast(this, messageId, Toast.LENGTH_SHORT);
	}

	/* Getters and setters */
	
	public FriendListController getController() {
		return controller;
	}

}