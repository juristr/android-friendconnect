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

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class FriendConnectActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ListView listViewFriends = (ListView) findViewById(R.id.listViewFriends);
        
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("username", "Juri username");
        map.put("status", "some status");
        mylist.add(map);
        map.put("username", "Matthias username");
        map.put("status", "some status2");
        mylist.add(map);
        // ...
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, mylist, R.layout.friendlistrowitem,
                    new String[] {"username", "status"}, new int[] {R.id.textViewUsername, R.id.textViewStatus});
        
        listViewFriends.setAdapter(simpleAdapter);
        
        setContentView(R.layout.main);
    }
}