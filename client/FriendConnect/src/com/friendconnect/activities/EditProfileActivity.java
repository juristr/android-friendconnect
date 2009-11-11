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
import com.friendconnect.model.FriendConnectUser;
import com.friendconnect.services.FriendUpdateService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
		setContentView(R.layout.editprofileview);

		progressDialog = new ProgressDialog(this);

		controller = IoC.getInstance(EditProfileController.class);
		controller.registerView(this);
		
		init();

		Button saveButton = (Button) this.findViewById(R.id.buttonSaveProfile);
		saveButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String name = ((EditText) findViewById(R.id.editTextName)).getText().toString();
				String phone = ((EditText) findViewById(R.id.editTextPhone)).getText().toString();
				String website = ((EditText) findViewById(R.id.editTextWebsite)).getText().toString();
				String statusMessage = ((EditText) findViewById(R.id.editTextStatusMsg)).getText().toString();

				progressDialog.setMessage(getText(R.string.uiMessageSavingProfile));
				progressDialog.show();

				controller.saveProfile(name, phone, website, statusMessage);
			}
		});
	}
	
	private void init() {
		FriendConnectUser user = controller.getModel();
		((EditText) findViewById(R.id.editTextEmail)).setText(user.getEmailAddress());
		((EditText) findViewById(R.id.editTextName)).setText(user.getName());
		((EditText) findViewById(R.id.editTextPhone)).setText(user.getPhone());
		((EditText) findViewById(R.id.editTextWebsite)).setText(user.getWebsite());
		((EditText) findViewById(R.id.editTextStatusMsg)).setText(user.getStatusMessage());
	}

	public void update(Observable observable, Object data) {
		//do nothing
	}

	public void onProgressChanged(String message) {
		progressDialog.setMessage(message);
	}

	public void stopProgess() {
		progressDialog.cancel();
	}
}
