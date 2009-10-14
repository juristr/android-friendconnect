package com.androidmvc.tests;

import junit.framework.TestCase;

import com.androidmvc.controller.ButtonController;
import com.androidmvc.mocks.MockButtonView;
import com.androidmvc.model.ButtonModel;

public class MVCActivityTest extends TestCase {

	private ButtonModel model;
	private ButtonController controller;
	
	public MVCActivityTest() {
		
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		model = new ButtonModel();
		controller = new ButtonController(model);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		
		model = null;
		controller = null;
	}

	public void testButtonIncrement(){
		MockButtonView view = new MockButtonView();
		view.setButtonController(controller);
		controller.registerObserver(view);
		
		int currentIncrement = view.getButtonValue();
		assertEquals(0, currentIncrement);
		
		//do the increment by clicking the button
		view.simulateButtonClick();
		
		int increment = view.getButtonValue();
		assertEquals(1, increment);
	}

	public void testModelIncrement(){
		MockButtonView view = new MockButtonView();
		view.setButtonController(controller);
		controller.registerObserver(view);
		
		int currentIncrement = view.getButtonValue();
		assertEquals(0, currentIncrement);
		
		//increment the model, the button should be incremented too "automagically" through MVC
		this.model.incrementButtonValue(0);
		
		int increment = view.getButtonValue();
		assertEquals(1, increment);
	}
	

}
