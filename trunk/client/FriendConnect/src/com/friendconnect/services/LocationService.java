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

package com.friendconnect.services;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.friendconnect.R;
import com.friendconnect.controller.LocationController;
import com.friendconnect.main.IoC;
import com.friendconnect.utils.ActivityUtils;

public class LocationService extends Service implements LocationListener {
	private final int UPDATE_INTERVAL = 5000;
	private LocationController locationController;
	private LocationManager locationManager;
	private NotificationManager notificationManager;
	private Timer timer;
	private int notificationId = 339484712;
	private Handler mainHandler;
	private String provider;
	private boolean isUserNotified = false;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		this.locationController = IoC.getInstance(LocationController.class);
		this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		instantiateLocationProvider();

		this.notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		this.timer = new Timer("LocationProviderTimer");
		
		if (provider != null) {
			registerForLocationUpdates();
		} else {
			startLocationProviderChecker();
		}
	}

	private void startLocationProviderChecker() {
		timer.scheduleAtFixedRate(locationCheckerTimer, 0, UPDATE_INTERVAL);
	}

	private void instantiateLocationProvider() {
		if (provider == null) {
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			criteria.setAltitudeRequired(false);
			criteria.setBearingRequired(false);
			criteria.setCostAllowed(true);
			provider = locationManager.getBestProvider(criteria, true);
		}
	}

	@Override
	public void onDestroy() {
		if (timer != null)
			timer.cancel();

		if (locationManager != null) {
			locationManager.removeUpdates(this);
		}
	}

	private TimerTask locationCheckerTimer = new TimerTask() {
		@Override
		public void run() {
			Log.i(LocationService.class.getCanonicalName(), "Location post started");
			instantiateLocationProvider();

			if (provider != null) {
				Log.i(LocationService.class.getCanonicalName(), "provider found");
				// remove previous notification (if present)
				notificationManager.cancel(notificationId);
				isUserNotified = false;

				registerForLocationUpdates();
			} else {
				Log.i(LocationService.class.getCanonicalName(), "notifying user");
				if (!isUserNotified) {
					showNotification();
					isUserNotified = true;
				}
			}
		}
	};

	public void onLocationChanged(android.location.Location location) {
		if (location != null) {
			com.friendconnect.model.Location userLocation = new com.friendconnect.model.Location();
			userLocation.setLatitude(location.getLatitude());
			userLocation.setLongitude(location.getLongitude());
			locationController.sendLocationUpdateToServer(userLocation);
		}
	}

	public void onProviderDisabled(String provider) {
		Log.i(LocationService.class.getCanonicalName(), provider + " disabled");
		ActivityUtils.showToast(getBaseContext(),
				R.string.uiMessageLocationProviderDisabled, 10000);
	}

	public void onProviderEnabled(String provider) {
		Log.i(LocationService.class.getCanonicalName(), provider + " enabled");
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.i(LocationService.class.getCanonicalName(), "provider status change");
		if (status == LocationProvider.OUT_OF_SERVICE) {
			ActivityUtils.showToast(getBaseContext(),
					R.string.uiMessageLocationProviderOutOfService, 10000);
		} else if (status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
			ActivityUtils.showToast(getBaseContext(),
					R.string.uiMessageLocationProviderTemporarilyUnavailable, 10000);
		}
	}

	private void registerForLocationUpdates() {
		if (timer != null)
			timer.cancel();
		locationManager.requestLocationUpdates(provider, 10000, 10, this);
	};

	private void showNotification() {
		Notification notification = new Notification(R.drawable.warning, "Location problem", System
				.currentTimeMillis());
		notification.defaults = Notification.DEFAULT_ALL;

		ComponentName c = new ComponentName("com.android.settings",
				"com.android.settings.SecuritySettings");
		Intent locationSettingsIntent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
		locationSettingsIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		locationSettingsIntent.setComponent(c);
		locationSettingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId,
				locationSettingsIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

		notification.setLatestEventInfo(this, getText(R.string.notificationLocationTitle),
				getText(R.string.notificationLocationText), pendingIntent);

		notificationManager.notify(notificationId, notification);
	}
}
