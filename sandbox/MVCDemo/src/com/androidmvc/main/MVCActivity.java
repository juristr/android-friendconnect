package com.androidmvc.main;

import java.util.Observable;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.androidmvc.controller.ButtonController;
import com.androidmvc.model.ButtonModel;
import com.androidmvc.mvcutils.IView;

public class MVCActivity extends Activity implements IView, OnClickListener {

	private ButtonController buttonController;
	private Button[] buttons;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		initialize();
	}

	private void initialize() {
		ButtonModel model = new ButtonModel(); // may be initialized somewhere
		// else and afterwards
		// "injected"
		this.buttonController = new ButtonController(model);

		this.buttons = new Button[3];

		this.buttons[0] = (Button) findViewById(R.id.button1);
		this.buttons[1] = (Button) findViewById(R.id.button2);
		this.buttons[2] = (Button) findViewById(R.id.button3);

		for (Button button : buttons) {
			button.setOnClickListener(this);
		}
	}

	@Override
	public void update(Observable observable, Object data) {
		for (int i = 0; i < buttons.length; i++) {
			Button button = buttons[i];
			button.setText("" + ((ButtonModel) observable).getButtonState(i));
		}
	}

	@Override
	public void onClick(View v) {
		Button clickedButton = (Button) v;

		int buttonIndex = getButtonIndex(clickedButton);
		if (buttonIndex >= 0) {
			buttonController.incrementButton(buttonIndex);
		}
	}

	private int getButtonIndex(Button button) {
		int index = -1;

		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i] == button) {
				index = i;
			}
		}

		return index;

	}
}