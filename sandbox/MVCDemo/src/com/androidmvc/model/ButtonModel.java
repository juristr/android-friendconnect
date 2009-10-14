package com.androidmvc.model;

import java.util.Observable;

public class ButtonModel extends Observable {
	private int[] buttonsCount;
	
	public ButtonModel() {
		this.buttonsCount = new int[3];
	}
	
	public int getButtonState(int buttonId){
		return buttonsCount[buttonId];
	}
	
	public void incrementButtonValue(int buttonId){
		buttonsCount[buttonId]++;
		setChanged();
		notifyObservers();
	}
}
