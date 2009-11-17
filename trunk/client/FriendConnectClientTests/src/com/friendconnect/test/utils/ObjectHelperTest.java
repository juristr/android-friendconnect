package com.friendconnect.test.utils;

import java.lang.reflect.InvocationTargetException;

import junit.framework.TestCase;

import com.friendconnect.model.Location;
import com.friendconnect.model.User;
import com.friendconnect.utils.ObjectHelper;

public class ObjectHelperTest extends TestCase {
	private ObjectHelper objectHelper;

	protected void setUp() throws Exception {
		super.setUp();
		objectHelper = new ObjectHelper();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		objectHelper = null;
	}

	public void testSyncObjectGraph() throws IllegalArgumentException,
			SecurityException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		User original = new User();
		original.setId("1");
		original.setName("Juri Strumpflohner");
		original.setStatusMessage("testing...");
//		Location location = new Location();
//		location.setLatitude(12.4);
//		location.setLongitude(112.4);
//		original.setPosition(location);

		User updatedRecord = new User();
		updatedRecord.setId("1");
		updatedRecord.setName("Juri Strumpflohner");
		updatedRecord.setStatusMessage("testing...Android-FriendConnect");
		Location locationUpdated = new Location();
		locationUpdated.setLatitude(12.4);
		locationUpdated.setLongitude(112.4);
		updatedRecord.setPosition(locationUpdated);

		// sync
		objectHelper.syncObjectGraph(original, updatedRecord);

		assertEquals(updatedRecord.getId(), original.getId());
		assertEquals(updatedRecord.getName(), original.getName());
		assertEquals(updatedRecord.getStatusMessage(), original
				.getStatusMessage());
		assertEquals(updatedRecord.getPosition().getLatitude(), original
				.getPosition().getLatitude());
		assertEquals(updatedRecord.getPosition().getLongitude(), original
				.getPosition().getLongitude());

		updatedRecord = new User();
		updatedRecord.setId("1");
		updatedRecord.setName("Juri Strumpflohner");
		// no status message (= null)
		// no location (= null

		objectHelper.syncObjectGraph(original, updatedRecord);

		assertEquals(updatedRecord.getId(), original.getId());
		assertEquals(updatedRecord.getName(), original.getName());
		assertEquals(updatedRecord.getStatusMessage(), original
				.getStatusMessage());
		assertEquals(updatedRecord.getPosition(), original
				.getPosition()); //both should be null

	}
}
