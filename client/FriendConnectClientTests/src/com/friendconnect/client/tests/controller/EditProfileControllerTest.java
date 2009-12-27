package com.friendconnect.client.tests.controller;

import junit.framework.TestCase;

import com.friendconnect.client.tests.mocks.MockXMLRPCServiceEditProfile;
import com.friendconnect.controller.EditProfileController;
import com.friendconnect.model.FriendConnectUser;
import com.friendconnect.services.IXMLRPCService;
import com.friendconnect.xmlrpc.ObjectSerializer;

public class EditProfileControllerTest extends TestCase {
	private EditProfileController editProfileController;
	private FriendConnectUser model;
	private IXMLRPCService mockXmlRpcService;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		editProfileController = new EditProfileController();
		model = new FriendConnectUser();
		mockXmlRpcService = new MockXMLRPCServiceEditProfile();
		editProfileController.registerModel(model);
		editProfileController.setSerializer(new ObjectSerializer());
		editProfileController.setXmlRpcService(mockXmlRpcService);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		mockXmlRpcService = null;
		editProfileController = null;
		model = null;
	}

	public void testSaveProfile() {
		String newName = "newName";
		String newPhone = "newPhone";
		String newWebsite = "newWebsite";
		String newStatusMessage = "newStatusMessage";
		editProfileController.saveProfile(newName, newPhone, newWebsite, newStatusMessage);
		
		//The model should have been updated
		assertEquals("The names should be equals", newName, model.getName());
		assertEquals("The phones should be equal", newPhone, model.getPhone());
		assertEquals("The websites should be equal", newWebsite, model.getWebsite());
		assertEquals("The status messages should be equal", newStatusMessage, model.getStatusMessage());
	}
}
