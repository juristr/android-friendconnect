package com.friendconnect.test.activities;

import android.test.SingleLaunchActivityTestCase;
import android.widget.ListView;

import com.friendconnect.activities.FriendListActivity;
import com.friendconnect.activities.R;
import com.friendconnect.controller.FriendListController;
import com.friendconnect.model.Friend;

public class FriendListActivityTest extends SingleLaunchActivityTestCase<FriendListActivity> {
	private ListView friendListView;
	private FriendListController friendListController;
	
	public FriendListActivityTest() {
		super("com.friendconnect.activities", FriendListActivity.class);
	}

	@Override
    public void setUp() throws Exception {
      super.setUp();
      friendListController = ((FriendListActivity)getActivity()).getController();
      friendListView = (ListView) getActivity().findViewById(R.id.listViewFriends);
    }
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		friendListController = null;
		friendListView = null;
	}
	
	public void testMVCBindings() throws Exception {
		Friend friend = new Friend(1, "matthias.braunhofer@gmail.com", "Matthias", "Braunhofer", "");
		
		int initialNumberOfChildViews = friendListView.getChildCount();
		
		friendListController.addFriend(friend);
		
		int numberOfChildViews = friendListView.getChildCount();
		
		assertTrue("Number of childviews should have increased", numberOfChildViews == (initialNumberOfChildViews + 1));
    }
}
