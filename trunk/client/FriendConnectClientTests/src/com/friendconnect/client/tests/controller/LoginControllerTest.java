package com.friendconnect.client.tests.controller;

import junit.framework.TestCase;

import com.friendconnect.client.tests.mocks.MockXMLRPCServiceLogin;
import com.friendconnect.controller.LoginController;
import com.friendconnect.main.FriendConnectApplication;
import com.friendconnect.main.IFriendConnectApplication;
import com.friendconnect.services.IXMLRPCService;
import com.friendconnect.utils.Encrypter;

public class LoginControllerTest extends TestCase {
	private IFriendConnectApplication application;
	private LoginController loginController;
	private Encrypter encrypter;
	private IXMLRPCService mockXmlRpcService;
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		loginController = new LoginController();
		application = new FriendConnectApplication();
		mockXmlRpcService = new MockXMLRPCServiceLogin();
		encrypter = new Encrypter();
		loginController.setEncrypter(encrypter);
		loginController.setApplication(application);
		loginController.setXmlRpcService(mockXmlRpcService);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		loginController = null;
		encrypter = null;
		application = null;
		mockXmlRpcService = null;
	}
	
	public void testLogin(){
		assertNull(application.getApplicationModel());
		
		String username = "jurilogintest@gmail.com";
		
		//do the dummy login
		loginController.login(username, "myPassword");
		
		assertNotNull(application.getApplicationModel());
		assertEquals("the username should match", username, application.getApplicationModel().getEmailAddress());
		assertEquals("The token should have been set", "sometoken", application.getApplicationModel().getToken());
	}
}
