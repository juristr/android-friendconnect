package com.friendconnect.client.tests.controller;

import com.friendconnect.client.tests.mocks.MockXMLRPCServiceLogin;
import com.friendconnect.controller.LoginController;
import com.friendconnect.main.FriendConnectApplication;
import com.friendconnect.main.IFriendConnectApplication;
import com.friendconnect.services.IXMLRPCService;

import junit.framework.TestCase;
import static org.mockito.Mockito.*;

public class LoginControllerTest extends TestCase {
	private IFriendConnectApplication application;
	private LoginController loginController;
	private IXMLRPCService mockXmlRpcService;
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		loginController = new LoginController();
		application = new FriendConnectApplication();
		mockXmlRpcService = new MockXMLRPCServiceLogin();
		loginController.setApplication(application);
		loginController.setXmlRpcService(mockXmlRpcService);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		loginController = null;
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
