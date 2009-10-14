package com.androidmvc.controller;

import java.util.Observable;

import com.androidmvc.model.ButtonModel;
import com.androidmvc.mvcutils.AbstractController;

public class ButtonController extends AbstractController {
	
	public ButtonController(Observable observable) {
		super();
		registerModel(observable);
	}

	public void incrementButton(int buttonIndex) {
		((ButtonModel)getModel()).incrementButtonValue(buttonIndex);	
	}
	
	
}
