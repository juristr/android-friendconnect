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

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import com.friendconnect.R;
import com.friendconnect.controller.LocationController;
import com.friendconnect.main.IoC;

public class LocationService extends Service implements LocationListener {
	private LocationController locationController;
	private LocationManager locationManager;
	private String provider;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		this.locationController = IoC.getInstance(LocationController.class);
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria(); 
		criteria.setAccuracy(Criteria.ACCURACY_FINE); 
		criteria.setAltitudeRequired(false); 
		criteria.setBearingRequired(false); 
		criteria.setCostAllowed(true); 
		provider = locationManager.getBestProvider(criteria, true);
		Handler handler = new Handler();
		handler.post(new Runnable() {
			public void run() {
				locationManager.requestLocationUpdates(provider, 10000, 10, LocationService.this);
			}
		});
	}
	
	@Override
	public void onDestroy() {
		if (locationManager != null) {
			locationManager.removeUpdates(this);
		}
	}

	public void onLocationChanged(android.location.Location location) {
		if (location != null) {
			com.friendconnect.model.Location userLocation = new com.friendconnect.model.Location();
			userLocation.setLatitude(location.getLatitude());
			userLocation.setLongitude(location.getLongitude());
			locationController.sendLocationUpdateToServer(userLocation);
		}
	}

	public void onProviderDisabled(String provider) {
		locationController.notifyShowMessage(R.string.uiMessageLocationProviderDisabled);
	}

	public void onProviderEnabled(String provider) {
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		if(status == LocationProvider.OUT_OF_SERVICE){
			locationController.notifyShowMessage(R.string.uiMessageLocationProviderOutOfService);
		}else if(status == LocationProvider.TEMPORARILY_UNAVAILABLE){
			locationController.notifyShowMessage(R.string.uiMessageLocationProviderTemporarilyUnavailable);
		}
	}
}
