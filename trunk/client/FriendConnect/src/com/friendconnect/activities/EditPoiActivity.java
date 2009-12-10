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

package com.friendconnect.activities;

import java.util.Calendar;
import java.util.Date;
import java.util.Observable;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.friendconnect.R;
import com.friendconnect.controller.POIAlertListController;
import com.friendconnect.main.IoC;
import com.friendconnect.model.Location;
import com.friendconnect.model.POIAlert;

public class EditPoiActivity extends Activity implements IView {
	public static final String BUNDLE_GEO_LAT = "geoLatData";
	public static final String BUNDLE_GEO_LNG = "getLngData";
	private static final String BUNDLE_ALERT_ID = "alertId";
	private POIAlertListController controller;
	private POIAlert poiAlert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editpoiview);

		this.controller = IoC.getInstance(POIAlertListController.class);
		this.controller.registerView(this);

		Bundle retrievedData = this.getIntent().getExtras();
		if (retrievedData != null) {
			String poiAlertId = retrievedData.getString(BUNDLE_ALERT_ID);
			if (poiAlertId != null) {
				this.poiAlert = controller.getPoiAlertById(poiAlertId);
			}

			if (this.poiAlert == null) {
				this.poiAlert = new POIAlert();
				int lat = retrievedData.getInt(BUNDLE_GEO_LAT);
				int lng = retrievedData.getInt(BUNDLE_GEO_LNG);

				if (lat > 0 && lng > 0) {
					// if we have already
					TextView textViewAddress = (TextView) findViewById(R.id.textViewAddress);
					EditText editTextAddress = (EditText) findViewById(R.id.editTextAddress);
					textViewAddress.setVisibility(View.INVISIBLE);
					editTextAddress.setVisibility(View.INVISIBLE);

					Location location = new Location();
					location.setLatitude((double) (lat / 1E6));
					location.setLongitude((double) (lng / 1E6));
					poiAlert.setPosition(location);
				}
			} else {
				// bind the data for editing
				databindPoiAlert();
			}
		}

		Button saveButton = (Button) findViewById(R.id.buttonSavePoi);
		saveButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				unbindPoiAlert();

				controller.addPOIAlert(poiAlert);

				setResult(Activity.RESULT_OK, null);
				finish();
			}
		});
	}

	private void databindPoiAlert() {
		EditText editTextTitle = (EditText) findViewById(R.id.editTextTitle);
		EditText editTextRadius = (EditText) findViewById(R.id.editTextRadius);
		CheckBox checkBoxActivated = (CheckBox) findViewById(R.id.checkBoxPoiActivated);
		DatePicker expirationDatePicker = (DatePicker) findViewById(R.id.datePickerExpirationDatePicker);
		EditText editTextAddress = (EditText) findViewById(R.id.editTextAddress);

		editTextTitle.setText(poiAlert.getTitle());
		editTextRadius.setText(poiAlert.getRadius());
		checkBoxActivated.setChecked(poiAlert.getActivated());
		if (poiAlert.getExpirationDate() != null)
			expirationDatePicker.updateDate(poiAlert.getExpirationDate().getYear(), poiAlert
					.getExpirationDate().getMonth(), poiAlert.getExpirationDate().getDay());
	}

	private void unbindPoiAlert() {
		EditText editTextTitle = (EditText) findViewById(R.id.editTextTitle);
		EditText editTextRadius = (EditText) findViewById(R.id.editTextRadius);
		CheckBox checkBoxActivated = (CheckBox) findViewById(R.id.checkBoxPoiActivated);
		DatePicker expirationDatePicker = (DatePicker) findViewById(R.id.datePickerExpirationDatePicker);
		EditText editTextAddress = (EditText) findViewById(R.id.editTextAddress);

		poiAlert.setTitle(editTextTitle.getText().toString());
		try {
			poiAlert.setRadius(Integer.parseInt(editTextRadius.getText().toString()));
		} catch (NumberFormatException ex) {
			Log.w(EditPoiActivity.class.getCanonicalName(), "Error parsing radius: "
					+ editTextRadius.getText());
		}
		poiAlert.setActivated(checkBoxActivated.isChecked());		
		poiAlert.setExpirationDate(new Date(expirationDatePicker.getYear(), expirationDatePicker.getMonth(), expirationDatePicker.getDayOfMonth()));		
	}

	public void showMessage(int messageId) {
		// TODO Auto-generated method stub

	}

	public void stopProgress() {
		// TODO Auto-generated method stub

	}

	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub

	}

}
