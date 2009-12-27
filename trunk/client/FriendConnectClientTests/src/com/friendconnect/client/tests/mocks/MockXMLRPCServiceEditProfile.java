package com.friendconnect.client.tests.mocks;

import com.friendconnect.services.IXMLRPCService;
import com.friendconnect.xmlrpc.IAsyncCallback;

public class MockXMLRPCServiceEditProfile implements IXMLRPCService {

	@Override
	public <T> void sendRequest(String remoteMethod, Object[] params,
			IAsyncCallback<T> callback, Class baseClazz) {
		
		Boolean bool = new Boolean(true);
		callback.onSuccess((T) bool);
	}

}
