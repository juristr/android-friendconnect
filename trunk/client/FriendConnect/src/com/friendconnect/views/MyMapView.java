package com.friendconnect.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.android.maps.MapView;

public class MyMapView extends MapView {
	private long downTime = -1;
	private long upTime = -1;
	private long lastTouchTime = -1;
	
	private float x;
	private float y;
	private OnDoubleClickListener onDoubleClickListener;
	private OnLongTouchListener onLongTouchListener;

	public MyMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setOnDoubleClickListener(OnDoubleClickListener onDoubleClickListener) {
		this.onDoubleClickListener = onDoubleClickListener;
	}
	
	public void setOnLongTouchListener(OnLongTouchListener onLongTouchListener) {
		this.onLongTouchListener = onLongTouchListener;
	}
	
	private void notifyOnDoubleClickListener(MotionEvent ev) {
		if (onDoubleClickListener != null) {
			onDoubleClickListener.onDoubleClick(ev);
		}
	}
	
	private void notifyOnLongTouchListener(MotionEvent ev) {
		if (onLongTouchListener != null) {
			onLongTouchListener.onLongTouch(ev);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			downTime = ev.getEventTime();
			x = ev.getX();
			y = ev.getY();
		} else if (ev.getAction() == MotionEvent.ACTION_UP) {
			upTime = ev.getEventTime();
		}
		
		if (upTime - downTime > 1000 && ev.getX() == x && ev.getY() == y) {
			notifyOnLongTouchListener(ev);
			return true;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			long thisTime = System.currentTimeMillis();
			if (thisTime - lastTouchTime < 250) {

				// Double tap
				notifyOnDoubleClickListener(ev);
				lastTouchTime = -1;
				return true;

			} else {

				// Too slow :)
				lastTouchTime = thisTime;
			}
		}
		return super.onInterceptTouchEvent(ev);
	}
}
