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
import android.os.Bundle;
import android.os.Handler;
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
import com.friendconnect.model.User;
import com.friendconnect.view.IView;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class FriendListActivity extends Activity implements IView {
	static final private int MVC_TEST = Menu.FIRST;

	private FriendListController controller;
	private ListView listViewFriends;
	private BaseAdapter adapter;
	private Friend selectedUser;

	static final private int FRIENDDETAILS_DIALOG = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friendlist);

		this.listViewFriends = (ListView) findViewById(R.id.listViewFriends);

		// TODO just dummy instantiation here
		this.controller = IoC.getInstance(FriendListController.class);
		this.controller.setLayoutId(R.layout.friendlistrowitem);
		this.controller.registerModel(new User()); // TODO just dummy
		this.controller.initializeUserWithDummyFriends();
		this.controller.registerObserver(this);

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
			((TextView) dialog.findViewById(R.id.textViewNickname))
					.setText(selectedUser.getEmailAddress());
			((TextView) dialog.findViewById(R.id.textViewFirstname))
					.setText(selectedUser.getFirstname());
			((TextView) dialog.findViewById(R.id.textViewSurname))
					.setText(selectedUser.getSurname());
			((TextView) dialog.findViewById(R.id.textViewStatusmessage))
					.setText(selectedUser.getStatusMessage());
			break;
		}
	}

	public void update(Observable observable, Object data) {
		// provocate rebinding of the friendlist
		this.adapter.notifyDataSetChanged();
	}

	/** Triggered the first time activity’s menu is displayed. */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Create and add new menu items.
		MenuItem itemAdd = menu.add(0, MVC_TEST, Menu.NONE, "MVC test");

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
			case (MVC_TEST): {
				// simulate some common operations to test the MVC binding
				simulateMVCBindings();
				controller.loadFriends();
				
				
				
//				XMLRPCService xmlRPCServ = new XMLRPCService();
//				xmlRPCServ.sendRequest(null, new IAsyncCallback(){
//
//					@SuppressWarnings("unchecked")
//					public void onSuccess(Object result) {
////						Map<String, Serializable> map = (Map<String, Serializable>)result;
////						
//						Friend friend = null;
//						
//						try {
////							Friend friend = (Friend)deserialize((byte[])map.get("juri.strumpflohner@gmail.com"));
//							friend = (Friend)deserialize((byte[])result);
//						} catch (StreamCorruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (ClassNotFoundException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//
//
//					public void onFailure(Throwable throwable) {
//						// TODO Auto-generated method stub						
//					}
//				});
				
				
				return true;
			}
		}
		return false;
	}
	
	public FriendListController getController() {
		return controller;
	}

	/*
	 * These operations could potentially take place in another class having the
	 * same model instance
	 */
	private void simulateMVCBindings() {
		this.controller.addFriend(new Friend(3, "Test friend", "Test friend",
				"some", "Hi there, I'm new here!"));
		this.controller
				.simulateFriendsStatusMessageChange("FriendConnect rocks!!");
	}
}