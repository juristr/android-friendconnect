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

import android.location.LocationManager;

import com.friendconnect.model.Location;
import com.google.inject.Singleton;

@Singleton
public class LocationService implements ILocationService {
	private LocationManager locationManager;
	private String provider; 
	
	public LocationService() {
		provider = LocationManager.GPS_PROVIDER;
	}
	
	public Location getLocation() {
		android.location.Location loc = locationManager.getLastKnownLocation(provider);
		Location location = new Location();
		location.setLatitude(loc.getLatitude());
		location.setLongitude(loc.getLongitude());
		return location;
	}
	
	public String getProvider() {
		return provider;
	}

	public void setSystemService(Object systemService) {
		locationManager = (LocationManager)systemService;
	}
	
	public LocationManager getLocationManager() {
		return locationManager;
	}
}
