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

package org.xmlrpc.android;

import android.os.Handler;
import android.util.Log;

public class XMLRPCMethod extends Thread {
	private String method;
	private Object[] params;
	private Handler handler;
	private IXMLRPCCallback callBack;
	private XMLRPCClient client;

	public XMLRPCMethod(XMLRPCClient client, String method, IXMLRPCCallback callBack) {
		this.client = client;
		this.method = method;
		this.callBack = callBack;
		handler = new Handler();
	}
	
	/**
	 * Just intended for testing purposes (synchronous)
	 * @param client
	 * @param method
	 * @param params
	 */
	public XMLRPCMethod(XMLRPCClient client, String method, Object[] params){
		this.client = client;
		this.method = method;
		this.params = params;
	}

	public void call() {
		call(null);
	}

	public void call(Object[] params) {
//		status.setTextColor(0xff80ff80);
//		status.setError(null);
//		status.setText("Calling host " + uri.getHost());
//		tests.setEnabled(false);
		this.params = params;
		start();
	}

	@Override
	public void run() {
		try {
			//invoke the XML-RPC client for doing the actual call
			final Object result = client.callEx(method, params);
			
			handler.post(new Runnable() {
				public void run() {
//					tests.setEnabled(true);
//					status.setText("XML-RPC call took " + (t1 - t0) + "ms");
					callBack.callFinished(result);
				}
			});
		} catch (final XMLRPCFault e) {
			handler.post(new Runnable() {
				public void run() {
//					testResult.setText("");
//					tests.setEnabled(true);
//					status.setTextColor(0xffff8080);
//					status.setError("", errorDrawable);
//					status.setText("Fault message: " + e.getFaultString()
//							+ "\nFault code: " + e.getFaultCode());
					Log.d("Test", "error", e);
					callBack.onError(e);
				}
			});
		} catch (final XMLRPCException e) {
			handler.post(new Runnable() {
				public void run() {
//					testResult.setText("");
//					tests.setEnabled(true);
//					status.setTextColor(0xffff8080);
//					status.setError("", errorDrawable);
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
					callBack.onError(e);
				}
			});
		}
	}
}