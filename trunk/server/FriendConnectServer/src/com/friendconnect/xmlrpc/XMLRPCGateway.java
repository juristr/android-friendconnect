package com.friendconnect.xmlrpc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.friendconnect.model.Friend;
import com.friendconnect.model.TestDTO;
import com.friendconnect.services.AuthenticationService;
import com.friendconnect.services.FriendService;
import com.friendconnect.services.IAuthenticationService;
import com.friendconnect.services.IFriendService;

public class XMLRPCGateway {
	private IAuthenticationService authService; // TODO inject this
	private IFriendService friendService; // TODO inject this

	public XMLRPCGateway() {
		init();
	}

	// TODO just dummy of course :)
	private void init() {
		// TODO these initializations will be injected!!!
		friendService = new FriendService();
		authService = new AuthenticationService();
	}

	public byte[] getFirstname() throws IOException {
		Friend friend = new Friend(1, "Juri", "Juri", "Strumpflohner", "it sucks...");
		return serializeObj(friend);
	}

	public void getFriends(String username, String token) {
		init(); // TODO remove later

		boolean isAuthenticated = authService.validateToken(username, token);
		if (isAuthenticated) {
			List<Friend> friends = friendService.getFriends();

		} else {
			// TODO handle authentication failure appropriately, once for all
			// method calls
		}
	}

	public Map<String, byte[]> getFriendObject() throws IOException {
		Map<String, byte[]> map = new HashMap<String, byte[]>();
		Friend friend = new Friend();
		friend.setEmailAddress("juri.strumpflohner@gmail.com");
		friend.setFirstname("Juri");
		map.put(friend.getEmailAddress(), serializeObj(friend));

		return map;
	}

	private byte[] serializeObj(Object obj) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(obj);
		oos.flush();
		oos.close();
		bos.close();
		byte[] data = bos.toByteArray();
		return data;

	}

	public Map<String, Object> modifyObject(Map<String, Object> person)
			throws IllegalArgumentException, NoSuchMethodException,
			InvocationTargetException, IllegalAccessException {
		ObjectDeserializer<TestDTO> deserializer = new ObjectDeserializer<TestDTO>(
				TestDTO.class);

		TestDTO dto = deserializer.deSerialize(person);
		dto.setLastname("Strumpflohner");

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("firstname", dto.getFirstname());
		result.put("lastname", dto.getLastname());
		return result;
	}

	public int getSimpleRCPTestResult(int x) {
		return x++;
	}
}
