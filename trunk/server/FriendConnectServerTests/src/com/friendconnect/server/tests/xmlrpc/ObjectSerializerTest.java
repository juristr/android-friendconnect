package com.friendconnect.server.tests.xmlrpc;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import junit.framework.TestCase;

import com.friendconnect.model.Location;
import com.friendconnect.model.User;
import com.friendconnect.xmlrpc.ObjectSerializer;

public class ObjectSerializerTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testObjectDeserializer() throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		User friend = new User();
		friend.setId("123456");
		friend.setEmailAddress("juri.strumpflohner@gmail.com");
		Location location = new Location();
		location.setLatitude(10.34);
		location.setLongitude(112.3);
		friend.setPosition(location);
		
		ObjectSerializer serializer = new ObjectSerializer();
		Map<String, Object> serialized = serializer.serialize(friend);
		
		assertNotNull("the serialized object shouldn't be null", serialized);
		assertEquals("The ids should match", friend.getId(), serialized.get("Id"));
		assertEquals("The names should match", friend.getName(), serialized.get("Name"));
		assertNotNull("The hashmap should have an object value for the subobj", serialized.get("Position"));
		
		
		//deserialize
		User deserializedUser = serializer.deSerialize(serialized, User.class);
		assertNotNull(deserializedUser);
		assertEquals(friend.getId(), deserializedUser.getId());
		assertEquals(friend.getName(), deserializedUser.getName());
		assertEquals(friend.getPosition().getLatitude(), deserializedUser.getPosition().getLatitude());
		assertEquals(friend.getPosition().getLongitude(), deserializedUser.getPosition().getLongitude());
	}

}
