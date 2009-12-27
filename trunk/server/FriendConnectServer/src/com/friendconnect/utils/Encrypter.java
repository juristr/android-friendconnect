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

package com.friendconnect.utils;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.engines.RijndaelEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;

/**
 * Utility class that can be used to encrypt/decrypt passwords.
 */
public class Encrypter {

	private String key = "abc123abc123abcd";
	private BufferedBlockCipher cipher;

	public Encrypter() {
		RijndaelEngine engine = new RijndaelEngine();
		CBCBlockCipher c = new CBCBlockCipher(engine);
		cipher = new PaddedBufferedBlockCipher(c);
	}

	public byte[] encryptPassword(String pass) {
		byte[] password = pass.getBytes();
		cipher.init(true, new KeyParameter(key.getBytes()));
		byte[] encryptedPassword = new byte[cipher.getOutputSize(password.length)];

		int size = cipher.processBytes(password, 0, password.length, encryptedPassword, 0);
		try {
			cipher.doFinal(encryptedPassword, size);
		} catch (CryptoException ce) {
			System.out.println("Encryption failed");
		}
		return encryptedPassword;

	}

	public String performDecrypt(byte[] cipherText) {
		cipher.init(false, new KeyParameter(key.getBytes()));

		byte[] rv = new byte[cipher.getOutputSize(cipherText.length)];

		int oLen = cipher.processBytes(cipherText, 0, cipherText.length, rv, 0);
		try {
			cipher.doFinal(rv, oLen);
		} catch (CryptoException ce) {
			System.out.println("dencryption failed");
		}
		return new String(rv).trim();
	}

}
