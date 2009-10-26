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

package com.friendconnect.dto;

import java.util.HashMap;
import java.util.Map;

import org.xmlrpc.android.XMLRPCSerializable;

public class RequestDTO<T extends XMLRPCSerializable> implements XMLRPCSerializable {
	//header info
	private String username;
	private String token;
	
	//server-side method to invoke
	private String method;
	
	//content
	private T value;
	
	public RequestDTO(T value) {
		this.value = value;
	}

	public Object getSerializable() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", this.username);
		map.put("token", this.token);
		map.put("value", this.value.getSerializable());
		return map;
	}
}
