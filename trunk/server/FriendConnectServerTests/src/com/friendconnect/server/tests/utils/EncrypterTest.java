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

package com.friendconnect.server.tests.utils;

import com.friendconnect.utils.Encrypter;

import junit.framework.TestCase;

public class EncrypterTest extends TestCase {
	private Encrypter encrypter;
	private String password;

	protected void setUp() throws Exception {
		super.setUp();
		encrypter = new Encrypter();
		password = "mySecretPassword";
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		encrypter = null;
		password = null;
	}

	public void testEncryptDecryptPassword() {
		byte[] encryptedPassword = encrypter.encryptPassword(password);
		assertNotNull("Encrypted password should not be null", encryptedPassword);
		
		String decryptedPassword = encrypter.performDecrypt(encryptedPassword);
		assertNotNull("Decrypted password should not be null", decryptedPassword);
		assertEquals("Passwords should be equal", password, decryptedPassword);
	}
}
