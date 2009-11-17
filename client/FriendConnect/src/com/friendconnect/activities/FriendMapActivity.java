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

import java.util.Observable;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.friendconnect.R;
import com.friendconnect.controller.LocationController;
import com.friendconnect.main.IoC;
import com.friendconnect.model.FriendConnectUser;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class FriendMapActivity extends MapActivity implements IView {
	private MapView mapView;
	private MapController mapController;
	private LocationController controller;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friendmapview);
		this.mapView = (MapView)findViewById(R.id.map_view);
		this.mapController = mapView.getController();
		
		LinearLayout zoomLayout = (LinearLayout)findViewById(R.id.zoom);  
        View zoomView = mapView.getZoomControls(); 
 
        zoomLayout.addView(zoomView, 
            new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, 
                LayoutParams.WRAP_CONTENT)); 
        mapView.displayZoomControls(true);
		
		
		this.controller = IoC.getInstance(LocationController.class);
		this.controller.registerView(this);
		this.controller.startLocationTracking();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){ 
        switch (keyCode) 
        {
            case KeyEvent.KEYCODE_3:
                mapController.zoomIn();
                break;
            case KeyEvent.KEYCODE_1:
            	mapController.zoomOut();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }    
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void navigateToPoint(double lat, double lng){
		GeoPoint point = new GeoPoint((int)(lat * 1E6),(int) (lng * 1E6));
		mapController.animateTo(point);
		mapView.invalidate();
	}
	
	public void onProgressChanged(String message) {
		// TODO Auto-generated method stub
	}

	public void stopProgess() {
		// TODO Auto-generated method stub
		
	}

	public void update(Observable observable, Object data) {
		FriendConnectUser user = (FriendConnectUser)observable;
		if(user.getPosition().getLatitude() != 0 && user.getPosition().getLongitude() != 0)
			navigateToPoint(user.getPosition().getLatitude(), user.getPosition().getLongitude());
	}

}
