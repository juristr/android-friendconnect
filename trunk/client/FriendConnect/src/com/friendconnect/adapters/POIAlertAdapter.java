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

package com.friendconnect.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.friendconnect.R;
import com.friendconnect.model.POIAlert;

/**
 * Adapter for binding a list of {@link POIAlert} objects
 * to a {@link ListView}
 *
 */
public class POIAlertAdapter extends ArrayAdapter<POIAlert> {
	private int resourceId;
	
	public POIAlertAdapter(Context context, int resourceId, List<POIAlert> poiAlerts) {
		super(context, resourceId, poiAlerts);
		this.resourceId = resourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout poiAlertListView;
		
		POIAlert poiAlert = getItem(position);
		
		if (convertView == null) {
			poiAlertListView = new LinearLayout(getContext());
			LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layoutInflater.inflate(resourceId, poiAlertListView, true);
		} else {
			poiAlertListView = (LinearLayout)convertView;
		}
		
		TextView titleView = (TextView)poiAlertListView.findViewById(R.id.textViewPoiAlertTitle);
		
		titleView.setText(poiAlert.getTitle());
		
		//show correct status image
		ImageView poiAlertStatusImageView = (ImageView)poiAlertListView.findViewById(R.id.imgViewPoiAlertStatus);
		if (poiAlert.getActivated()) {
			poiAlertStatusImageView.setImageResource(R.drawable.flag);
		} else {
			poiAlertStatusImageView.setImageResource(R.drawable.flag_inactive);
		}
		
		return poiAlertListView;
	}

}
