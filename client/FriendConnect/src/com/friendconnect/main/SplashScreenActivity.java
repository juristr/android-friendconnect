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

import com.friendconnect.R;
import com.friendconnect.activities.FriendListActivity;
import com.friendconnect.activities.LoginActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SplashScreenActivity extends Activity {
	private long splashTime = 3000;
	private boolean paused = false;
	private boolean splashActive = true;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		// Draw the splash screen
		setContentView(R.layout.splash);
		// Very simple timer thread
		Thread splashTimer = new Thread() {
			public void run() {
				try {
					// Wait loop
					long ms = 0;
					while (splashActive && ms < splashTime) {
						sleep(100);
						// Advance the timer only if we're running.
						if (!paused)
							ms += 100;
					}
					// Advance to the correct next screen
					FriendConnectApplication application = IoC.getInstance(FriendConnectApplication.class);
					Intent intent = null;
					if (application.getApplicationModel() == null) {
						intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
					} else {
						intent = new Intent(SplashScreenActivity.this, FriendListActivity.class);
					}
					startActivity(intent);
				} catch (Exception e) {
					Log.e("Splash", e.toString());
				} finally {
					finish();
				}
			}
		};
		splashTimer.start();
	}

	protected void onPause() {
		super.onPause();
		paused = true;
	}

	protected void onResume() {
		super.onResume();
		paused = false;
	}
}
