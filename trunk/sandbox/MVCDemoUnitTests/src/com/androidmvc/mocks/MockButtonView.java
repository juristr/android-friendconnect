package com.androidmvc.mocks;

import java.util.Observable;

import com.androidmvc.controller.ButtonController;
import com.androidmvc.model.ButtonModel;
import com.androidmvc.mvcutils.IView;

public class MockButtonView implements IView {

	private int dummyButtonValue = 0;
	private ButtonController buttonController;
	
	public void setButtonController(ButtonController c){
		this.buttonController = c;
	}
	
	public int getButtonValue(){
		return dummyButtonValue;
	}
	
	public void simulateButtonClick(){
		buttonController.incrementButton(0);
	}
	
	@Override
	public void update(Observable observable, Object data) {
		dummyButtonValue = ((ButtonModel)observable).getButtonState(0);
	}

}
