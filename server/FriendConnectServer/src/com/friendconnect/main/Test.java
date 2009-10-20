package com.friendconnect.main;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

public class Test {

	/**
	 * @param args
	 * @throws MalformedURLException 
	 * @throws XmlRpcException 
	 */
	public static void main(String[] args) throws MalformedURLException, XmlRpcException {
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

        Object[] params = new Object[0];
        String result = (String) client.execute("XMLRPCGateway.getFirstname", params);
        System.out.println(result);
        
        // make the a regular call
//        Object[] params = new Object[]
//            { new Integer(2), new Integer(3) };
        
        
        
        
//        Integer result = (Integer) client.execute(".add", params);
//        System.out.println("2 + 3 = " + result);
//      
//        // make a call using dynamic proxy
//        ClientFactory factory = new ClientFactory(client);
//        Adder adder = (Adder) factory.newInstance(Adder.class);
//        int sum = adder.add(2, 4);
//        System.out.println("2 + 4 = " + sum);
	}

}
