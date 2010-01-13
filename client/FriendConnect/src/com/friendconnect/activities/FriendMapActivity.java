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
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

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
import com.friendconnect.views.OnDoubleClickListener;
import com.friendconnect.views.OnLongTouchListener;
import com.friendconnect.views.OnOverlayClickListener;
import com.friendconnect.views.POIOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.Overlay;

public class FriendMapActivity extends AuthenticationMapActivity implements IView,
		OnLongTouchListener, OnDoubleClickListener, OnOverlayClickListener {

	public static String CENTER_LAT = "mapCenter_lat";
	public static String CENTER_LNG = "mapCenter_lng";

	private static final int CENTER_MAP = Menu.FIRST;
	private static final int LIST_POIALERTS = Menu.FIRST + 2;
	private static final int SUBACTIVITY_EDITPOI = 1;
	private static final int POIDIALOGVIEW = 2;
	private static final int SUBACTIVITY_POIALERTLIST = 3;

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
	}

	public void onAuthenticated() {
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
		this.poiController.registerView(this);

		this.friendOverlays = new HashMap<String, FriendPositionOverlay>();
		this.poiOverlays = new HashMap<String, POIOverlay>();
		update(locationController.getModel(), null);

		setCenterFromBundleData(getIntent());
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
		case SUBACTIVITY_POIALERTLIST:
			if (resultCode == Activity.RESULT_OK) {
				setCenterFromBundleData(data);
			}
			break;

		default:
			break;
		}
	}

	/**
	 * This sets the center of the map from data that is being passed through
	 * the Intent's bundle data
	 * 
	 * @param data
	 */
	private void setCenterFromBundleData(Intent data) {
		Bundle retrievedData = data.getExtras();
		if (retrievedData != null) {
			doCenterMap = false;
			double centerLat = retrievedData.getDouble(CENTER_LAT);
			double centerLng = retrievedData.getDouble(CENTER_LNG);

			if(centerLat != 0 && centerLng != 0){
				navigateToPoint(centerLat, centerLng);
			}else{
				ActivityUtils.showToast(this, R.string.uiMessageInvalidLocation, 2000);
			}
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

		MenuItem listPoiAlerts = menu.add(1, LIST_POIALERTS, Menu.NONE, R.string.menuListPoiAlerts);
		listPoiAlerts.setIcon(R.drawable.menu_myplaces);

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

		case (LIST_POIALERTS): {
			startActivityForResult(new Intent(this, POIAlertListActivity.class),
					SUBACTIVITY_POIALERTLIST);
			return true;
		}
		}
		return false;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case (POIDIALOGVIEW): {
			return ActivityUtils.createViewDialog(this, R.layout.poidialogview, R.string.details,
					R.drawable.flag);
		}
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, final Dialog dialog) {
		switch (id) {
		case POIDIALOGVIEW:
			((TextView) dialog.findViewById(R.id.textViewPoiTitle)).setText(clickedPOIAlert
					.getTitle());
			((TextView) dialog.findViewById(R.id.textViewPoiRadius)).setText(clickedPOIAlert
					.getRadius().toString());
			((TextView) dialog.findViewById(R.id.textViewPoiActivated)).setText(clickedPOIAlert
					.getActivated() ? getText(R.string.yes) : getText(R.string.no));
			((TextView) dialog.findViewById(R.id.textViewPoiExpirationDate))
					.setText(clickedPOIAlert.getExpirationDateString());
			Button deleteButton = (Button) dialog.findViewById(R.id.buttonDeletePoi);
			deleteButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					showProgressDialog(getText(R.string.uiMessageRemovingPOIAlert));
					poiController.removePOIAlert(clickedPOIAlert.getId());
					dialog.cancel();
				}
			});

			Button editButton = (Button) dialog.findViewById(R.id.buttonEditPoi);
			editButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(FriendMapActivity.this, EditPoiActivity.class);
					intent.putExtra(EditPoiActivity.BUNDLE_ALERT_ID, clickedPOIAlert.getId());
					startActivityForResult(intent, SUBACTIVITY_EDITPOI);
					dialog.cancel();
				}
			});

			break;

		default:
			break;
		}
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
		mapController.setZoom(16);
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
		try {
			final FriendConnectUser user = (FriendConnectUser) observable;

			updateFriendLocations(user);
			updatePOIFlags(user);

			mapView.invalidate();
			if (doCenterMap && user.getPosition() != null) {
				// center the map on the user's position

				if (isValidPosition(user.getPosition())) {
					navigateToPoint(user.getPosition().getLatitude(), user.getPosition()
							.getLongitude());
				}
			}
		} catch (Exception e) {
			Log.i(FriendMapActivity.class.getCanonicalName(), "Error during update: "
					+ e.getMessage());
		}
	}

	private void updateFriendLocations(final FriendConnectUser user) {
		// fix user's own position
		showAndroidUserPosition(user.getPosition());

		// update friend's positions
		List<User> friends = user.getCopyOfFriends();
		for (User friend : friends) {
			if (friend.getOnline() && friend.getPosition() != null && isValidPosition(friend.getPosition())) {
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
			if (!containsFriend(friendId, friends)) {
				mapView.getOverlays().remove(friendOverlays.get(friendId));
				friendOverlays.remove(friendId);
			}
		}
	}

	private boolean isValidPosition(Location position) {
		return (position.getLatitude() != 0 && position.getLongitude() != 0);
	}

	private void updatePOIFlags(final FriendConnectUser user) {
		if (user.getPoiAlerts() != null) {
			List<POIAlert> poiAlerts = user.getCopyOfPoiAlerts();
			for (POIAlert poiAlert : poiAlerts) {
				POIOverlay poiOverlay = poiOverlays.get(poiAlert.getId());
				if (poiOverlay == null) {
					poiOverlay = new POIOverlay(poiAlert, this);
					poiOverlays.put(poiAlert.getId(), poiOverlay);
					poiOverlay.setOnOverlayClickListener(this);
					addOverlay(poiOverlay);
				}
			}

			for (String poiAlertId : poiOverlays.keySet()) {
				if (!containsPoiAlert(poiAlertId, poiAlerts)) {
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		locationController.removeView(this);
		poiController.removeView(this);
	}
}
