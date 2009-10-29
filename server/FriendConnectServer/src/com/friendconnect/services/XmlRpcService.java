package com.friendconnect.services;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.friendconnect.model.Friend;
import com.friendconnect.xmlrpc.ObjectSerializer;
import com.google.gdata.util.AuthenticationException;

public class XmlRpcService {
	private IAuthenticationService authService;
	private IFriendService friendService;
	private ObjectSerializer serializer;

	public XmlRpcService() {
	}

	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws AuthenticationException
	 */
	public String login(String username, String password) throws AuthenticationException{
		String resultToken = null;
		resultToken = authService.authenticate(username, password);
		
		return resultToken;
	}
	
	public List getFriends() throws IOException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		 List result = new ArrayList();

		ObjectSerializer serializer = new ObjectSerializer();

		boolean isAuthenticated = true; // authService.validateToken(username,
										// token);
		if (isAuthenticated) {
			List<Friend> friends = friendService.getDummyFriends();

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
