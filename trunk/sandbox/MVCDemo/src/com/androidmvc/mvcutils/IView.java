package com.androidmvc.mvcutils;

import java.util.Observable;

public interface IView {
	public void update(Observable observable, Object data);
}
