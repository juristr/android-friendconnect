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

import java.util.Date;

import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.FetchGroups;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.friendconnect.xmlrpc.ComplexSerializableType;

/**
 * Represents a POI alert.
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
@FetchGroups(value={@FetchGroup(name=FetchGroupConstants.USER_ALL, members={@Persistent(name="position")}), 
				    @FetchGroup(name=FetchGroupConstants.POIALERT_POSITION, members={@Persistent(name="position")})})
public class POIAlert implements IIdentity {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;
	
	@Persistent
	private String title;
	
	@Persistent
	private int radius;
	
	@Persistent
	private Date expirationDate;
	
	@Persistent
	private boolean activated;
	
	@Persistent
	@Embedded
	private Location position;

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getRadius() {
		return radius;
	}

	public void setRadius(Integer radius) {
		this.radius = radius;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public void setActivated(Boolean activated) {
		this.activated = activated;
	}

	public Boolean getActivated() {
		return activated;
	}

	@ComplexSerializableType(clazz = Location.class)
	public Location getPosition() {
		return this.position;
	}

	@ComplexSerializableType(clazz = Location.class)
	public void setPosition(Location location) {
		this.position = location;
	}
}
