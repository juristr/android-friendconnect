package com.friendconnect.test.services;

import java.net.URI;

import org.xmlrpc.android.XMLRPCClient;

import android.test.AndroidTestCase;

import com.friendconnect.xmlrpc.IAsyncCallback;
import com.friendconnect.xmlrpc.XMLRPCMethod;

public class XMLRPCTest extends AndroidTestCase {
	private XMLRPCClient xmlrpcClient;
	private URI uri;
	Object lock;
	private Object actualResult; //returned by test
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		uri = URI.create("http://localhost:8080/xmlrpc");
		xmlrpcClient = new XMLRPCClient(uri);	
		lock = new Object();
	}
	
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
		uri = null;
		xmlrpcClient = null;
		actualResult = null;
		lock = null;
	}
	
	public void testSimpleRPCCall() throws InterruptedException{
		final int param = 3;
		final int expected = 4;
		
		XMLRPCMethod method = new XMLRPCMethod(xmlrpcClient, "XMLRPCGateway.getSimpleRCPTestResult", new IAsyncCallback<Object>() {
			public void onSuccess(Object result) {
				actualResult = result;
				synchronized (lock) {
					lock.notify();
				}
			}

			public void onFailure(Throwable throwable) {
				fail(throwable.getMessage());
			}
        }, Object.class);
        Object[] params = {
        		param
        };
        method.call(params);
        
        //wait
        synchronized (lock) {
			lock.wait(1000);
		}
        
        assertEquals(expected, Integer.parseInt(actualResult.toString()));        
        
	}
}
