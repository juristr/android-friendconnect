package com.friendconnect.xmlrpc;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.friendconnect.model.Friend;
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

	public Map<String, byte[]> getFriends(String username, String token) throws IOException {
		Map<String, byte[]> result = new HashMap<String, byte[]>();
		
		ObjectSerializer<Friend> serializer = new ObjectSerializer<Friend>();
		
		boolean isAuthenticated = authService.validateToken(username, token);
		if (isAuthenticated) {
			List<Friend> friends = friendService.getFriends();

			//serialize
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
