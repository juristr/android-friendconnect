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

/**
 * Interface for the LocationService
 */
public interface ILocationService {
	
	/**
	 * Returns the current position (latitude, longitude) of the user
	 * @return location
	 */
	public Location getLocation();
	
	/**
	 * Returns the provider
	 * @return provider
	 */
	public String getProvider();
	
	/**
	 * Sets the system service
	 * @param systemService
	 */
	public void setSystemService(Object systemService);

	
	/**
	 * Gets the location manager
	 * @return locationManager
	 */
	public LocationManager getLocationManager();
}
