package com.friendconnect.server.tests;

import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

public class RPCCallsTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	/**
	 * Simple test for quickly verifying whether the RPC service is correctly running
	 * @throws MalformedURLException
	 * @throws XmlRpcException
	 */
	public void testSimpleRPCCall() throws MalformedURLException, XmlRpcException{
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL("http://127.0.0.1:8080/xmlrpc"));
        config.setEnabledForExtensions(true);  
        config.setConnectionTimeout(60 * 1000);
        config.setReplyTimeout(60 * 1000);

        XmlRpcClient client = new XmlRpcClient();
      
        // use Commons HttpClient as transport
        client.setTransportFactory(
            new XmlRpcCommonsTransportFactory(client));
        // set configuration
        client.setConfig(config);

        int startInt = 3;
        Object[] params = new Object[]{startInt};
        int result = Integer.parseInt(client.execute("XMLRPCGateway.getSimpleRCPTestString", params).toString());
        
        assertEquals(startInt++, result);   
	}
}
