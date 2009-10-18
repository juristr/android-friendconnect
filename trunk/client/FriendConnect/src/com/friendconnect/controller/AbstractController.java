package com.friendconnect.controller;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import com.friendconnect.view.IView;

public abstract class AbstractController implements Observer {
	private ArrayList<IView> views;
	private Observable model;
	
	public AbstractController() {
		this.views = new ArrayList<IView>();
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
	
	public void registerModel(Observable model){
		this.model = model;
		this.model.addObserver(this);
	}
	
	public Observable getModel(){
		return this.model;
	}
	
	
	public void update(Observable observable, Object data) {		
		for (IView view : this.views) {
			view.update(observable, data);
		}
	}
	
}
