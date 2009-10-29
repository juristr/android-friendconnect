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

import java.util.Observable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.friendconnect.activities.FriendListActivity;
import com.friendconnect.activities.R;
import com.friendconnect.controller.LoginController;
import com.friendconnect.model.LoginResult;
import com.friendconnect.view.IView;

public class FriendConnectActivity extends Activity implements IView {
	private Button signInButton;
	private LoginController controller;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginview);
		
		this.controller = IoC.getInstance(LoginController.class);
		this.controller.registerObserver(this);
		
		signInButton = (Button)this.findViewById(R.id.buttonSignIn);
		signInButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String username = ((EditText)findViewById(R.id.editTextEmail)).getText().toString();
				String password = ((EditText)findViewById(R.id.editTextPassword)).getText().toString();
				
				controller.login(username, password);
			}
		});
	}

	public void update(Observable observable, Object data) {
		LoginResult result = (LoginResult)observable;
		
		if(result.isLoginSucceeded()){
			startActivity(new Intent(FriendConnectActivity.this, FriendListActivity.class));
		}
	}
}