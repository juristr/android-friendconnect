package com.friendconnect.services;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.friendconnect.model.Location;
import com.friendconnect.model.POIAlert;
import com.friendconnect.model.User;
import com.friendconnect.xmlrpc.ObjectSerializer;
import com.google.gdata.util.AuthenticationException;

public class XmlRpcService {
	private IUserService userService;
	private ObjectSerializer serializer;

	public XmlRpcService() {
	}

	/**
	 * RPC method to retrieve a user object by passing username and password
	 * @param username the username, i.e. the Google Account email of the user 
	 * @param password the password, i.e. the Google Account password
	 * @return user, if the authentication was successful
	 * @throws AuthenticationException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Object login(String username, String password) throws AuthenticationException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		User user = userService.authenticate(username, password);
		return serializer.serialize(user);
	}
	
	/**
	 * Helper method that validates the user's token
	 * @param userId
	 * @param token
	 * @throws AuthenticationException
	 */
	private void authenticateUser(String userId, String token) throws AuthenticationException {
		if (!userService.validateToken(userId, token)) {
			throw new AuthenticationException("Can't authenticate user: userid and/or token are invalid");
		}
	}

	/**
	 * RPC method to retrieve a list of serialized friends
	 * @param userId the user id
	 * @param token the user token
	 * @return list of serialized friends
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws AuthenticationException
	 */
	public List<Map<String, Object>> getFriends(String userId, String token) throws IOException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, AuthenticationException {	
		// Authenticate user
		authenticateUser(userId, token);
		
		// User has been successfully authenticated
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		ObjectSerializer serializer = new ObjectSerializer();
		List<User> friends = userService.getFriends(userId);

		// serialize
		for (User friend : friends) {
			result.add(serializer.serialize(friend));
		}
		
		return result;
	}

	/**
	 * RPC method to retrieve a list of serialized pending friends
	 * @param userId the user id
	 * @param token the user token
	 * @return list of serialized pending friends
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws AuthenticationException
	 */
	public List<Map<String, Object>> retrievePendingInvites(String userId, String token) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, AuthenticationException {
		// Authenticate user
		authenticateUser(userId, token);
		
		// User has been successfully authenticated
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		ObjectSerializer serializer = new ObjectSerializer();
		List<User> friends = userService.getPendingInvites(userId);

		// serialize
		for (User friend : friends) {
			result.add(serializer.serialize(friend));
		}
		
		return result;
	}

	/**
	 * RPC method to invite a friend
	 * @param userId the user id
	 * @param token the user token
	 * @param friendEmailAddress the e-mail address of the invited friend
	 * @return true on success
	 * @throws AuthenticationException
	 */
	public boolean addFriendInvite(String userId, String token, String friendEmailAddress) throws AuthenticationException {
		// Authenticate user
		authenticateUser(userId, token);
		
		// User has been successfully authenticated
		userService.addFriendInvite(userId, friendEmailAddress);
		
		return true;
	}

	/**
	 * RPC method to reject a friend request
	 * @param userId the user id
	 * @param token the user token
	 * @param friendIdToReject the friend id
	 * @return true on success
	 * @throws AuthenticationException
	 */
	public boolean rejectFriendInvite(String userId, String token, String friendIdToReject) throws AuthenticationException {
		// Authenticate user
		authenticateUser(userId, token);
		
		// User has been successfully authenticated
		userService.rejectFriendInvite(userId, friendIdToReject);
		return true;
	
	}

	/**
	 * RPC method to accept a friend request
	 * @param userId the user id
	 * @param token the user token
	 * @param friendId the friend id
	 * @return true on success
	 * @throws AuthenticationException
	 */
	public boolean acceptFriendInvite(String userId, String token, String friendId) throws AuthenticationException {
		// Authenticate user
		authenticateUser(userId, token);
		
		// User has been successfully authenticated
		userService.acceptFriendInvite(userId, friendId);
		return true;
	}

