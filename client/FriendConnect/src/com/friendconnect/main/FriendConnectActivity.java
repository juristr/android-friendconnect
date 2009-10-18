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

import java.util.Observable;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.friendconnect.controller.FriendListController;
import com.friendconnect.model.Friend;
import com.friendconnect.model.User;
import com.friendconnect.view.IView;

public class FriendConnectActivity extends Activity implements IView {
	static final private int MVC_TEST = Menu.FIRST;
	
	private FriendListController controller;
	private ListView listViewFriends;
	private BaseAdapter adapter;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        this.listViewFriends = (ListView) findViewById(R.id.listViewFriends); 
        
        //TODO just dummy instantiation here
        this.controller = new FriendListController(new User(), R.layout.friendlistrowitem);
        this.controller.registerObserver(this);
        
        this.adapter = controller.getAdapter(this);
        listViewFriends.setAdapter(this.adapter);
    }

	public void update(Observable observable, Object data) {
		//provocate rebinding of the friendlist
		this.adapter.notifyDataSetChanged();
	}
	
    /** Triggered the first time activity’s menu is displayed. */
    @Override 
    public boolean onCreateOptionsMenu(Menu menu) { 
    	super.onCreateOptionsMenu(menu); 
    	// Create and add new menu items. 
    	MenuItem itemAdd = menu.add(0, MVC_TEST, Menu.NONE, "MVC test"); 
    	 
    	// Allocate shortcuts to each of them.
    	itemAdd.setShortcut('0', 'a');  
    	return true; 
    }
    
    /** Handles activity’s menu item selections */
    @Override 
    public boolean onOptionsItemSelected(MenuItem item) { 
    	super.onOptionsItemSelected(item); 
//	    int index = this.listViewFriends.getSelectedItemPosition(); 
	    switch (item.getItemId()) { 
	    	case (MVC_TEST): { 
	    		//simulate some common operations to test the MVC binding
	    		simulateMVCBindings();
	    		return true; 
	        }  
	    } 
	    return false;
    }

    /*
     * These operations could potentially take place in another
     * class having the same model instance
     */
	private void simulateMVCBindings() {
		this.controller.addFriend(new Friend(3, "Test friend", "some", "Hi there, I'm new here!"));
		this.controller.simulateFriendsStatusMessageChange("FriendConnect rocks!!");		
	}
}