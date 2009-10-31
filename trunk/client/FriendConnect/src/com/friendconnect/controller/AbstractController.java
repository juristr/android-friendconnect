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

package com.friendconnect.controller;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.widget.BaseAdapter;

import com.friendconnect.activities.IView;

public abstract class AbstractController<T extends Observable> implements Observer {
	private ArrayList<IView> views;
	protected T model;
	
	public AbstractController() {
		this.views = new ArrayList<IView>();
	}
	
	public AbstractController(T model) {
		this();
		this.model = model;
	}
	
	public void registerObserver(IView observer){
		if(observer == null)
			throw new IllegalArgumentException("View cannot be null");
		
		this.views.add(observer);
	}
	
	public void removeObserver(IView observer){
		if(observer == null)
			throw new IllegalArgumentException("View cannot be null");

		this.views.remove(observer);
	}
	
	public void registerModel(T model){
		this.model = model;
		this.model.addObserver(this);
	}
	
	public T getModel(){
		return this.model;
	}
	
	public abstract <X extends BaseAdapter> X getAdapter(Context context);
	
	public void update(Observable observable, Object data) {		
		for (IView view : this.views) {
			view.update(observable, data);
		}
	}
	
}
