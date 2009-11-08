package com.friendconnect.client.tests.controller;

import junit.framework.TestCase;

import com.friendconnect.client.tests.mocks.MockXMLRPCServiceFriendList;
import com.friendconnect.controller.FriendListController;
import com.friendconnect.model.FriendConnectUser;
import com.friendconnect.model.User;
import com.friendconnect.utils.ObjectHelper;

public class FriendListControllerTest extends TestCase {
	private FriendListController controller;
	private FriendConnectUser model;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.controller = new FriendListController();
		this.model = new FriendConnectUser();
		this.controller.registerModel(model);
		this.controller.setXmlRPCService(new MockXMLRPCServiceFriendList());
		this.controller.setObjectHelper(new ObjectHelper());
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		this.controller = null;
		this.model = null;
	}

	public void testUpdateFriendList() {
		// preload list with some friends
		User friend = new User();
		friend.setId("11293884");
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
