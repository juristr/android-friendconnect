package com.friendconnect.services;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.friendconnect.model.User;
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
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public String login(String username, String password) throws AuthenticationException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		
		String resultToken = authService.authenticate(username, password);
		return resultToken;
		
//		if(resultToken != null){
//			User friendConnectUser; friendConnectUser = friendService.getFriendConnectUser(username);
//			if(friendConnectUser != null){
//				return serializer.serialize(friendConnectUser);
//			}else{
//				//register the user, i.e. put it into the FriendConnect DB
//				return null;
//			}
//		}else{
//			throw new AuthenticationException("Error logging in!");
//		}
	}
	
	public List getFriends(String username, String token) throws IOException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		 List result = new ArrayList();

		ObjectSerializer serializer = new ObjectSerializer();
		
		boolean isAuthenticated = authService.validateToken(username, token);
		if (isAuthenticated) {
			List<User> friends = friendService.getDummyFriends();

			// serialize
			for (User friend : friends) {
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
