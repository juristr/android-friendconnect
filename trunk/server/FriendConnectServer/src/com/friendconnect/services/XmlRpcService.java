package com.friendconnect.services;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.friendconnect.model.Friend;
import com.friendconnect.xmlrpc.ObjectSerializer;

public class XmlRpcService {
	private IAuthenticationService authService;
	private IFriendService friendService;
	private ObjectSerializer serializer;

	public XmlRpcService() {
//		init();
	}

	// TODO just dummy of course :)
	private void init() {
		// TODO these initializations will be injected!!!
		friendService = new FriendService();
		authService = new AuthenticationService();
	}

	public List getFriends() throws IOException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		 List result = new ArrayList();

		ObjectSerializer serializer = new ObjectSerializer();

		boolean isAuthenticated = true; // authService.validateToken(username,
										// token);
		if (isAuthenticated) {
			List<Friend> friends = friendService.getFriends();

			// serialize
			for (Friend friend : friends) {
				result.add(serializer.serialize(friend));
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
	

	public void setAuthService(IAuthenticationService authService) {
		this.authService = authService;
	}

	public void setFriendService(IFriendService friendService) {
		this.friendService = friendService;
	}

	public void setSerializer(ObjectSerializer serializer) {
		this.serializer = serializer;
	}
}
