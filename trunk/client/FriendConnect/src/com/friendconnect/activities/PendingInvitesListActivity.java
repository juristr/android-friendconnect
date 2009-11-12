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
import com.friendconnect.R;
import com.friendconnect.services.FriendUpdateService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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

import com.friendconnect.R;
import com.friendconnect.controller.PendingFriendListController;
import com.friendconnect.main.IoC;
import com.friendconnect.model.User;

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

	/** Triggered the first time activity’s menu is displayed. */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Create and add new menu items.
		MenuItem itemRefresh = menu.add(0, REFRESH, Menu.NONE, this
				.getString(R.string.menuRefresh));

		// Assign icons
		itemRefresh.setIcon(R.drawable.menu_refresh);

		// Allocate shortcuts to each of them.
		itemRefresh.setShortcut('0', 'a');
		return true;
	}

	/** Handles activity’s menu item selections */
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
	
	/**
	 * Called every time the context menu for listViewFriends is about to be
	 * shown
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
//		menu.setHeaderTitle("Selected Friend");
		MenuItem menuAccept = menu.add(0, ACCEPT_INVITE, Menu.NONE, R.string.menuAcceptInvite);
		MenuItem menuReject = menu.add(0, REJECT_INVITE, Menu.NONE, R.string.menuRejectInvite);
		menuAccept.setIcon(R.drawable.menu_check);
		menuReject.setIcon(R.drawable.menu_cancel);
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
		case (ACCEPT_INVITE): {
			progressDialog
					.setMessage(getText(R.string.uiMessageAcceptingInvite));
			progressDialog.show();

			String friendId = getFriendId(index);
			controller.acceptFriend(friendId);

			return true;
		}
		case (REJECT_INVITE): {
			progressDialog
					.setMessage(getText(R.string.uiMessageRejectingInvite));
			progressDialog.show();

			String friendId = getFriendId(index);
			controller.rejectFriend(friendId);

			return true;
		}
		}
		return false;
	}

	private void loadPendingInvites() {
		progressDialog.setMessage(getText(R.string.uiMessageLoadingInvites));
		progressDialog.show();
		controller.retrievePendingInvites();
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

	public void update(Observable observable, Object data) {
		handler.post(new Runnable() {
			public void run() {
				while (lock)
					;

				adapter.notifyDataSetChanged();
				listViewFriends.refreshDrawableState();
			}
		});
	}

	public void onProgressChanged(String message) {
		progressDialog.setMessage(message);
	}

	public void stopProgess() {
		progressDialog.cancel();
	}
}
