package com.friendconnect.client.tests.main;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.friendconnect.client.tests.controller.FriendListControllerTest;
import com.friendconnect.test.utils.ObjectHelperTest;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		//$JUnit-BEGIN$
		suite.addTestSuite(FriendListControllerTest.class);
		suite.addTestSuite(ObjectHelperTest.class);
		//$JUnit-END$
		return suite;
	}

}
