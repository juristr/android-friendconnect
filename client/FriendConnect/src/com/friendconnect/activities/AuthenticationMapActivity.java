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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.friendconnect.main.FriendConnectApplication;
import com.friendconnect.main.IoC;
import com.friendconnect.services.FriendUpdateService;
import com.friendconnect.services.ILocationService;
import com.friendconnect.services.LocationService;
import com.friendconnect.services.POIAlertNotificationService;
import com.friendconnect.utils.ActivityUtils;
import com.google.android.maps.MapActivity;

/**
 * Generic map activity that defines the behavior common to all FriendConnect 
 * map activities that require authentication.
 */
public abstract class AuthenticationMapActivity extends MapActivity {
	private static final int SUBACTIVITY_LOGIN = 1337;
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		progressDialog = new ProgressDialog(this);
		
		FriendConnectApplication application = IoC.getInstance(FriendConnectApplication.class);
		if (application.getApplicationModel() == null) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivityForResult(intent, SUBACTIVITY_LOGIN);
		} else {
			onAuthenticated();
		}
	}
	
	/**
	 * Template method that is used to let subclasses implement (through method overriding)
	 * the activity's behavior when the user has been successfully authenticated.
	 */
	public abstract void onAuthenticated();
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//Here we identify the subActivity we started
        if (requestCode == SUBACTIVITY_LOGIN) {
        	if (resultCode == Activity.RESULT_OK) {
        		//Start all FriendConnect services
        		startService(new Intent(this, FriendUpdateService.class));
        		startService(new Intent(this, POIAlertNotificationService.class));
        		
        		ILocationService locationService = IoC.getInstance(LocationService.class);
        		locationService.setSystemService(getSystemService(Context.LOCATION_SERVICE));
        		locationService.startLocationTracking();

        		onAuthenticated();
        	}
        }
	}
	
	public void stopProgress() {
		progressDialog.cancel();
	}
	
	public void showProgressDialog(CharSequence message) {
		progressDialog.setMessage(message);
		progressDialog.show();
	}

	public void showMessage(int messageId) {
		ActivityUtils.showToast(this, messageId, Toast.LENGTH_SHORT);
	}
}
