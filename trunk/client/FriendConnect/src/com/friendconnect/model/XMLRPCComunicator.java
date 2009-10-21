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

package com.friendconnect.model;

import java.net.URI;

import org.xmlrpc.android.IXMLRPCCallback;
import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCMethod;

public class XMLRPCComunicator {
	private URI baseURI = URI.create("http://10.7.199.30:8080/xmlrpc");
	private XMLRPCClient client;
	
	public XMLRPCComunicator() {
		this.client = new XMLRPCClient(baseURI);
	}
	
	public void sendXMLRPC(Object[] params, IXMLRPCCallback callback){
		XMLRPCMethod method = new XMLRPCMethod(client, "XMLRPCGateway.getSimpleRCPTestResult", callback);
        method.call(params);
	}

}
