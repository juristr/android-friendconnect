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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.friendconnect.R;
import com.friendconnect.controller.LocationController;
import com.friendconnect.controller.POIAlertListController;
import com.friendconnect.main.IoC;
import com.friendconnect.model.FriendConnectUser;
import com.friendconnect.model.Location;
import com.friendconnect.model.POIAlert;
import com.friendconnect.model.User;
import com.friendconnect.utils.ActivityUtils;
import com.friendconnect.views.AndroidUserPositionOverlay;
import com.friendconnect.views.FriendMapView;
import com.friendconnect.views.FriendPositionOverlay;
import com.friendconnect.views.OnOverlayClickListener;
import com.friendconnect.views.OnDoubleClickListener;
import com.friendconnect.views.OnLongTouchListener;
import com.friendconnect.views.POIOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.Overlay;

public class FriendMapActivity extends MapActivity implements IView, OnLongTouchListener,
		OnDoubleClickListener, OnOverlayClickListener {
	private static final int CENTER_MAP = Menu.FIRST;
	private static final int ADD_POI = Menu.FIRST + 1;
	private static final int SUBACTIVITY_EDITPOI = 1;
	private static final int POIDIALOGVIEW = 1;

	private ProgressDialog progressDialog;
	private FriendMapView mapView;
	private MapController mapController;
	private LocationController locationController;
	private POIAlertListController poiController;
	private AndroidUserPositionOverlay androidUserPositionOverlay;
	private HashMap<String, FriendPositionOverlay> friendOverlays;
	private HashMap<String, POIOverlay> poiOverlays;
	private boolean doCenterMap = true;
	private POIAlert clickedPOIAlert = null; // the clicked alert (for showing
												// appropriate dialog)

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friendmapview);
		this.mapView = (FriendMapView) findViewById(R.id.map_view);
		this.mapController = mapView.getController();

		mapView.setBuiltInZoomControls(true);
		mapView.displayZoomControls(true);
		mapView.setOnLongTouchListener(this);
		mapView.setOnDoubleClickListener(this);

		this.locationController = IoC.getInstance(LocationController.class);
		this.locationController.registerView(this);
		this.poiController = IoC.getInstance(POIAlertListController.class);
		 //do not register to not get too many updates! LocationController already registers on FriendConnectUser
		this.poiController.registerView(this);

		this.progressDialog = new ProgressDialog(this);
		
		this.friendOverlays = new HashMap<String, FriendPositionOverlay>();
		this.poiOverlays = new HashMap<String, POIOverlay>();
		update(locationController.getModel(), null);
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

	public void onLongTouch(MotionEvent ev) {
		GeoPoint point = mapView.getProjection().fromPixels((int) ev.getX(), (int) ev.getY());

		// start intent for editing
		Intent intent = new Intent(this, EditPoiActivity.class);
		intent.putExtra(EditPoiActivity.BUNDLE_GEO_LAT, point.getLatitudeE6());
		intent.putExtra(EditPoiActivity.BUNDLE_GEO_LNG, point.getLongitudeE6());
		startActivityForResult(intent, SUBACTIVITY_EDITPOI);
	}

	public void onDoubleClick(MotionEvent ev) {
		doCenterMap = false;
		mapController.zoomInFixing((int) ev.getX(), (int) ev.getY());
	}

	public void onClick(GeoPoint point, Object data) {
		// selected poi alert
		if (data != null) {
			clickedPOIAlert = (POIAlert) data;
			showDialog(POIDIALOGVIEW);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case SUBACTIVITY_EDITPOI:
			if (resultCode == Activity.RESULT_OK) {
				// handle the thing, otherwise remove
				ActivityUtils.showToast(this, "Ok!", 1500);
			} else {
				ActivityUtils.showToast(this, "Cancelled!", 1500);
			}
			break;

		default:
			break;
		}
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
		MenuItem itemCenterMap = menu.add(0, CENTER_MAP, Menu.NONE, R.string.menuCenterMap);
		itemCenterMap.setIcon(R.drawable.menu_mylocation);

		MenuItem itemAddPoi = menu.add(1, ADD_POI, Menu.NONE, R.string.menuAddPoiAlert);
		itemAddPoi.setIcon(R.drawable.menu_myplaces);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case (CENTER_MAP): {
			doCenterMap = true;
			return true;
		}
		case (ADD_POI): {
			startActivity(new Intent(this, EditPoiActivity.class));
			return true;
		}
		}
		return false;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case (POIDIALOGVIEW): {
			LayoutInflater li = LayoutInflater.from(this);
			View poiDialogView = li.inflate(R.layout.poidialogview, null);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			AlertDialog dummyLocationDialog = builder.create();
			dummyLocationDialog.setTitle("POI Details");
			dummyLocationDialog.setView(poiDialogView);
			dummyLocationDialog.setCanceledOnTouchOutside(true);
			dummyLocationDialog.setIcon(R.drawable.flag);
			return dummyLocationDialog;
		}
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, final Dialog dialog) {
		switch (id) {
		case POIDIALOGVIEW:
			((TextView) dialog.findViewById(R.id.textViewPoiTitle)).setText(clickedPOIAlert.getTitle());
			Button deleteButton = (Button)dialog.findViewById(R.id.buttonDeletePoi);
			deleteButton.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					progressDialog.setMessage(getText(R.string.uiMessageRemovingPOIAlert));
					progressDialog.show();
					poiController.removePOIAlert(clickedPOIAlert.getId());
					dialog.cancel();
				}
			});
			break;

		default:
			break;
		}

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

	private void showAndroidUserPosition(Location position) {
		if (androidUserPositionOverlay == null) {
			androidUserPositionOverlay = new AndroidUserPositionOverlay();
			addOverlay(androidUserPositionOverlay);
		}

		androidUserPositionOverlay.setPosition(position);
	}

	private void addOverlay(Overlay overlay) {
		List<Overlay> mapOverlays = mapView.getOverlays();
		mapOverlays.add(overlay);
		mapView.invalidate();
	}

	private void navigateToPoint(double lat, double lng) {
		GeoPoint point = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
		mapController.animateTo(point);
	}

	private boolean containsPoiAlert(String poiAlertId, List<POIAlert> poiAlerts) {
		for (POIAlert alert : poiAlerts) {
			if (alert.getId().equals(poiAlertId)) {
				return true;
			}
		}

		return false;
	}

	private boolean containsFriend(String friendId, List<User> friends) {
		for (User user : friends) {
			if (user.getId().equals(friendId))
				return true;
		}

		return false;
	}

	public synchronized void update(Observable observable, Object data) {
		final FriendConnectUser user = (FriendConnectUser) observable;

		updateFriendLocations(user);
		updatePOIFlags(user);

		mapView.invalidate();
		if (doCenterMap) {
			// center the map on the user's position
			navigateToPoint(user.getPosition().getLatitude(), user.getPosition().getLongitude());
		}

	}

	private void updateFriendLocations(final FriendConnectUser user) {
		// fix user's own position
		showAndroidUserPosition(user.getPosition());

		// update friend's positions
		for (User friend : user.getFriends()) {
			if (friend.getOnline()) {
				FriendPositionOverlay friendOverlay = friendOverlays.get(friend.getId());
				if (friendOverlay != null) {
					friendOverlay.setPosition(friend.getPosition().convertToAndroidGeoPoint(), user
							.getPosition().convertToAndroidGeoPoint());
				} else {
					friendOverlay = new FriendPositionOverlay(friend);
					friendOverlay.setPosition(friend.getPosition().convertToAndroidGeoPoint(), user
							.getPosition().convertToAndroidGeoPoint());
					friendOverlays.put(friend.getId(), friendOverlay);
					addOverlay(friendOverlay);
				}
			}
		}

		// remove overlays of past friends
		for (String friendId : friendOverlays.keySet()) {
			if (!containsFriend(friendId, user.getFriends())) {
				mapView.getOverlays().remove(friendOverlays.get(friendId));
				friendOverlays.remove(friendId);
			}
		}
	}

	private void updatePOIFlags(final FriendConnectUser user) {
		if (user.getPoiAlerts() != null) {
			for (POIAlert poiAlert : user.getPoiAlerts()) {
				POIOverlay poiOverlay = poiOverlays.get(poiAlert.getId());
				if (poiOverlay == null) {
					poiOverlay = new POIOverlay(poiAlert, this);
					poiOverlays.put(poiAlert.getId(), poiOverlay);
					poiOverlay.setOnOverlayClickListener(this);
					addOverlay(poiOverlay);
				}
			}

			for (String poiAlertId : poiOverlays.keySet()) {
				if (!containsPoiAlert(poiAlertId, user.getPoiAlerts())) {
					mapView.getOverlays().remove(poiOverlays.get(poiAlertId));
					poiOverlays.remove(poiAlertId);
				}
			}
		}

		// remove overlays of past friends
		// for (String friendId : friendOverlays.keySet()) {
		// if (!containsFriend(friendId, user.getFriends())) {
		// friendOverlays.remove(friendId);
		// mapView.getOverlays().remove(friendOverlays.get(friendId));
		// }
		// }
	}

	public void stopProgress() {
		progressDialog.cancel();
	}

	public void showMessage(int messageId) {
		ActivityUtils.showToast(this, messageId, Toast.LENGTH_SHORT);
	}
}
