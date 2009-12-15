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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;

import android.text.format.DateFormat;

import com.friendconnect.annotations.ComplexSerializableType;
import com.friendconnect.annotations.NotRecursiveSync;
import com.friendconnect.annotations.NotSerializable;


public class POIAlert extends Observable {
	private String id;
	private String title;
	private int radius;
	private Date expirationDate;
	private boolean activated;
	private Location position;

	public String getId() {
		return this.id;
	}

	public void setId(String value) {
		this.id = value;
	}

	public String getTitle() { 
		return title;
	}

	public void setTitle(String title) {
		String oldValue = this.title;
		this.title = title;
		
		if (oldValue == null || !oldValue.equals(this.title)) {
			setChanged();
			notifyObservers();
		}
	}

	public Integer getRadius() {
		return radius;
	}

	public void setRadius(Integer radius) {
		int oldValue = this.radius;
		this.radius = radius;
		
		if(oldValue != radius){
			setChanged();
			notifyObservers();
		}
	}

	@NotSerializable
	public String getExpirationDateString(){
		SimpleDateFormat format = new SimpleDateFormat("MMM-d-yyyy");
		return format.format(this.expirationDate);
	}
	
	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		Date oldValue = this.expirationDate;
		this.expirationDate = expirationDate;
		
		boolean changed = false;
		if(oldValue == null || expirationDate == null){
			changed = true;
		}
		
		if(!changed && oldValue.compareTo(expirationDate) != 1){
			changed = true;
		}
		
		if(changed){
			setChanged();
			notifyObservers();
		}
	}

	public boolean getActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		boolean oldValue = this.activated;
		this.activated = activated;
		
		if(oldValue != activated){
			setChanged();
			notifyObservers();
		}
	}

	@NotRecursiveSync
	@ComplexSerializableType(clazz = Location.class)
	public Location getPosition() {
		return this.position;
	}

	@NotRecursiveSync
	@ComplexSerializableType(clazz = Location.class)
	public void setPosition(Location location) {
		Location oldLocation = this.position;
		this.position = location;

		boolean fireChanges = false;
		
		if(oldLocation == null && this.position != null){
			fireChanges = true;
		}else if(oldLocation != null && this.position == null){
			fireChanges = true;
		}else if(oldLocation != null && this.position != null){
			if(oldLocation.getLatitude() != this.position.getLatitude() ||
			   oldLocation.getLongitude() != this.position.getLongitude()){
				fireChanges = true;
			}
		}
		
		if (fireChanges) {
			setChanged();
			notifyObservers();
		}
	}

}
