package com.friendconnect.client.tests.mocks;

import java.util.ArrayList;
import java.util.List;

import com.friendconnect.model.User;
import com.friendconnect.services.IXMLRPCService;
import com.friendconnect.xmlrpc.IAsyncCallback;

public class MockXMLRPCServiceFriendList implements IXMLRPCService {

	public <T> void sendRequest(String remoteMethod, Object[] params,
			IAsyncCallback<T> callback, Class baseClazz) {
		
		List<User> friends = new ArrayList<User>();
		
		User friend = new User();
		friend.setId("11293884");
		friend.setEmailAddress("firstname.lastname@somedomain.com");
		friend.setName("Juri");
		friend.setStatusMessage("Hi FriendConnect!");
		friend.setPhone("01220 3020030");
		friends.add(friend);
		
		friend = new User();
		friend.setId("1827312738");
		friend.setEmailAddress("firstname.lastname@somedomain.com");
		friend.setName("Matthias");
		friend.setPhone("01220 3020030");
		friends.add(friend);
		
		
		
		callback.onSuccess((T)friends);
	}

}
