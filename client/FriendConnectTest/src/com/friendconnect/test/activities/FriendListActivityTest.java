package com.friendconnect.test.activities;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.ListView;

import com.friendconnect.activities.FriendListActivity;
import com.friendconnect.activities.R;
import com.friendconnect.controller.FriendListController;
import com.friendconnect.model.Friend;

public class FriendListActivityTest extends ActivityUnitTestCase<FriendListActivity> {
	private Intent startIntent;
	
	public FriendListActivityTest() {
		super(FriendListActivity.class);
	}

	@Override
    public void setUp() throws Exception {
      super.setUp();
      startIntent = new Intent(getInstrumentation().getContext(), FriendListActivity.class);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
    @MediumTest
    public void testPreconditions(){
		startActivity(startIntent, null, null);
		assertNotNull(getActivity());
		assertNotNull(getActivity().findViewById(R.id.listViewFriends));
	}
	
	@MediumTest
	public void testMVCBindings() throws Exception {
		startActivity(startIntent, null, null);
		
		ListView friendListView = (ListView) getActivity().findViewById(R.id.listViewFriends);
		FriendListController friendListController = ((FriendListActivity)getActivity()).getController();
		
		Friend friend = new Friend(1, "Matthias", "", "matthias.braunhofer@gmail.com", "", "");
		
		int initialNumberOfItems = friendListView.getCount();
		
		friendListController.addFriend(friend);
						
		int numberOfItems = friendListView.getCount();
		
		assertTrue("Number of childviews should have increased", numberOfItems == initialNumberOfItems + 1);
    }
}
