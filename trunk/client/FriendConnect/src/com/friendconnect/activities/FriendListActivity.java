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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.friendconnect.R;
import com.friendconnect.controller.FriendListController;
import com.friendconnect.main.IFriendConnectApplication;
import com.friendconnect.main.IoC;
import com.friendconnect.model.FriendConnectUser;
import com.friendconnect.model.User;
import com.friendconnect.services.FriendUpdateService;

public class FriendListActivity extends Activity implements IView {
	static final private int ADD_FRIEND = Menu.FIRST;
	static final private int REMOVE_FRIEND = Menu.FIRST + 1;

	private Handler handler;
	private FriendListController controller;
	private ListView listViewFriends;
	private ProgressDialog progressDialog;
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

		this.listViewFriends = (ListView) findViewById(R.id.listViewFriends);

		progressDialog = new ProgressDialog(this);

		this.controller = IoC.getInstance(FriendListController.class);
		this.controller.setLayoutId(R.layout.friendlistrowitem);
		this.controller.registerView(this);
//		FriendConnectUser applicationModel = ((IFriendConnectApplication)getApplication()).getApplicationModel();
//		this.controller.registerModel(applicationModel);
		this.adapter = controller.getAdapter(this);
		listViewFriends.setAdapter(this.adapter);
		listViewFriends.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectedUser = controller.getFriend(position);
				showDialog(FRIENDDETAILS_DIALOG);
			}
		});
		
		startService(new Intent(this, FriendUpdateService.class));
	}

	@Override
	public Dialog onCreateDialog(int id) {
		switch (id) {
			case (FRIENDDETAILS_DIALOG): {
				LayoutInflater li = LayoutInflater.from(this);
				View friendDetailsView = li.inflate(R.layout.frienddetailsview, null);
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				AlertDialog friendDetailsDialog = builder.create();
				friendDetailsDialog.setTitle(R.string.details);
				friendDetailsDialog.setView(friendDetailsView);
				friendDetailsDialog.setCanceledOnTouchOutside(true);
				friendDetailsDialog.setIcon(R.drawable.icon);
				return friendDetailsDialog;
			} case (ADD_FRIEND): {
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
			} case (ADDFRIEND_DIALOG): {
				((Button) dialog.findViewById(R.id.buttonInvite)).setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						//TODO send add request to the server
						String emailAddress = ((EditText) dialog.findViewById(R.id.editTextInviteeEmail)).getText().toString();
						dialog.dismiss();
					}
				});
			}
		}
	}

	public void update(final Observable observable, final Object data) {
		handler.post(new Runnable() {
			public void run() {
				adapter.notifyDataSetChanged();
				listViewFriends.refreshDrawableState();
				
				FriendConnectUser user = (FriendConnectUser)observable;
				TextView myUserName = (TextView)findViewById(R.id.textViewMyUsername);
				myUserName.setText(user.getEmailAddress());
				TextView myStatusMsg = (TextView)findViewById(R.id.textViewMyStatus);
				myStatusMsg.setText(user.getStatusMessage());
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

	/** Triggered the first time activity’s menu is displayed. */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Create and add new menu items.
		MenuItem itemInviteFriend = menu.add(0, ADD_FRIEND, Menu.NONE, this.getString(R.string.addFriend));
		MenuItem itemRemoveFriend = menu.add(1, REMOVE_FRIEND, Menu.NONE, this.getString(R.string.removeFriend));
		
		// Allocate shortcuts to each of them.
		itemInviteFriend.setShortcut('0', 'a');
		itemRemoveFriend.setShortcut('1', 'b');
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
		}
		return false;
	}

	public FriendListController getController() {
		return controller;
	}
}