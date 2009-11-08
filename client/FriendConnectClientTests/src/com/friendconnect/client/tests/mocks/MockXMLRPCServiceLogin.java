package com.friendconnect.client.tests.mocks;

import com.friendconnect.model.FriendConnectUser;
import com.friendconnect.services.IXMLRPCService;
import com.friendconnect.xmlrpc.IAsyncCallback;

public class MockXMLRPCServiceLogin implements IXMLRPCService {

	@Override
	public <T> void sendRequest(String remoteMethod, Object[] params,
			IAsyncCallback<T> callback, Class baseClazz) {
		
		try {
			T instance = (T) baseClazz.newInstance();
			
			FriendConnectUser user = (FriendConnectUser) instance;
			user.setEmailAddress(params[0].toString());
			user.setToken("sometoken");
			
			callback.onSuccess((T) user);
		} catch (InstantiationException e) {
			callback.onFailure(e);
		} catch (IllegalAccessException e) {
			callback.onFailure(e);
		}
	}

}