	/**
	 * RPC method to remove a friend
	 * @param userId the user id
	 * @param token the user token
	 * @param friendId the friend id
	 * @return true on success
	 * @throws AuthenticationException
	 */
	public boolean removeFriend(String userId, String token, String friendId) throws AuthenticationException {
		// Authenticate user
		authenticateUser(userId, token);
		
		// User has been successfully authenticated
		userService.removeFriend(userId, friendId);
		return true;
	}

	/**
	 * RPC method to update a user profile
	 * @param userId the user id
	 * @param token the user token
	 * @param userData the serialized user data
	 * @return true on success
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws AuthenticationException
	 */
	public boolean updateUserProfile(String userId, String token, Map<String, Object> userData) throws IllegalArgumentException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, AuthenticationException {
		// Authenticate user
		authenticateUser(userId, token);
		
		// User has been successfully authenticated
		User userToSave = serializer.deSerialize(userData, User.class);
		userToSave.setLastAccess(new Date());
		userToSave.setOnline(true);
			
		userService.updateUser(userToSave);
		return true;
	}
	
	/**
	 * RPC method to update a user location
	 * @param userId the user id
	 * @param token the user token
	 * @param locationData the serialized location data 
	 * @return true on success
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws AuthenticationException
	 */
	public boolean updateUserLocation(String userId, String token, Map<String, Object> locationData) throws IllegalArgumentException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, AuthenticationException{
		// Authenticate user
		authenticateUser(userId, token);
		
		// User has been successfully authenticated
		Location userLocation = serializer.deSerialize(locationData, Location.class);
		userService.updateUserLocation(userId, userLocation);
		return true;
	}
	
	/**
	 * RPC method to create a new POI alert
	 * @param userId the user id
	 * @param token the user token
	 * @param poiAlertData the serialized POI alert data
	 * @return POI alert id on success
	 * @throws AuthenticationException
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public String addPOIAlert(String userId, String token, Map<String, Object> poiAlertData) throws AuthenticationException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		// Authenticate user
		authenticateUser(userId, token);
		
		// User has been successfully authenticated
		POIAlert poiAlert = serializer.deSerialize(poiAlertData, POIAlert.class);
		userService.addPOIAlert(userId, poiAlert);
		return poiAlert.getId();
	}
	
	/**
	 * RPC method to remove a POI alert
	 * @param userId the user id
	 * @param token the user token
	 * @param poiAlertId the POI alert id
	 * @return true on success
	 * @throws AuthenticationException
	 */
	public boolean removePOIAlert(String userId, String token, String poiAlertId) throws AuthenticationException {
		// Authenticate user
		authenticateUser(userId, token);
		
		// User has been successfully authenticated
		userService.removePOIAlert(poiAlertId);
		return true;
	}
	
	/**
	 * RPC method to retrieve a list of serialized POI alerts
	 * @param userId the user id
	 * @param token the user token
	 * @return list of serialized POI alerts
	 * @throws AuthenticationException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public List<Map<String, Object>> retrievePOIAlerts(String userId, String token) throws AuthenticationException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		// Authenticate user
		authenticateUser(userId, token);
		
		// User has been successfully authenticated
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		ObjectSerializer serializer = new ObjectSerializer();
		List<POIAlert> poiAlerts = userService.getPOIAlerts(userId);

		// serialize
		for (POIAlert poiAlert : poiAlerts) {
			result.add(serializer.serialize(poiAlert));
		}
		
		return result;
	}
	
	/**
	 * RPC method to update a POI alert
	 * @param userId the user id
	 * @param token the user token
	 * @param poiAlertData the serialized POI alert data
	 * @return true on success
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws AuthenticationException
	 */
	public boolean updatePOIAlert(String userId, String token, Map<String, Object> poiAlertData) throws IllegalArgumentException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, AuthenticationException {
		// Authenticate user
		authenticateUser(userId, token);
		
		// User has been successfully authenticated
		POIAlert poiAlert = serializer.deSerialize(poiAlertData, POIAlert.class);
		userService.updatePOIAlert(userId, poiAlert);
		return true;
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
