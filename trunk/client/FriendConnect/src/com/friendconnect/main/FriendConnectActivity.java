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

package com.friendconnect.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import android.app.Activity;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.friendconnect.view.IView;

public class FriendConnectActivity extends Activity implements IView {
	
	private ListView listViewFriends;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        this.listViewFriends = (ListView) findViewById(R.id.listViewFriends); 
        
        
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("username", "Juri");
        map.put("status", "Some status...");
        mylist.add(map);
        map = new HashMap<String, String>();
        map.put("username", "Matthias");
        map.put("status", "Some status...");
        mylist.add(map);
        // ...
        
        listViewFriends.setAdapter(getAdapter(mylist));
    }
    
    private BaseAdapter getAdapter(List data){
    	return new SimpleAdapter(this, data, R.layout.friendlistrowitem,
                new String[] {"username", "status"}, new int[] {R.id.textViewUsername, R.id.textViewStatus});
    }

	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub	
	}
}