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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.LayoutInflater;
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
import com.friendconnect.controller.FriendListController;
import com.friendconnect.main.IFriendConnectApplication;
import com.friendconnect.main.IoC;
import com.friendconnect.model.FriendConnectUser;
import com.friendconnect.model.User;
import com.friendconnect.services.FriendUpdateService;

public class FriendListActivity extends Activity implements IView {
	private static final int ADD_FRIEND = Menu.FIRST;
	private static final int REMOVE_FRIEND = Menu.FIRST + 1;
	private static final int PENDINGINVITES_LIST = Menu.FIRST + 2;
	private static final int PROFILE = Menu.FIRST + 3;

	private boolean lock = false;

	private Handler handler;
	private FriendListController controller;
	private IFriendConnectApplication application;
	private ListView listViewFriends;
	private ProgressDialog progressDialog;
	private Toast toast;
	private BaseAdapter adapter;
	private User selectedUser;

	static final private int FRIENDDETAILS_DIALOG = 10;
	static final private int ADDFRIEND_DIALOG = 20;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friendlist);
		handler = new Handler();
		listViewFriends = (ListView) findViewById(R.id.listViewFriends);

		progressDialog = new ProgressDialog(this);

		toast = Toast.makeText(this, R.string.uiMessageProvideEmailAddress,
				3000);

		controller = IoC.getInstance(FriendListController.class);
		controller.setLayoutId(R.layout.friendlistrowitem);
		controller.registerView(this);

		FriendConnectUser user = controller.getModel();
		((TextView) findViewById(R.id.textViewMyUsername)).setText(user
				.getEmailAddress());
		((TextView) findViewById(R.id.textViewMyStatus)).setText(user
				.getStatusMessage());

		adapter = controller.getAdapter(this);

		listViewFriends.setAdapter(this.adapter);
		listViewFriends.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectedUser = controller.getFriend(position);
				showDialog(FRIENDDETAILS_DIALOG);
			}
		});

		registerForContextMenu(listViewFriends);

		startService(new Intent(this, FriendUpdateService.class));
	}

	@Override
	public Dialog onCreateDialog(int id) {
		switch (id) {
		case (FRIENDDETAILS_DIALOG): {
			LayoutInflater li = LayoutInflater.from(this);
			View friendDetailsView = li.inflate(R.layout.frienddetailsview,
					null);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			AlertDialog friendDetailsDialog = builder.create();
			friendDetailsDialog.setTitle(R.string.details);
			friendDetailsDialog.setView(friendDetailsView);
			friendDetailsDialog.setCanceledOnTouchOutside(true);
			friendDetailsDialog.setIcon(R.drawable.icon);
			return friendDetailsDialog;
		}
		case (ADDFRIEND_DIALOG): {
			LayoutInflater li = LayoutInflater.from(this);
			View inviteFriendView = li.inflate(R.layout.friendinvite, null);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			AlertDialog inviteFriendDialog = builder.create();
			inviteFriendDialog.setTitle(R.string.invitation);
			inviteFriendDialog.setView(inviteFriendView);
			inviteFriendDialog.setCanceledOnTouchOutside(true);
			inviteFriendDialog.setIcon(R.drawable.icon);
			return inviteFriendDialog;
		}
		}
		return null;
	}

	@Override
	public void onPrepareDialog(int id, final Dialog dialog) {
		switch (id) {
		case (FRIENDDETAILS_DIALOG): {
			((TextView) dialog.findViewById(R.id.textViewName))
					.setText(selectedUser.getName());
			((TextView) dialog.findViewById(R.id.textViewPhone))
					.setText(selectedUser.getPhone());
			((TextView) dialog.findViewById(R.id.textViewEmail))
					.setText(selectedUser.getEmailAddress());
			((TextView) dialog.findViewById(R.id.textViewStatusmessage))
					.setText(selectedUser.getStatusMessage());
			break;
		}
		case (ADDFRIEND_DIALOG): {
			((Button) dialog.findViewById(R.id.buttonInvite))
					.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							EditText editTextEmailAddress = (EditText) dialog
									.findViewById(R.id.editTextInviteeEmail);
							String emailAddress = editTextEmailAddress
									.getText().toString();
							if (!emailAddress.trim().equals("")) {
								showProgressDialog(getText(R.string.uiMessageSendingInvite));
								controller.inviteFriend(emailAddress);
								editTextEmailAddress.setText("");
								dialog.dismiss();
							} else {
								toast.show();
							}
						}
					});
		}
		}
	}

	private void showProgressDialog(CharSequence message) {
		progressDialog.setMessage(message);
		progressDialog.show();
	}

	public void update(final Observable observable, final Object data) {
		handler.post(new Runnable() {
			public void run() {
				FriendConnectUser user = (FriendConnectUser)observable;
				((TextView) findViewById(R.id.textViewMyUsername)).setText(user.getEmailAddress());
				((TextView) findViewById(R.id.textViewMyStatus)).setText(user.getStatusMessage());
				
				while (lock);
				
				adapter.notifyDataSetChanged();
				listViewFriends.refreshDrawableState();
			}
		});
	}

	public void onProgressChanged(String message) {
		if (!message.equals(""))
			progressDialog.setMessage(message);
	}

	public void stopProgess() {
		progressDialog.cancel();
	}

	/** Called every time before the options menu is shown */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);

		int index = listViewFriends.getSelectedItemPosition();
		MenuItem itemRemoveFriend = menu.findItem(REMOVE_FRIEND);
		itemRemoveFriend.setVisible(index >= 0);

		return true;
	}

	/** Triggered the first time activity’s menu is displayed. */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Create and add new menu items.
		MenuItem itemAddFriend = menu.add(0, ADD_FRIEND, Menu.NONE, this
				.getString(R.string.menuAddFriend));
		MenuItem itemRemoveFriend = menu.add(0, REMOVE_FRIEND, Menu.NONE, this
				.getString(R.string.menuRemoveFriend));
		MenuItem itemPendingInvitesList = menu.add(2, PENDINGINVITES_LIST,
				Menu.NONE, this.getString(R.string.menuPendingInvitesListView));
		MenuItem itemProfile = menu.add(3, PROFILE, Menu.NONE, this
				.getString(R.string.menuEditProfile));

		// Assign icons
		itemAddFriend.setIcon(R.drawable.menu_invite);
		itemRemoveFriend.setIcon(R.drawable.menu_delete);
		itemPendingInvitesList.setIcon(R.drawable.menu_pendinginvites);
		itemProfile.setIcon(R.drawable.menu_preferences);

		// Allocate shortcuts to each of them.
		itemAddFriend.setShortcut('0', 'a');
		itemRemoveFriend.setShortcut('1', 'r');
		return true;
	}

	/** Handles activity’s menu item selections */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		// int index = this.listViewFriends.getSelectedItemPosition();
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
			startActivity(new Intent(FriendListActivity.this,
					PendingInvitesListActivity.class));
			return true;
		}
		case (PROFILE): {
			startActivity(new Intent(FriendListActivity.this,
					EditProfileActivity.class));
			return true;
		}
		}
		return false;
	}

	/**
	 * Called every time the context menu for listViewFriends is about to be
	 * shown
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Selected Friend");
		menu.add(0, REMOVE_FRIEND, Menu.NONE, R.string.menuRemoveFriend);
	}

	/** Handles context menu selections */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);
		
		AdapterView.AdapterContextMenuInfo menuInfo;
        menuInfo = (AdapterView.AdapterContextMenuInfo)
        item.getMenuInfo();
        int index = menuInfo.position;
		
		switch (item.getItemId()) {
		case (REMOVE_FRIEND): {
			progressDialog
					.setMessage(getText(R.string.uiMessageRemovingFriend));
			progressDialog.show();
			doRemoveFriendActions(index);
			return true;
		}
		}
		return false;
	}

	/**
	 * Does preparative actions for calling the controller's removeFriend method
	 */
	private void doRemoveFriendActions(int index) {
		progressDialog.setMessage(getText(R.string.uiMessageRemovingFriend));
		progressDialog.show();

		String userId = getFriendId(index);
		if (userId != null && !userId.equals("")) {
			controller.removeFriend(userId);
		}
	}

	/**
	 * Locks the friend-list to be updated and retrieves the id of the object
	 * that is being selected by the user
	 * 
	 * @return the id of the friend to remove
	 */
	private String getFriendId(int index) {
		String result;
		lock = true;
		try {
			User user = (User) listViewFriends.getItemAtPosition(index);
			result = user.getId();
		} finally {
			lock = false;
		}

		return result;
	}

	public FriendListController getController() {
		return controller;
	}
}