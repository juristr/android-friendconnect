package com.friendconnect.client.tests.controller;

import junit.framework.TestCase;

import com.friendconnect.client.tests.mocks.MockXMLRPCService;
import com.friendconnect.controller.FriendListController;
import com.friendconnect.model.Friend;
import com.friendconnect.model.User;
import com.friendconnect.utils.ObjectHelper;

public class FriendListControllerTest extends TestCase {
	private FriendListController controller;
	private User model;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.controller = new FriendListController();
		this.model = new User();
		this.controller.registerModel(model);
		this.controller.setXmlRPCService(new MockXMLRPCService());
		this.controller.setObjectHelper(new ObjectHelper());
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		this.controller = null;
		this.model = null;
	}

	public void testLoadFriends() {
		assertTrue("There should be no friends initially", model.getFriends()
				.size() == 0);
		this.controller.loadFriends();

		assertTrue("The model should contain some friends", model.getFriends()
				.size() > 0);
		assertEquals("Juri", model.getFriends().get(0).getName());
		assertEquals("Matthias", model.getFriends().get(1).getName());
	}

	public void testUpdateFriendList() {
		// preload list with some friends
		Friend friend = new Friend();
		friend.setId(11293884);
		friend.setEmailAddress("firstname.lastname@somedomain.com");
		friend.setName("Juri");
		friend.setPhone("01220 3020030");
		this.model.addFriend(friend);

		assertTrue("The model should contain one friend", model.getFriends()
				.size() == 1);
		assertEquals("The status of friend(1) should be empty", null, model
				.getFriends().get(0).getStatusMessage());

		// update the list
		this.controller.updateFriendList();

		assertTrue("The model should contain two friends", model.getFriends()
				.size() == 2);
		// check, the status of friend(1) should have been updated
		assertNotSame("The status should have been updated!", "", this.model
				.getFriends().get(0).getStatusMessage());
		assertEquals("Matthias", model.getFriends().get(1).getName());
	}
}
