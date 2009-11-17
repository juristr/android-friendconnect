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
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class LocationController extends AbstractController<FriendConnectUser> {
	private ILocationService locationService;
	private Handler handler;
	private IXMLRPCService xmlRPCService;
	
	@SuppressWarnings("unchecked")
	@Override
	public BaseAdapter getAdapter(Context context) {
		return null;
	}

	/* Getters and setters */
	@Inject
	public void setLocationService(final ILocationService locationService) {
		this.locationService = locationService;
		handler = new Handler();
		handler.post(new Runnable() {		
			public void run() {
				locationService.getLocationManager().requestLocationUpdates(locationService.getProvider(), 60000, 10, new LocationListener() {		
					public void onStatusChanged(String provider, int status, Bundle extras) {
						
					}
					
					public void onProviderEnabled(String provider) {
						
					}
					
					public void onProviderDisabled(String provider) {
						
					}
					
					public void onLocationChanged(Location location) {
						if (model.getPosition() == null) {
							com.friendconnect.model.Location loc = new com.friendconnect.model.Location();
							model.setPosition(loc);
						}
						model.getPosition().setLatitude(location.getLatitude());
						model.getPosition().setLongitude(location.getLongitude());
						xmlRPCService.sendRequest(RPCRemoteMappings.UPDATE_USERLOCATION, null, new IAsyncCallback<Boolean>() {
							public void onFailure(Throwable throwable) {
								Log.e(LocationController.class.getCanonicalName(), throwable.getMessage());
							}

							public void onSuccess(Boolean result) {
								if (!result) {
									onFailure(new Exception("Sending of location was successful!"));
								}
							}
						}, Boolean.class);
					}
				});
			}
		});
	}

	public ILocationService getLocationService() {
		return locationService;
	}
	
	@Inject
	public void setApplication(IFriendConnectApplication application) {
		registerModel(application.getApplicationModel());
	}

	public IXMLRPCService getXmlRPCService() {
		return xmlRPCService;
	}
	
	@Inject
	public void setXmlRPCService(IXMLRPCService xmlRPCService) {
		this.xmlRPCService = xmlRPCService;
	}
}
