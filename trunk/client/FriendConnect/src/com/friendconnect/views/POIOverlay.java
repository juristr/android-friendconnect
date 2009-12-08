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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.RectF;
import com.friendconnect.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class POIOverlay extends Overlay {
	private GeoPoint geoLocation;
	private OnClickListener clickListener;
	private Bitmap flagBitmap;
	private Bitmap flagShadowBitmap;

	public POIOverlay(GeoPoint geoLocation, Context context) {
		this.geoLocation = geoLocation;
		this.flagBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.flag);
		this.flagShadowBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.flag_shadow);
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		Point screenPos = new Point();
		mapView.getProjection().toPixels(geoLocation, screenPos);

		if (!shadow) {
			int xPos = screenPos.x - (flagBitmap.getWidth() / 2) + 5;
			int yPos = screenPos.y - (flagBitmap.getHeight() - 2);
			canvas.drawBitmap(flagBitmap, xPos, yPos, null);
			
//			Paint rectPaint = new Paint();
//			rectPaint.setARGB(225, 75, 75, 75); //gray
//			rectPaint.setAntiAlias(true);
			
//			RectF rect = new RectF();
//			rect.set(-flagBitmap.getWidth()/2,-flagBitmap.getHeight(),flagBitmap.getWidth()/2,0);
//			rect.offset(screenPos.x, screenPos.y);
//			canvas.drawRect(rect, rectPaint);
			
		} else {
			int xPos = screenPos.x-10;
			int yPos = screenPos.y - flagShadowBitmap.getHeight();
			canvas.drawBitmap(flagShadowBitmap, xPos, yPos, null);
		}
	}
	
	@Override
	public boolean onTap(GeoPoint tapPoint, MapView mapView) {
		
		RectF hitTestRect = new RectF();
		
		Point screenCoords = new Point();
		mapView.getProjection().toPixels(geoLocation, screenCoords);
		
		hitTestRect.set(-flagBitmap.getWidth()/2,-flagBitmap.getHeight(),flagBitmap.getWidth()/2,0);
		hitTestRect.offset(screenCoords.x, screenCoords.y);
		
		mapView.getProjection().toPixels(tapPoint, screenCoords);
		if(hitTestRect.contains(screenCoords.x, screenCoords.y)){
			//we have a hit
			notifyClickListener(tapPoint);
		}
		
		return super.onTap(tapPoint, mapView);
	}
	
	public void setOnClickListener(OnClickListener listener){
		this.clickListener = listener;
	}
	
	private void notifyClickListener(GeoPoint point){
		if(this.clickListener != null){
			this.clickListener.onClick(point);
		}
	}
}
