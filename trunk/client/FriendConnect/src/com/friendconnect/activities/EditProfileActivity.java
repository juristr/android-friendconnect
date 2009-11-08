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

import java.util.Observable;

import com.friendconnect.R;
import com.friendconnect.controller.EditProfileController;
import com.friendconnect.main.IoC;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EditProfileActivity extends Activity implements IView {
	private EditProfileController controller;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friendlist);

		progressDialog = new ProgressDialog(this);

		controller = IoC.getInstance(EditProfileController.class);
		controller.registerView(this);

		Button signInButton = (Button) this.findViewById(R.id.buttonSignIn);
		signInButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String username = ((EditText) findViewById(R.id.editTextEmail))
						.getText().toString();
				String password = ((EditText) findViewById(R.id.editTextPassword))
						.getText().toString();

				progressDialog.setMessage(getText(R.string.uiMessageLogin));
				progressDialog.show();

				// controller.login(username, password);
			}
		});
	}

	public void update(Observable observable, Object data) {
		//close the activity??
	}

	public void onProgressChanged(String message) {
		progressDialog.setMessage(message);
	}

	public void stopProgess() {
		progressDialog.cancel();
	}
}
