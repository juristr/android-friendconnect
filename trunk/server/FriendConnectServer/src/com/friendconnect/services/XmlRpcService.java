package com.friendconnect.services;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.friendconnect.model.User;
import com.friendconnect.xmlrpc.ObjectSerializer;
import com.google.gdata.util.AuthenticationException;

public class XmlRpcService {
	private IUserService userService;
	private ObjectSerializer serializer;

	public XmlRpcService() {
	}

	/**
	 * @param username
	 * @param password
	 * @return
	 * @throws AuthenticationException
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public Object login(String username, String password) throws AuthenticationException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		User user = null;
		user = userService.authenticate(username, password);
		
		return serializer.serialize(user);
	}
	
	public List getFriends(String userId) throws IOException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		 List result = new ArrayList();

		ObjectSerializer serializer = new ObjectSerializer();

		boolean isAuthenticated = true; // authService.validateToken(username,
										// token);
		if (isAuthenticated) {
			List<User> friends = userService.getFriends(userId);

			// serialize
			for (User friend : friends) {
				//erase token s.t. it is not send to clients
				friend.setToken(null);
				result.add(serializer.serialize(friend));
			}
		} else {
			// TODO handle authentication failure appropriately, once for all
			// method calls
		}

		return result;
	}
	
	public boolean addFriend(String userId, String token, String friendEmailAddress) {
		if (userService.validateToken(userId, token)) {
			userService.addFriend(userId, friendEmailAddress);
			return true;
		}
		return false;
	}
	
	public boolean removeFriend(String userId, String token, String friendId) {
		if (userService.validateToken(userId, token)) {
			userService.removeFriend(userId, friendId);
			return true;
		}
		return false;
	}

	public int getSimpleRCPTestResult(int x) {
		return x++;
	}
	

	/* Getters and setters */
	
	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public ObjectSerializer getSerializer() {
		return serializer;
	}

	public void setSerializer(ObjectSerializer serializer) {
		this.serializer = serializer;
	}
}
