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

import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.friendconnect.R;
import com.friendconnect.activities.FriendContactActivity;
import com.friendconnect.main.IFriendConnectApplication;
import com.friendconnect.main.IoC;
import com.friendconnect.model.POIAlert;
import com.friendconnect.model.User;

/**
 * Background Android service which continuously checks whether a friend
 * is located nearby of a POI. 
 */
public class POIAlertNotificationService extends Service {
	private IFriendConnectApplication application;
	private NotificationManager notificationManager;
	private Timer timer;
	private final int CHECK_INTERVAL = 5000; // TODO make configurable??
	private Handler mainHandler;
	private Hashtable<String, Integer> notificationsTable = new Hashtable<String, Integer>();
	private int notificationCounter = 1;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		this.application = IoC.getInstance(IFriendConnectApplication.class);
		
		String svcName = Context.NOTIFICATION_SERVICE; 
		notificationManager = (NotificationManager)getSystemService(svcName); 
		
		this.mainHandler = new Handler();
		this.timer = new Timer("POIAlertNotificationTimer");
		timer.scheduleAtFixedRate(performUpdate, 0, CHECK_INTERVAL);
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
	}
	
	private TimerTask performUpdate = new TimerTask() {	
		@Override
		public void run() {
			mainHandler.post(new Runnable(){
				public void run() {
					Log.i(POIAlertNotificationService.class.getCanonicalName(), "Checking POI alerts' locations with friends' locations");
					checkForPOIMatches();
				};
			});
		}
	};
	
	private void checkForPOIMatches() {
		List<POIAlert> poiAlerts = application.getApplicationModel().getCopyOfPoiAlerts();
		List<User> friends = application.getApplicationModel().getCopyOfFriends();
		for (POIAlert poiAlert : poiAlerts) {
			if (poiAlert.getPosition() != null) {
				android.location.Location poiLocation = poiAlert.getPosition().convertToAndroidLocation();
				for (User friend : friends) {
					if (friend.getOnline() && friend.getPosition() != null) {
						android.location.Location friendLocation = friend.getPosition().convertToAndroidLocation();
						if (poiLocation.distanceTo(friendLocation) <= poiAlert.getRadius()) {
							showNotification(friend, poiAlert);
						}
					}
				}
			}
		}
	}
	
	private void showNotification(User friend, POIAlert poiAlert) {
		Notification notification = new Notification(R.drawable.icon, getText(R.string.notificationNewPoiAlert), System.currentTimeMillis()); 
		Intent intent = new Intent(this, FriendContactActivity.class);
		intent.putExtra(FriendContactActivity.FRIEND_EMAIL, friend.getEmailAddress());
		intent.putExtra(FriendContactActivity.FRIEND_NAME, friend.getName());
		intent.putExtra(FriendContactActivity.FRIEND_PHONE, friend.getPhone());
		intent.putExtra(FriendContactActivity.POI_TITLE, poiAlert.getTitle());
		
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);

		String notificationContentText = String.format(getString(R.string.notificationText), friend.toString(), poiAlert.getTitle());
			
		notification.setLatestEventInfo(this, poiAlert.getTitle(), notificationContentText, pendingIntent);
		
		String notificationKey = friend.getId() + poiAlert.getId();
		int notificationId;
		if (notificationsTable.containsKey(notificationKey)) {
			notificationId = notificationsTable.get(notificationKey);
		} else {
			notificationId = notificationCounter;
			notificationsTable.put(notificationKey, notificationId);
			notificationCounter++;
		}
		 
		notificationManager.notify(notificationId, notification);
	}
	
	@Override
	public void onDestroy() {
		if(timer != null)
			timer.cancel();
		
		Set<String> keys = notificationsTable.keySet();
		for (String key : keys) {
			notificationManager.cancel(notificationsTable.get(key));
		}
		
		Log.i(POIAlertNotificationService.class.getCanonicalName(), "Service stopped");
	}
}
