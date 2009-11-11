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

package com.friendconnect.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.friendconnect.R;
import com.friendconnect.activities.FriendListActivity;
import com.friendconnect.activities.LoginActivity;

public class SplashScreenActivity extends Activity {
	private long splashTime = 3000;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		FriendConnectApplication application = IoC.getInstance(FriendConnectApplication.class);
		
		if (application.getApplicationModel() == null) {
		
	        setContentView(R.layout.splash);
	        
	        /* New Handler to start the Menu-Activity
	         * and close this Splash-Screen after some seconds.*/
	        new Handler().postDelayed(new Runnable(){
	             public void run() {
	                  /* Create an Intent that will start the LoginActivity. */
	                  startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
	                  finish();
	             }
	        }, splashTime);
		} else {
			startActivity(new Intent(SplashScreenActivity.this, FriendListActivity.class));
            finish();
		}
	}
}
