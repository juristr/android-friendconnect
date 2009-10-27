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

package com.friendconnect.server.tests.xmlrpc;

import java.io.IOException;
import junit.framework.TestCase;
import com.friendconnect.model.Friend;
import com.friendconnect.xmlrpc.ObjectSerializer;

public class ObjectSerializerTest extends TestCase {
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testSerializeDeserializeFriend() throws IOException, ClassNotFoundException{
		Friend friend = new Friend(1, "juri.strumpflohner@gmail.com", "Juri", "Strumpflohner", "");
		
		ObjectSerializer<Friend> friendSerializer = new ObjectSerializer<Friend>();
		byte[] serialized = friendSerializer.serialize(friend);
		
		//deserialize again
		Friend deserialized = friendSerializer.deserialize(serialized);
		
		assertNotNull(deserialized);
		assertEquals(friend.getId(), deserialized.getId());
		assertEquals(friend.getEmailAddress(), deserialized.getEmailAddress());
		assertEquals(friend.getFirstname(), deserialized.getFirstname());
		assertEquals(friend.getSurname(), deserialized.getSurname());
		assertEquals(friend.getStatusMessage(), deserialized.getStatusMessage());		
	}

}
