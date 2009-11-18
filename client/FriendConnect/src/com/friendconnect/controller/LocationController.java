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

package com.friendconnect.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.BaseAdapter;

import com.friendconnect.main.IFriendConnectApplication;
import com.friendconnect.model.FriendConnectUser;
import com.friendconnect.model.RPCRemoteMappings;
import com.friendconnect.services.ILocationService;
import com.friendconnect.services.IXMLRPCService;
import com.friendconnect.xmlrpc.IAsyncCallback;
import com.friendconnect.xmlrpc.ObjectSerializer;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class LocationController extends AbstractController<FriendConnectUser> {
	private ILocationService locationService;
	private Handler handler;
	private IXMLRPCService xmlRPCService;
	private ObjectSerializer serializer;

	@SuppressWarnings("unchecked")
	@Override
	public BaseAdapter getAdapter(Context context) {
		return null;
	}

	public void updateDummyLocation(com.friendconnect.model.Location loc) {
		if (loc != null)
			sendLocationUpdateToServer(loc);
	}

	public void startLocationTracking() {
		handler = new Handler();
		handler.post(new Runnable() {
			public void run() {
				locationService.getLocationManager().requestLocationUpdates(
						locationService.getProvider(), 60000, 10,
						new LocationListener() {
							public void onStatusChanged(String provider,
									int status, Bundle extras) {

							}

							public void onProviderEnabled(String provider) {

							}

							public void onProviderDisabled(String provider) {

							}

							public void onLocationChanged(Location location) {
								if (location != null) {
									com.friendconnect.model.Location userLocation = new com.friendconnect.model.Location();
									userLocation.setLatitude(location
											.getLatitude());
									userLocation.setLongitude(location
											.getLongitude());
									sendLocationUpdateToServer(userLocation);
								}
							}
						});
			}
		});
	}

	private void sendLocationUpdateToServer(
			com.friendconnect.model.Location location) {

		if (location != null) {
			com.friendconnect.model.Location userLocation = new com.friendconnect.model.Location();
			userLocation.setLatitude(location.getLatitude());
			userLocation.setLongitude(location.getLongitude());

			model.setPosition(userLocation);

			Map<String, Object> locationData;
			try {
				locationData = serializer.serialize(userLocation);

				xmlRPCService.sendRequest(
						RPCRemoteMappings.UPDATE_USERLOCATION,
						new Object[] { locationData },
						new IAsyncCallback<Boolean>() {
							public void onFailure(Throwable throwable) {
								Log.e(LocationController.class
										.getCanonicalName(), throwable
										.getMessage());
							}

							public void onSuccess(Boolean result) {
								if (!result) {
									onFailure(new Exception(
											"Sending of location was successful!"));
								}
							}
						}, Boolean.class);
			} catch (Exception e) {
				Log.e(LocationController.class.getCanonicalName(),
						"Serialization error:" + e.getMessage());
			}
		}
	}

	/* Getters */
	@Inject
	public void setApplication(IFriendConnectApplication application) {
		registerModel(application.getApplicationModel());
	}

	@Inject
	public void setXmlRPCService(IXMLRPCService xmlRPCService) {
		this.xmlRPCService = xmlRPCService;
	}

	@Inject
	public void setLocationService(final ILocationService locationService) {
		this.locationService = locationService;
	}

	@Inject
	public void setSerializer(ObjectSerializer serializer) {
		this.serializer = serializer;
	}

}
