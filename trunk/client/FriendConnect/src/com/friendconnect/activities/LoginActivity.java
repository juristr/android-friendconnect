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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.friendconnect.controller.LoginController;
import com.friendconnect.main.IoC;
import com.friendconnect.model.Constants;
import com.friendconnect.model.LoginResult;

/**
 * View for performing the user login
 * 
 */
public class LoginActivity extends Activity implements IView {
	private Button signInButton;
	private ProgressDialog progressDialog;
	private LoginController controller;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginview);

		progressDialog = new ProgressDialog(this);

		controller = IoC.getInstance(LoginController.class);
		controller.registerView(this);

		loadActivityPreferences();
		final Toast toast = Toast.makeText(this, R.string.uiMessageProvideUsernamePassword, 3000);

		signInButton = (Button) this.findViewById(R.id.buttonSignIn);
		signInButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String username = ((EditText) findViewById(R.id.editTextEmail)).getText().toString();
				String password = ((EditText) findViewById(R.id.editTextPassword)).getText().toString();

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
		SharedPreferences activityPreferences = getSharedPreferences(
				Constants.USER_PREFS, MODE_PRIVATE);

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
		SharedPreferences activityPreferences = getSharedPreferences(
				Constants.USER_PREFS, MODE_PRIVATE);
		SharedPreferences.Editor editor = activityPreferences.edit();

		if (doSavePreferences()) {

			String username = ((EditText) findViewById(R.id.editTextEmail)).getText().toString();
			String password = ((EditText) findViewById(R.id.editTextPassword)).getText().toString();

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
			startActivity(new Intent(LoginActivity.this,
					FriendListActivity.class));
			finish();
		} else {
			//TODO factor this out to a factory or somewhere?
			AlertDialog.Builder ad = new AlertDialog.Builder(this); 
			ad.setTitle(R.string.uiMessageLoginErrorTitle); 
			ad.setMessage(R.string.uiMessageLoginErrorMsg); 
			ad.setPositiveButton("Ok",
					new android.content.DialogInterface.OnClickListener() { 
						public void onClick(DialogInterface dialog,	int arg1) {
						} 
					});
			ad.show();
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