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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.location.Location;

import com.friendconnect.model.User;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class FriendPositionOverlay extends Overlay {
	private final float MAX_DISTANCE = 20000;
	private Context context;
	private User friendUser;
	private Location friendPosition;
	private GeoPoint friendGeoPoint;
	private Location androidUserPosition;
	private GeoPoint androidUserGeoPoint;

	public FriendPositionOverlay(User friendUser, Context context) {
		super();
		this.friendUser = friendUser;
		this.context = context;
	}

	public void setPosition(com.friendconnect.model.Location position,
			com.friendconnect.model.Location androidUserPosition) {

		this.friendPosition = position.convertToAndroidLocation();
		this.friendGeoPoint = new GeoPoint(
				(int) (position.getLatitude() * 1E6), (int) (position
						.getLongitude() * 1E6));

		this.androidUserPosition = androidUserPosition
				.convertToAndroidLocation();
		this.androidUserGeoPoint = new GeoPoint((int) (androidUserPosition
				.getLatitude() * 1E6), (int) (androidUserPosition
				.getLongitude() * 1E6));
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		if (shadow == false) {
			Projection projection = mapView.getProjection();
//			float friendDist = androidUserPosition.distanceTo(friendPosition);		

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
				
				String textToDisplay = friendUser.toString() + "(" + friendUser.getDistanceToFriendConnectUser() + ")";

				float textWidth = paint.measureText(textToDisplay);
				float textHeight = paint.getTextSize();
				RectF textRect = new RectF(friendPoint.x + markerRadius,
						friendPoint.y - textHeight, friendPoint.x
								+ markerRadius + 8 + textWidth,
						friendPoint.y + 4);
				canvas.drawText(textToDisplay, friendPoint.x + markerRadius + 4,
						friendPoint.y, paint);
//			}
		}

		super.draw(canvas, mapView, shadow);
	}

	@Override
	public boolean onTap(GeoPoint p, MapView mapView) {
		return false;
	}
}
