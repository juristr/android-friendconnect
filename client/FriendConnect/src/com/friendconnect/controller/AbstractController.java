package com.friendconnect.controller;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.widget.BaseAdapter;

import com.friendconnect.view.IView;

public abstract class AbstractController<T extends Observable> implements Observer {
	private ArrayList<IView> views;
	protected T model;
	
	public AbstractController(T model) {
		this.views = new ArrayList<IView>();
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
	
	public abstract <T extends BaseAdapter> T getAdapter(Context context);
	
	public void update(Observable observable, Object data) {		
		for (IView view : this.views) {
			view.update(observable, data);
		}
	}
	
}
