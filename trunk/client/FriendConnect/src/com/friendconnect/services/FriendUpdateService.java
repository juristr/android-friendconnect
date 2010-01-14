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
import android.os.Handler;
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
	private final int UPDATE_INTERVAL = 5000;
	private FriendListController controller;
	private Timer timer;
	private Handler mainHandler;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		this.controller = IoC.getInstance(FriendListController.class);
		this.mainHandler = new Handler();
		this.timer = new Timer("FriendUpdateTimer");
		timer.scheduleAtFixedRate(performUpdate, 0, UPDATE_INTERVAL);
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
	}
	
	private TimerTask performUpdate = new TimerTask() {	
		@Override
		public void run() {
			mainHandler.post(new Runnable(){
				public void run() {
					Log.i(FriendUpdateService.class.getCanonicalName(), "Fetching update from server");
					controller.updateFriendList();	
				};
			});
		}
	};
	
	@Override
	public void onDestroy() {
		if(timer != null)
			timer.cancel();
		
		Log.i(FriendUpdateService.class.getCanonicalName(), "Service stopped");
	}

}
