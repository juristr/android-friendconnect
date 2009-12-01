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
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.friendconnect.R;
import com.friendconnect.controller.PendingFriendListController;
import com.friendconnect.main.IoC;
import com.friendconnect.model.User;
import com.friendconnect.utils.ActivityUtils;

public class PendingInvitesListActivity extends Activity implements IView {
	private static final int REFRESH = Menu.FIRST;
	private static final int ACCEPT_INVITE = Menu.FIRST + 1;
	private static final int REJECT_INVITE = Menu.FIRST + 2;

	private PendingFriendListController controller;
	private ListView listViewFriends;
	private ProgressDialog progressDialog;
	private BaseAdapter adapter;
	private Handler handler;
	private boolean lock = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pendinginviteslist);
		handler = new Handler();
		listViewFriends = (ListView) findViewById(R.id.listViewFriends);

		progressDialog = new ProgressDialog(this);

		controller = IoC.getInstance(PendingFriendListController.class);
		controller.setLayoutId(R.layout.friendlistrowitem);
		controller.registerView(this);

		adapter = controller.getAdapter(this);
		listViewFriends.setAdapter(adapter);
		
		registerForContextMenu(listViewFriends);

		loadPendingInvites();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Create and add new menu items.
		MenuItem itemRefresh = menu.add(0, REFRESH, Menu.NONE, this.getString(R.string.menuRefresh));

		// Assign icons
		itemRefresh.setIcon(R.drawable.menu_refresh);

		// Allocate shortcuts to each of them.
		itemRefresh.setShortcut('0', 'r');
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
			case (REFRESH): {
				loadPendingInvites();
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuItem menuAccept = menu.add(0, ACCEPT_INVITE, Menu.NONE, R.string.menuAcceptInvite);
		MenuItem menuReject = menu.add(0, REJECT_INVITE, Menu.NONE, R.string.menuRejectInvite);
		menuAccept.setIcon(R.drawable.menu_check);
		menuReject.setIcon(R.drawable.menu_cancel);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);
		
		AdapterView.AdapterContextMenuInfo menuInfo;
        menuInfo = (AdapterView.AdapterContextMenuInfo)
        item.getMenuInfo();
        int index = menuInfo.position;
		
		switch (item.getItemId()) {
			case (ACCEPT_INVITE): {
				doAcceptInvitationActions(index);
				return true;
			}
			case (REJECT_INVITE): {
				doRejectInvitationActions(index);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Does preparative actions for calling the controller's acceptFriend method
	 */
	private void doAcceptInvitationActions(int index) {
		final User user = getSelectedFriend(index);
		AlertDialog.Builder ad = ActivityUtils.createConfirmationDialog(this, getString(R.string.dialogAcceptInvitationTitle), String.format(getString(R.string.dialogAcceptInvitationMessage), user.toString()));
		ad.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (user != null) {
					showProgressDialog(getText(R.string.uiMessageAcceptingInvite));
					controller.acceptFriend(user.getId());
				}
			}
		});
		ad.setNegativeButton(R.string.cancel, null);
		ad.setCancelable(true);
		ad.show();
	}
	
	/**
	 * Does preparative actions for calling the controller's rejectFriend method
	 */
	private void doRejectInvitationActions(int index) {
		final User user = getSelectedFriend(index);
		AlertDialog.Builder ad = ActivityUtils.createConfirmationDialog(this, getString(R.string.dialogRejectInvitationTitle), String.format(getString(R.string.dialogRejectInvitationMessage), user.toString()));
		ad.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (user != null) {
					showProgressDialog(getText(R.string.uiMessageRejectingInvite));
					controller.rejectFriend(user.getId());
				}
			}
		});
		ad.setNegativeButton(R.string.cancel, null);
		ad.setCancelable(true);
		ad.show();
	}

	/** 
	 * Loads the list of pending invitations.
	 */
	private void loadPendingInvites() {
		showProgressDialog(getText(R.string.uiMessageLoadingInvites));
		controller.retrievePendingInvites();
	}

	/**
	 * Locks the friend-list to be updated and retrieves the friend object
	 * that is being selected by the user
	 * 
	 * @return the selected friend
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

	public void update(Observable observable, Object data) {
		handler.post(new Runnable() {
			public void run() {
				while (lock);

				adapter.notifyDataSetChanged();
			}
		});
	}
	
	/** 
	 * Displays a progress dialog 
	 */
	private void showProgressDialog(CharSequence message) {
		progressDialog.setMessage(message);
		progressDialog.show();
	}
	
	public void stopProgess() {
		progressDialog.cancel();
	}

	public void onSuccess(int successMessageId) {
		ActivityUtils.showToast(this, successMessageId, Toast.LENGTH_SHORT);
	}
	
	public void onFailure(int failureMessageId) {
		ActivityUtils.showToast(this, failureMessageId, Toast.LENGTH_LONG);
	}
}
