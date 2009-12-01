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

import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import android.app.Dialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.friendconnect.R;
import com.friendconnect.controller.LocationController;
import com.friendconnect.main.IoC;
import com.friendconnect.model.FriendConnectUser;
import com.friendconnect.model.Location;
import com.friendconnect.model.User;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class FriendMapActivity extends MapActivity implements IView {
	private static final int CENTER_MAP = Menu.FIRST;
	private MapView mapView;
	private MapController mapController;
	private LocationController controller;
	private AndroidUserPositionOverlay androidUserPositionOverlay;
	private HashMap<String, FriendPositionOverlay> friendOverlays;
	private boolean doCenterMap = true;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friendmapview);
		this.mapView = (MapView) findViewById(R.id.map_view);
		this.mapController = mapView.getController();

		mapView.setBuiltInZoomControls(true);
		mapView.displayZoomControls(true);

		this.controller = IoC.getInstance(LocationController.class);
		this.controller.registerView(this);

		this.friendOverlays = new HashMap<String, FriendPositionOverlay>();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_3:
			mapController.zoomIn();
			break;
		case KeyEvent.KEYCODE_1:
			mapController.zoomOut();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int actionType = ev.getAction();
		switch (actionType) {
		case MotionEvent.ACTION_MOVE:
			doCenterMap = false;
			break;
		}

		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int actionType = event.getAction();
		switch (actionType) {
		case MotionEvent.ACTION_MOVE:
			doCenterMap = false;
			return true;
		}

		return super.onTouchEvent(event);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		return true;
	}

	/** Triggered the first time activity’s menu is displayed. */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// Create and add new menu items.
		MenuItem itemCenterMap = menu.add(0, CENTER_MAP, Menu.NONE,
				R.string.menuCenterMap);
		itemCenterMap.setIcon(R.drawable.menu_mylocation);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		// int index = this.listViewFriends.getSelectedItemPosition();
		switch (item.getItemId()) {
		case (CENTER_MAP): {
			doCenterMap = true;
			return true;
		}
		}
		return false;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// switch (id) {
		// case (DUMMY_LOCATIONSETTERDIALOG): {
		// LayoutInflater li = LayoutInflater.from(this);
		// View dummyLocationView = li.inflate(
		// R.layout.dummylocationsetdialog, null);
		// AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// AlertDialog dummyLocationDialog = builder.create();
		// dummyLocationDialog.setTitle("Dummy Location setter");
		// dummyLocationDialog.setView(dummyLocationView);
		// dummyLocationDialog.setCanceledOnTouchOutside(true);
		// dummyLocationDialog.setIcon(R.drawable.icon);
		// return dummyLocationDialog;
		// }
		// }
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, final Dialog dialog) {
		// switch (id) {
		// case (DUMMY_LOCATIONSETTERDIALOG): {
		// ((Button) dialog.findViewById(R.id.buttonSubmitLoc))
		// .setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// EditText editTextLat = (EditText) dialog
		// .findViewById(R.id.editTextLatitude);
		// EditText editTextLng = (EditText) dialog
		// .findViewById(R.id.editTextLongitude);
		//
		// double lat = Double.parseDouble(editTextLat
		// .getText().toString());
		// double lng = Double.parseDouble(editTextLng
		// .getText().toString());
		//
		// final Location loc = new Location();
		// loc.setLatitude(lat);
		// loc.setLongitude(lng);
		//
		// Handler handler = new Handler();
		// handler.post(new Runnable() {
		// public void run() {
		// controller.updateDummyLocation(loc);
		// }
		// });
		//
		// dialog.dismiss();
		// }
		// });
		// }
		// }
	}
	
	private void showAndroidUserPosition(Location position){
		if(androidUserPositionOverlay == null){
			androidUserPositionOverlay = new AndroidUserPositionOverlay();
			addOverlay(androidUserPositionOverlay);
		}
		
		androidUserPositionOverlay.setPosition(position);
	}

	private void addOverlay(Overlay overlay) {
		List<Overlay> mapOverlays = mapView.getOverlays();
		mapOverlays.add(overlay);
	}

	private void navigateToPoint(double lat, double lng) {
		GeoPoint point = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
		mapController.animateTo(point);
	}

	private boolean containsFriend(String friendId, List<User> friends) {
		for (User user : friends) {
			if (user.getId().equals(friendId))
				return true;
		}

		return false;
	}

	public void update(Observable observable, Object data) {
		final FriendConnectUser user = (FriendConnectUser) observable;

		//fix user's own position
		showAndroidUserPosition(user.getPosition());
		
		// update friend's positions
		for (User friend : user.getFriends()) {
			FriendPositionOverlay friendOverlay = friendOverlays.get(friend
					.getId());
			if (friendOverlay != null) {
				friendOverlay.setPosition(friend.getPosition(), user
						.getPosition());
			} else {
				friendOverlay = new FriendPositionOverlay(friend);
				friendOverlay.setPosition(friend.getPosition(), user
						.getPosition());
				friendOverlays.put(friend.getId(), friendOverlay);
				addOverlay(friendOverlay);
			}
		}

		// remove overlays of past friends
		for (String friendId : friendOverlays.keySet()) {
			if (!containsFriend(friendId, user.getFriends())) {
				friendOverlays.remove(friendId);
				mapView.getOverlays().remove(friendOverlays.get(friendId));
			}
		}

		mapView.invalidate();
		if (doCenterMap) {
			// center the map on the user's position
			navigateToPoint(user.getPosition().getLatitude(), user
					.getPosition().getLongitude());
		}

	}
	
	public void stopProgess() {
		//do nothing
	}

	public void onFailure(int failureMessageId) {
		//do nothing
	}

	public void onSuccess(int successMessageId) {
		//do nothing
	}
}
