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

import java.util.List;

import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.friendconnect.xmlrpc.ComplexSerializableType;
import com.friendconnect.xmlrpc.NotSerializable;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class User {
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String id;
	
	@Persistent
	private String name;
	
	@Persistent
	private String emailAddress;
	
	@Persistent 
	private String website;
	
	@Persistent 
	private String phone;
		
	@Persistent
	private String token;
	
	@Persistent
	private boolean online;
	
	@Persistent
	private String statusMessage;
	
	@Persistent
	@Embedded
	private Location position;
	
	@Persistent(defaultFetchGroup = "true")
	private List<String> friends;
    
	@Persistent(defaultFetchGroup = "true")
    private List<String> pendingFriends;
	
	public User() {	
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getWebsite() {
		return website;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public boolean isOnline() {
		return online;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	@ComplexSerializableType(clazz = Location.class)
	public void setPosition(Location position) {
		this.position = position;
	}

	@ComplexSerializableType(clazz = Location.class)
	public Location getPosition() {
		return position;
	}

	@NotSerializable
	public void setFriends(List<String> friends) {
		this.friends = friends;
	}

	@NotSerializable
	public List<String> getFriends() {
		return friends;
	}
	
	@NotSerializable
	public void setPendingFriends(List<String> pendingFriends) {
		this.pendingFriends = pendingFriends;
	}

	@NotSerializable
	public List<String> getPendingFriends() {
		return pendingFriends;
	}
}