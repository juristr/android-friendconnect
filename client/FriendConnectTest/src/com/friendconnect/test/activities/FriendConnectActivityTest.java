package com.friendconnect.test.activities;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;
import android.widget.EditText;

import com.friendconnect.activities.R;
import com.friendconnect.main.FriendConnectActivity;

public class FriendConnectActivityTest extends ActivityUnitTestCase<FriendConnectActivity> {
	private Intent startIntent;
	
	public FriendConnectActivityTest() {
		super(FriendConnectActivity.class);
	}

	@Override
    public void setUp() throws Exception {
      super.setUp();
      startIntent = new Intent(Intent.ACTION_MAIN);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
    @MediumTest
    public void testPreconditions(){
		startActivity(startIntent, null, null);
		assertNotNull(getActivity());
		assertNotNull(getActivity().findViewById((R.id.editTextEmail)));
		assertNotNull(getActivity().findViewById((R.id.editTextPassword)));
		assertNotNull(getActivity().findViewById((R.id.buttonSignIn)));
	}
	
	@MediumTest
	public void testLogin() throws Exception {
		startActivity(startIntent, null, null);
		
		EditText editTextEmail = (EditText) getActivity().findViewById(R.id.editTextEmail);
		EditText editTextPassword = (EditText) getActivity().findViewById(R.id.editTextPassword);
		Button signInButton = (Button) getActivity().findViewById(R.id.buttonSignIn);
		
		String username = "";
		String password = "";
		
		fail("Username + password must be specified");
		
		editTextEmail.setText(username);
		editTextPassword.setText(password);
		signInButton.performClick();
		
		//wait some seconds
		Thread.sleep(2000);
		
		boolean focus = getActivity().hasWindowFocus();
		
		assertFalse("Activity's main window should have lost the focus", focus);
    }
}
