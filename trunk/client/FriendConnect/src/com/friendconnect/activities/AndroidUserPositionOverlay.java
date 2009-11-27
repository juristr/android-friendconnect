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

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class AndroidUserPositionOverlay extends Overlay {
	// private Context context;
	private GeoPoint androidUserGeoPoint;

	public AndroidUserPositionOverlay() {
		super();
		// this.context = context;
	}

	public void setPosition(com.friendconnect.model.Location position) {
		this.androidUserGeoPoint = new GeoPoint(
				(int) (position.getLatitude() * 1E6), (int) (position
						.getLongitude() * 1E6));
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		Projection projection = mapView.getProjection();
		if (shadow == false) {
			Paint paint = new Paint();
			paint.setARGB(250, 0, 0, 255);
			paint.setAntiAlias(true);
			paint.setFakeBoldText(true);

			Point androidUserPoint = new Point();
			projection.toPixels(androidUserGeoPoint, androidUserPoint);

			int markerRadius = 5;
			RectF oval = new RectF(androidUserPoint.x - markerRadius,
					androidUserPoint.y - markerRadius, androidUserPoint.x
							+ markerRadius, androidUserPoint.y + markerRadius);

			canvas.drawOval(oval, paint);

			String textToDisplay = "(That's you)";

			float textWidth = paint.measureText(textToDisplay);
			float textHeight = paint.getTextSize();
			RectF textRect = new RectF(androidUserPoint.x + markerRadius,
					androidUserPoint.y - textHeight, androidUserPoint.x
							+ markerRadius + 8 + textWidth,
					androidUserPoint.y + 4);
			canvas.drawText(textToDisplay, androidUserPoint.x + markerRadius
					+ 4, androidUserPoint.y, paint);
		}

		super.draw(canvas, mapView, shadow);
	}

	@Override
	public boolean onTap(GeoPoint p, MapView mapView) {
		return false;
	}
}
