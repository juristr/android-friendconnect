package com.friendconnect.server.tests.xmlrpc;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.friendconnect.model.Location;
import com.friendconnect.model.POIAlert;
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
		Location location = new Location();
		location.setLatitude(10.34);
		location.setLongitude(112.3);
		
		List<POIAlert> poiAlerts = new ArrayList<POIAlert>();
		POIAlert poiAlert = new POIAlert();
		poiAlert.setRadius(1);
		poiAlert.setTitle("Title");
		poiAlert.setPosition(location);
		poiAlerts.add(poiAlert);
		
		User user = new User();
		user.setId("123456");
		user.setEmailAddress("juri.strumpflohner@gmail.com");
		user.setPosition(location);
		user.setPoiAlerts(poiAlerts);
		
		ObjectSerializer serializer = new ObjectSerializer();
		Map<String, Object> serialized = serializer.serialize(user);
		
		assertNotNull("The serialized object shouldn't be null", serialized);
		assertEquals("The ids should match", user.getId(), serialized.get("Id"));
		assertEquals("The names should match", user.getName(), serialized.get("Name"));
		assertNotNull("The hashmap should have an object value for the subobj", serialized.get("Position"));
		
		//deserialize
		User deserializedUser = serializer.deSerialize(serialized, User.class);
		assertNotNull("The deserialized object shouldn't be null", deserializedUser);
		assertEquals("The ids should match", user.getId(), deserializedUser.getId());
		assertEquals("The names should match", user.getName(), deserializedUser.getName());
		assertNotNull("User's position should not be null", deserializedUser.getPosition());
		assertEquals("The latitudes should match", location.getLatitude(), deserializedUser.getPosition().getLatitude());
		assertEquals("The longitudes should match", location.getLongitude(), deserializedUser.getPosition().getLongitude());
		assertNotNull("POI alert list shouldn't be null", deserializedUser.getPoiAlerts());
		assertTrue("POI alert list should contain one POI alert", deserializedUser.getPoiAlerts().size() == 1);
		
		POIAlert deserializedPOIAlert = deserializedUser.getPoiAlerts().get(0);
		assertNotNull("POI alert shouldn't be null", deserializedPOIAlert);
		assertNotNull("POI alert's position should not be null", deserializedPOIAlert.getPosition());
		assertEquals("POI alert's latitudes should match", location.getLatitude(), deserializedPOIAlert.getPosition().getLatitude());
		assertEquals("POI alert's longitudes should match", location.getLongitude(), deserializedPOIAlert.getPosition().getLongitude());
		assertEquals("POI alert's titles should match", poiAlert.getTitle(), deserializedPOIAlert.getTitle());
	}
}
