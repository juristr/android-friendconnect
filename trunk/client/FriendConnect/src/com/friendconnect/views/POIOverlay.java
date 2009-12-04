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

package com.friendconnect.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;

import com.friendconnect.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class POIOverlay extends Overlay {
	private GeoPoint geoLocation;

	public POIOverlay(GeoPoint geoLocation) {
		this.geoLocation = geoLocation;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		Point screenPos = new Point();
		mapView.getProjection().toPixels(geoLocation, screenPos);
		
		if (!shadow) {
			Bitmap bmp = BitmapFactory.decodeResource(mapView.getResources(), R.drawable.flag);
			canvas.drawBitmap(bmp, screenPos.x, screenPos.y-bmp.getHeight(), null);
		}else{
			Bitmap bmp = BitmapFactory.decodeResource(mapView.getResources(), R.drawable.flag_shadow);
			canvas.drawBitmap(bmp, screenPos.x, screenPos.y-bmp.getHeight(), null);
		}
	}
}
