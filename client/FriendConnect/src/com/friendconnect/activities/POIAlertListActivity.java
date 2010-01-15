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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.friendconnect.R;
import com.friendconnect.controller.POIAlertListController;
import com.friendconnect.main.IoC;
import com.friendconnect.model.POIAlert;
import com.friendconnect.utils.ActivityUtils;

public class POIAlertListActivity extends AuthenticationActivity implements IView {
	private static final int REMOVE_POIALERT = Menu.FIRST;
	private static final int NAVIGATE_TO_ALERT = Menu.FIRST + 1;

	private static final int POIDIALOGVIEW = 1;
	private static final int SUBACTIVITY_EDITPOI = 2;
	
	private POIAlertListController controller;
	private ListView listViewPoiAlerts;
	private BaseAdapter adapter;
	private Handler handler;
	private POIAlert selectedPOIAlert;
	
	public void onAuthenticated() {
		setContentView(R.layout.poilist);
		handler = new Handler();
		listViewPoiAlerts = (ListView) findViewById(R.id.listViewPoiAlerts);
		
		controller = IoC.getInstance(POIAlertListController.class);
		controller.setLayoutId(R.layout.poilistrowitem);
		controller.registerView(this);

		adapter = controller.getAdapter(this);
		listViewPoiAlerts.setAdapter(adapter);
		listViewPoiAlerts.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectedPOIAlert = controller.getPOIAlert(position);
				showDialog(POIDIALOGVIEW);
			}	
		});
		
		registerForContextMenu(listViewPoiAlerts);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);

		int index = listViewPoiAlerts.getSelectedItemPosition();
		MenuItem itemRemovePOIAlert = menu.findItem(REMOVE_POIALERT);
		itemRemovePOIAlert.setVisible(index >= 0);
		MenuItem itemLocateOnMap = menu.findItem(NAVIGATE_TO_ALERT);
		itemLocateOnMap.setVisible(index >= 0);
		
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Create and add new menu items.
		MenuItem itemRemovePOIAlert = menu.add(0, REMOVE_POIALERT, Menu.NONE, R.string.menuRemovePoiAlert);
		MenuItem itemNavigateToAlert = menu.add(0, NAVIGATE_TO_ALERT, Menu.NONE, R.string.menuShowOnMap);
		
		// Assign icons
		itemRemovePOIAlert.setIcon(R.drawable.menu_delete);
		itemNavigateToAlert.setIcon(R.drawable.menu_locateonmap);

		// Allocate shortcuts to each of them.
		itemRemovePOIAlert.setShortcut('1', 'r');
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case (REMOVE_POIALERT): {
				doRemovePOIAlertActions(listViewPoiAlerts.getSelectedItemPosition());
				return true;
			}
			case (NAVIGATE_TO_ALERT):{
				showOnMap(listViewPoiAlerts.getSelectedItemPosition());				
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, REMOVE_POIALERT, Menu.NONE, R.string.menuRemovePoiAlert);
		menu.add(0, NAVIGATE_TO_ALERT, Menu.NONE, R.string.menuShowOnMap);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo menuInfo;
		menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int index = menuInfo.position;

		switch (item.getItemId()) {
			case (REMOVE_POIALERT): {
				doRemovePOIAlertActions(index);
				return true;
			}
			case (NAVIGATE_TO_ALERT):{
				showOnMap(index);
				return true;
			}
		}
		return super.onContextItemSelected(item);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case (POIDIALOGVIEW): {
				return ActivityUtils.createViewDialog(this, R.layout.poidialogview, R.string.details, R.drawable.flag);
			}
		}
		return super.onCreateDialog(id);
	}
	
	@Override
	protected void onPrepareDialog(int id, final Dialog dialog) {
		switch (id) {
			case POIDIALOGVIEW:
				((TextView) dialog.findViewById(R.id.textViewPoiTitle)).setText(selectedPOIAlert.getTitle());
				((TextView) dialog.findViewById(R.id.textViewPoiRadius)).setText(selectedPOIAlert.getRadius().toString());
				((TextView) dialog.findViewById(R.id.textViewPoiActivated)).setText(selectedPOIAlert.getActivated() ? getText(R.string.yes) : getText(R.string.no)); 
				((TextView) dialog.findViewById(R.id.textViewPoiExpirationDate)).setText(selectedPOIAlert.getExpirationDateString());
				Button deleteButton = (Button)dialog.findViewById(R.id.buttonDeletePoi);
				deleteButton.setOnClickListener(new OnClickListener() {	
					public void onClick(View v) {
						showProgressDialog(getText(R.string.uiMessageRemovingPOIAlert));
						controller.removePOIAlert(selectedPOIAlert.getId());
						dialog.cancel();
					}
				});
				
				Button editButton = (Button)dialog.findViewById(R.id.buttonEditPoi);
				editButton.setOnClickListener(new OnClickListener() {	
					public void onClick(View v) {
						Intent intent = new Intent(POIAlertListActivity.this, EditPoiActivity.class);
						intent.putExtra(EditPoiActivity.BUNDLE_ALERT_ID, selectedPOIAlert.getId());
						startActivityForResult(intent, SUBACTIVITY_EDITPOI);
						dialog.cancel();
					}
				});
				
				break;
		}
	}
	
	/**
	 * Does preparative actions for calling the controller's removePOIAlert method
	 */
	private void doRemovePOIAlertActions(int index) {
		final POIAlert poiAlert = getSelectedPOIAlert(index);
		AlertDialog.Builder ad = ActivityUtils.createConfirmationDialog(this, getString(R.string.dialogRemovePOIAlertTitle), String.format(getString(R.string.dialogRemovePOIAlertMessage), poiAlert.getTitle()));
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
	 * This method is used to return the result of the item that should be zoomed and
	 * centered to the map activity
	 * @param selectedItemPosition
	 */
	private void showOnMap(int selectedItemPosition) {
		final POIAlert poiAlert = getSelectedPOIAlert(selectedItemPosition);
				
		Intent result = getIntent();
		result.putExtra(FriendMapActivity.CENTER_LAT, poiAlert.getPosition().getLatitude());
		result.putExtra(FriendMapActivity.CENTER_LNG, poiAlert.getPosition().getLongitude());
		
		setResult(RESULT_OK, result);
		finish();
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		controller.removeView(this);
	}
}
