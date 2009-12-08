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
import android.widget.Toast;

import com.friendconnect.R;
import com.friendconnect.controller.POIAlertListController;
import com.friendconnect.main.IoC;
import com.friendconnect.model.POIAlert;
import com.friendconnect.utils.ActivityUtils;

public class POIAlertListActivity extends Activity implements IView {
	private static final int ADD_POIALERT = Menu.FIRST;
	private static final int REMOVE_POIALERT = Menu.FIRST + 1;

	private POIAlertListController controller;
	private ListView listViewPoiAlerts;
	private ProgressDialog progressDialog;
	private BaseAdapter adapter;
	private Handler handler;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pendinginviteslist);
		handler = new Handler();
		listViewPoiAlerts = (ListView) findViewById(R.id.listViewPoiAlerts);

		progressDialog = new ProgressDialog(this);

		controller = IoC.getInstance(POIAlertListController.class);
		controller.setLayoutId(R.layout.poilistrowitem);
		controller.registerView(this);

		adapter = controller.getAdapter(this);
		listViewPoiAlerts.setAdapter(adapter);
		
		registerForContextMenu(listViewPoiAlerts);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);

		int index = listViewPoiAlerts.getSelectedItemPosition();
		MenuItem itemRemovePOIAlert = menu.findItem(REMOVE_POIALERT);
		itemRemovePOIAlert.setVisible(index >= 0);
		
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Create and add new menu items.
		MenuItem itemAddPOIAlert = menu.add(0, ADD_POIALERT, Menu.NONE, this.getString(R.string.menuAddPoiAlert));
		MenuItem itemRemovePOIAlert = menu.add(0, REMOVE_POIALERT, Menu.NONE, this.getString(R.string.menuRemovePoiAlert));
		
		// Assign icons
		itemAddPOIAlert.setIcon(R.drawable.menu_add);
		itemRemovePOIAlert.setIcon(R.drawable.menu_delete);

		// Allocate shortcuts to each of them.
		itemAddPOIAlert.setShortcut('0', 'a');
		itemRemovePOIAlert.setShortcut('1', 'r');
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
			case (ADD_POIALERT): {
				startActivity(new Intent(POIAlertListActivity.this, EditPoiActivity.class));
				return true;
			}
			case (REMOVE_POIALERT): {
				doRemovePOIAlertActions(listViewPoiAlerts.getSelectedItemPosition());
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, REMOVE_POIALERT, Menu.NONE, R.string.menuRemovePoiAlert);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);

		AdapterView.AdapterContextMenuInfo menuInfo;
		menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int index = menuInfo.position;

		switch (item.getItemId()) {
			case (REMOVE_POIALERT): {
				doRemovePOIAlertActions(index);
				return true;
			}
		}
		return super.onContextItemSelected(item);
	}
	
	/**
	 * Does preparative actions for calling the controller's removePOIAlert method
	 */
	private void doRemovePOIAlertActions(int index) {
		final POIAlert poiAlert = getSelectedPOIAlert(index);
		AlertDialog.Builder ad = ActivityUtils.createConfirmationDialog(this, getString(R.string.dialogRemovePOIAlertTitle), String.format(getString(R.string.dialogRemovePOIAlertMessage), poiAlert.toString()));
		ad.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (poiAlert != null) {
					showProgressDialog(getText(R.string.uiMessageRemovingPOIAlert));
					controller.removePOIAlert(poiAlert.getId());
				}
			}
		});
		ad.setNegativeButton(R.string.cancel, null);
		ad.setCancelable(true);
		ad.show();
	}

	/**
	 * Retrieves the POIAlert object that is being selected by the user
	 * 
	 * @return the selected POIAlert
	 */
	private POIAlert getSelectedPOIAlert(int index) {
		return (POIAlert) listViewPoiAlerts.getItemAtPosition(index);
	}

	public void update(Observable observable, Object data) {
		handler.post(new Runnable() {
			public void run() {
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
	
	public void stopProgress() {
		progressDialog.cancel();
	}

	public void showMessage(int messageId) {
		ActivityUtils.showToast(this, messageId, Toast.LENGTH_LONG);
	}
}
