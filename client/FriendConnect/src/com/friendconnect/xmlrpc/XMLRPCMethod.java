/*   **********************************************************************  **
 **   Copyright notice                                                       **
 **                                                                          **
 **   (c) 2009, FriendConnect			                       				 **
 **   All rights reserved.                                                   **
 **                                                                          **
 **	  This program and the accompanying materials are made available under   **
 **   the terms of the GPLv3 license which accompanies this	    			 **
 **   distribution. A copy is found in the textfile LICENSE.txt				 **
 **                                                                          **
 **   This copyright notice MUST APPEAR in all copies of the file!           **
 **                                                                          **
 **   Main developers:                                                       **
 **     Juri Strumpflohner		http://blog.js-development.com	             **
 **     Matthias Braunhofer		http://matthias.jimdo.com	                 **
 **                                                                          **
 **  **********************************************************************  */

package com.friendconnect.xmlrpc;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;
import org.xmlrpc.android.XMLRPCFault;
import android.os.Handler;
import android.util.Log;

public class XMLRPCMethod<T> extends Thread {
	private String method;
	private Object[] params;
	private Handler handler;
	private IAsyncCallback<T> callBack;
	private Class clazz;
	private XMLRPCClient client;
	private ObjectSerializer serializer;
	

	/**
	 * 
	 * @param client the {@link XMLRPCClient} object
	 * @param method the identifier of the method to invoke on the server
	 * @param callBack the {@link IAsyncCallback}
	 * @param clazz the Class of the base type object to be de-serialized
	 */
	public XMLRPCMethod(XMLRPCClient client, String method, IAsyncCallback<T> callBack, Class clazz) {
		this.client = client;
		this.method = method;
		this.callBack = callBack;
		handler = new Handler();
		serializer = new ObjectSerializer();
		this.clazz = clazz;
	}
	
	/**
	 * Just intended for testing purposes (synchronous)
	 * @param client
	 * @param method
	 * @param params
	 */
//	public XMLRPCMethod(XMLRPCClient client, String method, Object[] params){
//		this.client = client;
//		this.method = method;
//		this.params = params;
//	}

	public void call() {
		call(null);
	}

	public void call(Object[] params) {
		this.params = params;
		start();
	}

	@Override
	public void run() {
		try {
			//TODO serialization of objects??
			final Object result = client.callEx(method, params);
			
			handler.post(new Runnable() {
				public void run() {
					T deserializedResult = null;
					try {
						deserializedResult = deserializeResult(result);
					} catch (Exception ex) {
						Log.e(ex.getClass().getCanonicalName(), ex.getMessage());
						callBack.onFailure(ex);
					}
					
					callBack.onSuccess(deserializedResult);
				}

				/**
				 * Deserialize the received object using the ObjectSerializer
				 * @param result
				 * @return
				 * @throws IllegalArgumentException
				 * @throws NoSuchMethodException
				 * @throws InvocationTargetException
				 * @throws IllegalAccessException
				 * @throws InstantiationException
				 */
				private T deserializeResult(Object result) throws IllegalArgumentException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
					if(result instanceof Map<?, ?>){
						//we have a single serialized object probably of type T
						return serializer.deSerialize((Map<String, Object>) result, clazz);
					}else if(result instanceof Object[]){
						List<Object> list = new ArrayList<Object>();
						for (Object obj : (Object[])result) {
							Object deserializedObj = serializer.deSerialize((Map<String, Object>) obj, clazz);
							list.add(deserializedObj);
						}
						return (T) list;
					}
					
					return null;
				}
			});
		} catch (final XMLRPCFault e) {
			handler.post(new Runnable() {
				public void run() {
					Log.d("Test", "error", e);
					callBack.onFailure(e);
				}
			});
		} catch (final XMLRPCException e) {
			handler.post(new Runnable() {
				public void run() {
//					Throwable couse = e.getCause();
//					if (couse instanceof HttpHostConnectException) {
//						status
//								.setText("Cannot connect to "
//										+ uri.getHost()
//										+ "\nMake sure server.py on your development host is running !!!");
//					} else {
//						status.setText("Error " + e.getMessage());
//					}
					Log.d("Test", "error", e);
					callBack.onFailure(e);
				}
			});
		}
	}
}
