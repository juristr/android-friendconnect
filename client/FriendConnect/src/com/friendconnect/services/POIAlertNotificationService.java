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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
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
import com.friendconnect.activities.POIAlertActivity;
import com.friendconnect.controller.POIAlertListController;
import com.friendconnect.main.IoC;
import com.friendconnect.model.POIAlert;
import com.friendconnect.model.User;

/**
 * Background Android service which continuously checks whether a friend
 * is located nearby of a POI. 
 */
public class POIAlertNotificationService extends Service {
	private POIAlertListController controller;
	private NotificationManager notificationManager;
	private Timer timer;
	private final int CHECK_INTERVAL = 5000; // TODO make configurable??
	private Handler mainHandler;
	private List<String> shownNotifications = new ArrayList<String>();
	private int notificationCounter = 0;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		this.controller = IoC.getInstance(POIAlertListController.class);
		
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
	
	/**
	 * Checks whether users' friends are in the range of POI alerts. It also 
	 * deactivates POI alerts automatically if the expiration date has already passed. 
	 */
	private void checkForPOIMatches() {
		List<POIAlert> poiAlerts = controller.getModel().getCopyOfPoiAlerts();
		List<User> friends = controller.getModel().getCopyOfFriends();
		Date now = new Date();
		for (POIAlert poiAlert : poiAlerts) {
			//check if an activated POI alert is expired; if yes deactivate it
			if (poiAlert.getActivated() && poiAlert.getExpirationDate().compareTo(now) < 0) {
				poiAlert.setActivated(false);
				controller.updatePOIAlert(poiAlert);
			} 
			//check if a friend is in the range of a POI
			else if (poiAlert.getActivated() && poiAlert.getPosition() != null) {
				android.location.Location poiLocation = poiAlert.getPosition().convertToAndroidLocation();
				for (User friend : friends) {
					if (friend.getOnline() && friend.getPosition() != null) {
						android.location.Location friendLocation = friend.getPosition().convertToAndroidLocation();
						if (poiLocation.distanceTo(friendLocation) <= poiAlert.getRadius()) {
							//show notification, since friend is in range
							showNotification(friend, poiAlert);
						} else {
							//eventually remove notification key, since friend is no more inside the range
							removeNotificationKey(friend, poiAlert);
						}
					} else {
						//eventually remove notification key, since friend is offline or its position is unknown
						removeNotificationKey(friend, poiAlert);
					}
				}
			}
		}
	}
	
	/**
	 * Removes the notification key associated to the pair of {@link User} and {@link POIAlert) objects from 
	 * the list of shown notifications, s.t. the notification can be shown again afterwards.  
	 * @param friend
	 * @param poiAlert
	 */
	private void removeNotificationKey(User friend, POIAlert poiAlert) {
		shownNotifications.remove(getNotificationKey(friend, poiAlert));
	}
	
	/**
	 * Returns the notification key for a pair of {@link User} and {@link POIAlert) objects. 
	 * @param friend
	 * @param poiAlert
	 * @return
	 */
	private String getNotificationKey(User friend, POIAlert poiAlert) {
		return friend.getId() + poiAlert.getId();
	}
	
	/**
	 * Returns the next notification id.
	 * @return
	 */
	private int getNextNotificationId() {
		if (notificationCounter == Integer.MAX_VALUE) {
			notificationCounter = 0;
		}
		notificationCounter++;
		return notificationCounter;
	}
	
	/**
	 * Shows a custom notification in the status bar.
	 * @param friend
	 * @param poiAlert
	 */
	private void showNotification(User friend, POIAlert poiAlert) {
		String notificationKey = getNotificationKey(friend, poiAlert);
		//Check whether the notification has already been shown
		if (!shownNotifications.contains(notificationKey)) {
			int notificationId = getNextNotificationId();
			shownNotifications.add(notificationKey);
			
			Notification notification = new Notification(R.drawable.icon, getText(R.string.notificationNewPoiAlert), System.currentTimeMillis()); 
			Intent intent = new Intent(this, POIAlertActivity.class);
			intent.putExtra(POIAlertActivity.FRIEND_EMAIL, friend.getEmailAddress());
			intent.putExtra(POIAlertActivity.FRIEND_NAME, friend.getName());
			intent.putExtra(POIAlertActivity.FRIEND_PHONE, friend.getPhone());
			intent.putExtra(POIAlertActivity.POI_TITLE, poiAlert.getTitle());
			intent.putExtra(POIAlertActivity.NOTIFICATION_ID, notificationId);
			
			notification.defaults = Notification.DEFAULT_ALL;
			
			PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
	
			String notificationContentText = String.format(getString(R.string.notificationText), friend.toString(), poiAlert.getTitle());
				
			notification.setLatestEventInfo(this, poiAlert.getTitle(), notificationContentText, pendingIntent);
			
			notificationManager.notify(notificationId, notification);
		}
	}
	
	@Override
	public void onDestroy() {
		if(timer != null)
			timer.cancel();
		
		Log.i(POIAlertNotificationService.class.getCanonicalName(), "Service stopped");
	}
}
