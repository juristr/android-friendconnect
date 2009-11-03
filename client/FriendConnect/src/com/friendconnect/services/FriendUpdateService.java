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

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.friendconnect.controller.FriendListController;
import com.friendconnect.main.IoC;

/**
 * Background Android service which continuously queries for
 * updated friend's data from the server.
 *
 */
public class FriendUpdateService extends Service {
	private FriendListController controller;
	private Timer timer = new Timer();
	private final int UPDATE_INTERVAL = 20000; // TODO make configurable??

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		this.controller = IoC.getInstance(FriendListController.class);
		startService();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
		shutdownService();
	}

	private void startService() {
		timer.scheduleAtFixedRate(
				new TimerTask() {
					@Override
					public void run() {
						Log.i(FriendUpdateService.class.getCanonicalName(), "Fetching updated friend list");
						controller.updateFriendList();	
					}
				}, 
				0, 
				UPDATE_INTERVAL
		);
		
		Log.i(getClass().getSimpleName(), "FriendUpdateService launched");
	}
	
	private void shutdownService(){
		if(timer != null)
			timer.cancel();
		
		Log.i(getClass().getSimpleName(), "FriendUpdateService stopped");
	}

	public void setController(FriendListController controller) {
		this.controller = controller;
	}

}
