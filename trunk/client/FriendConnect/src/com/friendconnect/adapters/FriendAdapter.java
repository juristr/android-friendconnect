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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.friendconnect.activities.R;
import com.friendconnect.model.Friend;
import com.friendconnect.model.Person;

/**
 * Adapter for binding a list of {@link Person} objects
 * to a {@link ListView}
 *
 */
public class FriendAdapter extends ArrayAdapter<Friend> {
	private int resourceId;
	
	public FriendAdapter(Context context, int resourceId,
			List<Friend> persons) {
		super(context, resourceId, persons);
		this.resourceId = resourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout friendListView;
		
		Friend person = getItem(position);
		
		//TODO check the following lines
		if(convertView == null){
			friendListView = new LinearLayout(getContext());
			LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layoutInflater.inflate(resourceId, friendListView, true);
		}else{
			friendListView = (LinearLayout)convertView;
		}
		
		TextView nicknameView = (TextView)friendListView.findViewById(R.id.textViewUsername);
		TextView statusView = (TextView)friendListView.findViewById(R.id.textViewStatus);
		
		nicknameView.setText(person.getFirstname());
		statusView.setText(person.getStatusMessage());
		
		return friendListView;
	}

}