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

import com.friendconnect.R;
import com.friendconnect.services.FriendUpdateService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class PendingInvitesListActivity extends Activity {

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pendinginviteslist);
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		startService(new Intent(PendingInvitesListActivity.this, FriendUpdateService.class));	
	}
	
	@Override
	protected void onStop() {
		super.onStop();
        stopService(new Intent(PendingInvitesListActivity.this, FriendUpdateService.class));
	}
}
