package com.friendconnect.server.tests;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.friendconnect.xmlrpc.ObjectDeserializer;

public class ObjectDeserializerTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testObjectDeserializer() throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Firstname", "Juri");
		map.put("lastname", "Strumpflohner");
//		map.put("Age", 24);
		
		ObjectDeserializer<Person> deserializer = new ObjectDeserializer<Person>(
				Person.class);

		Person personInstance = deserializer.deSerialize(map);
		
		assertNotNull(personInstance);
		assertEquals("Juri",personInstance.getFirstname());
		assertEquals("Strumpflohner", personInstance.getLastname());
//		assertEquals(24, personInstance.getAge());
	}

}
