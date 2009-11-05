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
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.friendconnect.R;
import com.friendconnect.activities.FriendListActivity;
import com.friendconnect.activities.IView;
import com.friendconnect.controller.LoginController;
import com.friendconnect.model.Constants;
import com.friendconnect.model.LoginResult;

/**
 * View for performing the user login
 * 
 */
public class FriendConnectActivity extends Activity implements IView {
	private Button signInButton;
	private ProgressDialog progressDialog;
	private LoginController controller;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginview);

		progressDialog = new ProgressDialog(this);

		this.controller = IoC.getInstance(LoginController.class);
		this.controller.registerView(this);

		loadActivityPreferences();
		final Toast toast = Toast.makeText(this,
				R.string.uiMessageProvideUsernamePassword, 3000);

		signInButton = (Button) this.findViewById(R.id.buttonSignIn);
		signInButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String username = ((EditText) findViewById(R.id.editTextEmail))
						.getText().toString();
				String password = ((EditText) findViewById(R.id.editTextPassword))
						.getText().toString();

				if (username.equals("") || password.equals("")) {
					toast.show();
				} else {
					progressDialog.setMessage(getText(R.string.uiMessageLogin));
					progressDialog.show();

					controller.login(username, password);
				}
			}
		});
	}

	private boolean doSavePreferences() {
		CheckBox savePrefs = (CheckBox) findViewById(R.id.checkBoxSavePassword);
		return savePrefs.isChecked();
	}

	protected void loadActivityPreferences() {
		SharedPreferences activityPreferences = ((IFriendConnectApplication)getApplication()).getGlobalApplicationPreferences();

		String username = activityPreferences.getString("username", "");
		String password = activityPreferences.getString("password", "");
		boolean savePrefs = activityPreferences.getBoolean("savePrefs", false);

		EditText editTextUsername = ((EditText) findViewById(R.id.editTextEmail));
		EditText editTextPassword = ((EditText) findViewById(R.id.editTextPassword));
		CheckBox checkBoxSavePrefs = (CheckBox) findViewById(R.id.checkBoxSavePassword);

		editTextUsername.setText(username);
		editTextPassword.setText(password);
		checkBoxSavePrefs.setChecked(savePrefs);
	}

	protected void saveActivityPreferences() {
		SharedPreferences activityPreferences = ((IFriendConnectApplication)getApplication()).getGlobalApplicationPreferences();
		SharedPreferences.Editor editor = activityPreferences.edit();

		if (doSavePreferences()) {

			String username = ((EditText) findViewById(R.id.editTextEmail))
					.getText().toString();
			String password = ((EditText) findViewById(R.id.editTextPassword))
					.getText().toString();

			editor.putString("username", username);
			editor.putString("password", password);
			editor.putBoolean("savePrefs", true);
		} else {
			editor.clear();
		}

		editor.commit();

	}

	public void update(Observable observable, Object data) {
		LoginResult result = (LoginResult) observable;

		if (result.isLoginSucceeded()) {
			saveActivityPreferences();
			startActivity(new Intent(FriendConnectActivity.this,
					FriendListActivity.class));
			finish();
		}
	}

	public void onProgressChanged(String message) {
		if (message != null && !message.equals("")) {
			progressDialog.setMessage(message);
		}
	}
	
	public void stopProgess() {
		progressDialog.cancel();
	}
}