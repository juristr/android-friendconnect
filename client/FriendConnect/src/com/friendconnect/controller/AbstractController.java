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

/**
 * Abstracts the common operations done by the Controller 
 *
 * @param <T> the model
 */
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
	
	/**
	 * Register views
	 * @param view the {@link IView}
	 */
	public void registerView(IView view){
		if(view == null)
			throw new IllegalArgumentException("View cannot be null");
		
		this.views.add(view);
	}
	
	/**
	 * Removes a registered view
	 * @param view the {@link IView}
	 */
	public void removeView(IView view){
		if(view == null)
			throw new IllegalArgumentException("View cannot be null");

		this.views.remove(view);
	}
	
	/**
	 * Registering a model
	 * @param model
	 */
	public void registerModel(T model){
		this.model = model;
		this.model.addObserver(this);
	}
	
	public T getModel(){
		return this.model;
	}
	
	/**
	 * The main update method which notifies all registered views about
	 * the model changes
	 */
	public void update(Observable observable, Object data) {		
		for (IView view : this.views) {
			view.update(observable, data);
		}
	}
	
	/**
	 * Notifies views to stop their progress indications
	 */
	protected void notifyStopProgress(){
		for (IView view : this.views) {
			view.stopProgress();
		}
	}
	
	/**
	 * Notifies views to show a message. This works since always just one view
	 * will be registered at the same time, since they register during the onCreate and
	 * de-register when they're dismissed.
	 * @param messageId
	 */
	public void notifyShowMessage(int messageId){
		for (IView view : this.views) {
			view.showMessage(messageId);
		}
	}
	
	public abstract <X extends BaseAdapter> X getAdapter(Context context);
}
