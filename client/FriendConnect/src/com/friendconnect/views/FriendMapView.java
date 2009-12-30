package com.friendconnect.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.google.android.maps.MapView;

public class FriendMapView extends MapView {
	private long downTime = -1;
	private long upTime = -1;
	private long lastTouchTime = -1;
	
	private float x;
	private float y;
	private OnDoubleClickListener onDoubleClickListener;
	private OnLongTouchListener onLongTouchListener;

	public FriendMapView(Context context, AttributeSet attrs) {
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
		
		if (upTime - downTime > 1000 && doEventCoordinatesMatchInitialLocation(ev.getX(), ev.getY())){
			Log.i("FriendMapView.onTouchEvent", "ok, notify the onLongTouchListener!!");
			notifyOnLongTouchListener(ev);
			return true;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * This is needed since a thumb on a real device may be thicker and therefore
	 * the x,y coordinates of the touch may vary slightly (while it will work with a
	 * mouse pointer on the emulator).
	 * @param evX
	 * @param evY
	 * @return
	 */
	private boolean doEventCoordinatesMatchInitialLocation(float evX, float evY){
		int toleranz = 10;
		
		int preparedEvX = (int)evX;
		int preparedEvY = (int)evY;
		int preparedX = (int)x;
		int preparedY = (int)y;
		
		boolean xMatches = (preparedX <= (preparedEvX + toleranz)) && (preparedX >= (preparedEvX - toleranz));
		boolean yMatches = (preparedY <= (preparedEvY + toleranz)) && (preparedY >= (preparedEvY - toleranz));
		
		return xMatches && yMatches;
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
