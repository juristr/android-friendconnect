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

import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;

import com.friendconnect.R;
import com.friendconnect.controller.LocationController;
import com.friendconnect.model.Location;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class LocationService implements ILocationService, LocationListener {
	private LocationController locationController;
	private LocationManager locationManager;
	private String provider;
	private boolean running = false;
	
	public LocationService() {
		
	}
	
	public Location getLocation() {
		android.location.Location loc = locationManager.getLastKnownLocation(provider);
		Location location = new Location();
		location.setLatitude(loc.getLatitude());
		location.setLongitude(loc.getLongitude());
		return location;
	}
	
	public void startLocationTracking() {
		if (!running) {
			Handler handler = new Handler();
			handler.post(new Runnable() {
				public void run() {
					locationManager.requestLocationUpdates(provider, 60000, 10, LocationService.this);
				}
			});
			running = true;
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
	
	public void setSystemService(Object systemService) {
		locationManager = (LocationManager)systemService;
		Criteria criteria = new Criteria(); 
		criteria.setAccuracy(Criteria.ACCURACY_FINE); 
		criteria.setAltitudeRequired(false); 
		criteria.setBearingRequired(false); 
		criteria.setCostAllowed(true); 
		criteria.setPowerRequirement(Criteria.POWER_LOW); 
		provider = locationManager.getBestProvider(criteria, true);
	}
	
	@Inject
	public void setLocationController(LocationController locationController) {
		this.locationController = locationController;
	}
}
