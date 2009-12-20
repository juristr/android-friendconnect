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

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.friendconnect.R;

public class POIAlertActivity extends AuthenticationActivity {
	public static final String FRIEND_NAME = "friendName";
	public static final String FRIEND_PHONE = "friendPhone";
	public static final String FRIEND_EMAIL = "friendEmail";
	public static final String POI_TITLE = "poiTitle";
	public static final String NOTIFICATION_ID = "notificationId";
	
	private String friendEmail;
	private String friendPhone;
	
	private RadioButton radioButtonEmail;
	private RadioButton radioButtonPhone;
	private RadioButton radioButtonSms;
	
	public void onAuthenticated() {
		setContentView(R.layout.poialert);
		
		Bundle retrievedData = this.getIntent().getExtras();
		String friendName = retrievedData.getString(FRIEND_NAME);
		friendPhone = retrievedData.getString(FRIEND_PHONE);
		friendEmail = retrievedData.getString(FRIEND_EMAIL);
		String poiTitle = retrievedData.getString(POI_TITLE);
		int notificationId = retrievedData.getInt(NOTIFICATION_ID);
		
		cancelNotification(notificationId);
		
		radioButtonPhone = (RadioButton)findViewById(R.id.radioButtonPhone);
		radioButtonEmail = (RadioButton)findViewById(R.id.radioButtonEmail);
		radioButtonSms = (RadioButton)findViewById(R.id.radioButtonSms);
			
		if (friendPhone == null || friendPhone.equals("")) {
			radioButtonPhone.setVisibility(RadioButton.INVISIBLE);
			radioButtonSms.setVisibility(RadioButton.INVISIBLE);
		}
			
		((TextView)findViewById(R.id.textViewContactFriend)).setText(String.format(getString(R.string.contactFriendText), friendName, poiTitle));
			
		Button okButton = (Button) findViewById(R.id.buttonOK);
		okButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent;
				if (radioButtonEmail.isChecked()) {
					intent = new Intent(Intent.ACTION_SENDTO);
					intent.setData(Uri.parse("mailto:" + friendEmail));
				} else if (radioButtonPhone.isChecked()) {
					intent = new Intent(Intent.ACTION_DIAL);
					intent.setData(Uri.parse("tel:" + friendPhone));
				} else {
					intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("tel:" + friendPhone));
					intent.setType("vnd.android-dir/mms-sms");   
				}
				startActivity(intent); 
			}
		});
	}

	/**
	 * Cancels the notification as it has been handled by this activity.
	 * @param notificationId
	 */
	private void cancelNotification(int notificationId) {
		String svcName = Context.NOTIFICATION_SERVICE; 
		NotificationManager notificationManager = (NotificationManager)getSystemService(svcName); 
		notificationManager.cancel(notificationId);
	}
}
