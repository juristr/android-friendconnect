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

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import com.friendconnect.model.User;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;

public class FriendPositionOverlay extends BasePositionOverlay {
	private final float MAX_DISTANCE = 20000;
	private User friendUser;
	private GeoPoint friendGeoPoint;
	private GeoPoint androidUserGeoPoint;

	public FriendPositionOverlay(User friendUser) {
		super();
		this.friendUser = friendUser;
	}

	public void setPosition(com.friendconnect.model.Location position,
			com.friendconnect.model.Location androidUserPosition) {
		this.friendGeoPoint = new GeoPoint(
				(int) (position.getLatitude() * 1E6), (int) (position
						.getLongitude() * 1E6));

		this.androidUserGeoPoint = new GeoPoint((int) (androidUserPosition
				.getLatitude() * 1E6), (int) (androidUserPosition
				.getLongitude() * 1E6));
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		if (shadow == false) {
			Projection projection = mapView.getProjection();		

			Paint paint = new Paint();
			paint.setARGB(250, 255, 0, 0);
			paint.setAntiAlias(true);
			paint.setFakeBoldText(true);

//			if (friendDist < MAX_DISTANCE) {
				Point androidUserPoint = new Point();
				projection.toPixels(androidUserGeoPoint, androidUserPoint);

				Point friendPoint = new Point();
				projection.toPixels(friendGeoPoint, friendPoint);

				canvas.drawLine(androidUserPoint.x, androidUserPoint.y,
						friendPoint.x, friendPoint.y, paint);

				int markerRadius = 5;
				RectF oval = new RectF(friendPoint.x - markerRadius,
						friendPoint.y - markerRadius, friendPoint.x
								+ markerRadius, friendPoint.y + markerRadius);

				canvas.drawOval(oval, paint);
				
				String textToDisplay = friendUser.toString() + " (" + friendUser.getFormattedDistanceString() + ")";

				float textWidth = paint.measureText(textToDisplay);
				float textHeight = paint.getTextSize();
				
				RectF infoWindowRect = new RectF(friendPoint.x + markerRadius, friendPoint.y - textHeight,friendPoint.x + 14 + textWidth, friendPoint.y + 7);	
				
				canvas.drawRoundRect(infoWindowRect, 5, 5, getInnerPaint());
				canvas.drawRoundRect(infoWindowRect, 5, 5, getBorderPaint());
				
				canvas.drawText(textToDisplay, friendPoint.x + markerRadius + 4,
						friendPoint.y, getTextPaint());
//			}
		}

		super.draw(canvas, mapView, shadow);
	}

	@Override
	public boolean onTap(GeoPoint p, MapView mapView) {
		return false;
	}
}
