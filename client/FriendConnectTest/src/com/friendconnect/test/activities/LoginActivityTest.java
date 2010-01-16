package com.friendconnect.test.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.friendconnect.R;
import com.friendconnect.activities.FriendListActivity;
import com.friendconnect.activities.LoginActivity;
import com.friendconnect.model.Constants;

public class LoginActivityTest extends ActivityUnitTestCase<LoginActivity> {
	private Intent startIntent;
	private String username;
	private String password;

	public LoginActivityTest() {
		super(LoginActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		startIntent = new Intent(this.getInstrumentation().getTargetContext(), FriendListActivity.class);
		username = "";
		password = "";
		
		fail("Username + password must be specified");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		startIntent = null;
		username = null;
		password = null;
	}

	@MediumTest
	public void testPreconditions() {
		startActivity(startIntent, null, null);
		assertNotNull(getActivity());
		assertNotNull(getActivity().findViewById((R.id.editTextEmail)));
		assertNotNull(getActivity().findViewById((R.id.editTextPassword)));
		assertNotNull(getActivity().findViewById((R.id.buttonSignIn)));
		assertNotNull(getActivity().findViewById(R.id.checkBoxSavePassword));
	}

	@MediumTest
	public void testLogin() throws Exception {
		startActivity(startIntent, null, null);

		EditText editTextEmail = (EditText) getActivity().findViewById(
				R.id.editTextEmail);
		EditText editTextPassword = (EditText) getActivity().findViewById(
				R.id.editTextPassword);
		Button signInButton = (Button) getActivity().findViewById(
				R.id.buttonSignIn);

		editTextEmail.setText(username);
		editTextPassword.setText(password);
		signInButton.performClick();

		// wait some seconds
		Thread.sleep(2000);

		boolean focus = getActivity().hasWindowFocus();

		assertFalse("Activity's main window should have lost the focus", focus);
	}

	@MediumTest
	public void testLoadPreferences() {
		// clear Activity's preference store
		SharedPreferences activityPreferences = this.getInstrumentation().getTargetContext().getSharedPreferences(Constants.USER_PREFS, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = activityPreferences.edit();
		editor.putString("username", "juri");
		editor.putString("password", "juri123");
		editor.putBoolean("savePrefs", true);
		editor.commit();
		
		startActivity(startIntent, null, null);
			
		EditText editTextEmail = (EditText) getActivity().findViewById(
				R.id.editTextEmail);
		EditText editTextPassword = (EditText) getActivity().findViewById(
				R.id.editTextPassword);
		CheckBox checkBoxSavePrefs = (CheckBox) getActivity().findViewById(
				R.id.checkBoxSavePassword);

		assertEquals("should be juri", "juri", editTextEmail.getText().toString());
		assertEquals("should be juri123", "juri123", editTextPassword.getText()
				.toString());
		assertTrue("checkbox should be checked", checkBoxSavePrefs
				.isChecked());
	}
	
	@MediumTest
	public void testSavePreferences() throws Exception {
		startActivity(startIntent, null, null);

		EditText editTextEmail = (EditText) getActivity().findViewById(
				R.id.editTextEmail);
		EditText editTextPassword = (EditText) getActivity().findViewById(
				R.id.editTextPassword);
		Button signInButton = (Button) getActivity().findViewById(
				R.id.buttonSignIn);
		CheckBox checkBoxSavePrefs = (CheckBox) getActivity().findViewById(
				R.id.checkBoxSavePassword);

		editTextEmail.setText(username);
		editTextPassword.setText(password);
		checkBoxSavePrefs.setChecked(true);
		
		signInButton.performClick();
		
		// wait some seconds
		Thread.sleep(2000);
		
		//the Activity's preference store should contain the data now
		SharedPreferences activityPreferences = getActivity().getSharedPreferences(Constants.USER_PREFS, Activity.MODE_PRIVATE);
		assertEquals(username, activityPreferences.getString("username", ""));
		assertEquals(password, activityPreferences.getString("password", ""));
		assertEquals(true, activityPreferences.getBoolean("storePrefs", false));
	}
	
	@MediumTest
	public void testSavePreferences2() throws Exception {
		startActivity(startIntent, null, null);

		EditText editTextEmail = (EditText) getActivity().findViewById(
				R.id.editTextEmail);
		EditText editTextPassword = (EditText) getActivity().findViewById(
				R.id.editTextPassword);
		Button signInButton = (Button) getActivity().findViewById(
				R.id.buttonSignIn);
		CheckBox checkBoxSavePrefs = (CheckBox) getActivity().findViewById(
				R.id.checkBoxSavePassword);

		editTextEmail.setText(username);
		editTextPassword.setText(password);
		checkBoxSavePrefs.setChecked(false);
		
		signInButton.performClick();
		
		// wait some seconds
		Thread.sleep(2000);
		
		//the Activity's preference store should contain the data now
		SharedPreferences activityPreferences = getActivity().getSharedPreferences(Constants.USER_PREFS, Activity.MODE_PRIVATE);
		assertEquals("", activityPreferences.getString("username", ""));
		assertEquals("", activityPreferences.getString("password", ""));
		assertEquals(false, activityPreferences.getBoolean("storePrefs", false));
	}
}
