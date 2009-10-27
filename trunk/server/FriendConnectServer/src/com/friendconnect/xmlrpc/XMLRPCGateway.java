package com.friendconnect.xmlrpc;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.friendconnect.model.Friend;
import com.friendconnect.model.Location;
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

	public Map<String, Object> getFriend() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Friend friend = new Friend(1, "juri.strumpflohner@gmail.com", "Juri",
				"Strumpflohner", "");
		Location location = new Location();
		location.setLatitude(10.34);
		location.setLongitude(112.3);
		friend.setPosition(location);
		
		return new ObjectSerializer().serialize(friend);
	}

	public Map<String, Object> getFriends() throws IOException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		 Map<String, Object> result = new HashMap<String, Object>();

		ObjectSerializer serializer = new ObjectSerializer();

		boolean isAuthenticated = true; // authService.validateToken(username,
										// token);
		if (isAuthenticated) {
			List<Friend> friends = friendService.getFriends();

			// serialize
			for (Friend friend : friends) {
				result.put(friend.getEmailAddress(), serializer.serialize(friend));
			}
		} else {
			// TODO handle authentication failure appropriately, once for all
			// method calls
		}

		return result;
	}

	public int getSimpleRCPTestResult(int x) {
		return x++;
	}
}
