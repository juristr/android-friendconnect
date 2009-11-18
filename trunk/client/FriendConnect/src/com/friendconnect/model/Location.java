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

package com.friendconnect.model;


/**
 * Representing a location on the earth (lat/lng)
 * 
 */
public class Location {
	private double latitude;
	private double longitude;

	public Location() {
		// TODO check whether to wrap directly a
		// com.google.android.maps.GeoPoint here
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	/**
	 * Converts into a Android Location
	 * @return
	 */
	public android.location.Location convertToAndroidLocation(){
		android.location.Location androidLoc = new android.location.Location("converted");
		androidLoc.setLatitude(this.latitude);
		androidLoc.setLongitude(this.longitude);
		return androidLoc;
	}

}
