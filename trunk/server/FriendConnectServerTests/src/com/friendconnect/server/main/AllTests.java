/*   **********************************************************************  **
 **   Copyright notice                                                       **
 **                                                                          **
 **   (c) 2009, FriendConnect			                       				 **
 **   All rights reserved.                                                   **
 **                                                                          **
 **	  This program and the accompanying materials are made available under   **
 **   the terms of the GPLv3 license which accompanies this	    			 **
 **   distribution. A copy is found in the textfile LICENSE.txt				 **
 **                                                                          **
 **   This copyright notice MUST APPEAR in all copies of the file!           **
 **                                                                          **
 **   Main developers:                                                       **
 **     Juri Strumpflohner		http://blog.js-development.com	             **
 **     Matthias Braunhofer		http://matthias.jimdo.com	                 **
 **                                                                          **
 **  **********************************************************************  */

package com.friendconnect.server.main;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.friendconnect.server.tests.dao.UserDaoTest;
import com.friendconnect.server.tests.services.UserServiceTest;
import com.friendconnect.server.tests.xmlrpc.ObjectSerializerTest;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for com.friendconnect.server");
		//$JUnit-BEGIN$
		suite.addTestSuite(ObjectSerializerTest.class);
		suite.addTestSuite(UserDaoTest.class);
		suite.addTestSuite(UserServiceTest.class);
		//$JUnit-END$
		return suite;
	}

}
