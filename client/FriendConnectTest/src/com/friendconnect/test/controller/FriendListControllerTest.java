package com.friendconnect.test.controller;

import android.test.AndroidTestCase;

import com.friendconnect.controller.FriendListController;
import com.friendconnect.model.User;

public class FriendListControllerTest extends AndroidTestCase {
	private FriendListController controller;
	private User model;
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.controller = new FriendListController();
		this.model = new User();
		this.controller.registerModel(model);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		this.controller = null;
		this.model = null;
	}
	
	public void testLoadFriends(){
		assertTrue("There should be no friends initially", model.getFriends().size() == 0);
		
		//not yet finished...have to find strategy for testing async calls
		//probably the callback has to be done externally and passed as parameter to the controller
		//for instance. This would however require to integrate the deserializer s.t. in the
		//onSuccess method of the callback we get already the finished usable, deserialized object
		//should be doable
	}
}
