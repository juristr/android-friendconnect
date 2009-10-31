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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.friendconnect.controller.FriendListController;
import com.friendconnect.main.IoC;
import com.friendconnect.model.Friend;

public class FriendListActivity extends Activity implements IView {
	static final private int FETCH_FRIENDS_TEST = Menu.FIRST;

	private FriendListController controller;
	private ListView listViewFriends;
	private ProgressDialog progressDialog;
	private BaseAdapter adapter;
	private Friend selectedUser;

	static final private int FRIENDDETAILS_DIALOG = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friendlist);

		this.listViewFriends = (ListView) findViewById(R.id.listViewFriends);

		progressDialog = new ProgressDialog(this);

		this.controller = IoC.getInstance(FriendListController.class);
		this.controller.setLayoutId(R.layout.friendlistrowitem);
		this.controller.registerView(this);

		this.adapter = controller.getAdapter(this);
		listViewFriends.setAdapter(this.adapter);
		listViewFriends.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectedUser = controller.getFriend(position);
				showDialog(FRIENDDETAILS_DIALOG);
			}
		});
	}

	@Override
	public Dialog onCreateDialog(int id) {
		switch (id) {
		case (FRIENDDETAILS_DIALOG):
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
		return null;
	}

	@Override
	public void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case (FRIENDDETAILS_DIALOG):
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
	}

	public void update(Observable observable, Object data) {
		progressDialog.cancel();
		this.adapter.notifyDataSetChanged();
	}

	public void onProgressChanged(String message) {
		if (!message.equals(""))
			progressDialog.setMessage(message);
	}

	/** Triggered the first time activity’s menu is displayed. */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Create and add new menu items.
		MenuItem itemAdd = menu.add(0, FETCH_FRIENDS_TEST, Menu.NONE,
				"fetch friends test");

		// Allocate shortcuts to each of them.
		itemAdd.setShortcut('0', 'a');
		return true;
	}

	/** Handles activity’s menu item selections */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		// int index = this.listViewFriends.getSelectedItemPosition();
		switch (item.getItemId()) {
			case (FETCH_FRIENDS_TEST): {
				progressDialog.setMessage(getText(R.string.uiMessageLoadingFriends));
				progressDialog.show();
				controller.updateFriendList();
				return true;
			}
		}
		return false;
	}

	public FriendListController getController() {
		return controller;
	}
}