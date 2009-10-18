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
import java.util.List;
import java.util.Observable;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.friendconnect.adapters.PersonAdapter;
import com.friendconnect.model.Friend;
import com.friendconnect.model.Person;
import com.friendconnect.view.IView;

public class FriendConnectActivity extends Activity implements IView {
	
	private ListView listViewFriends;
	private ArrayAdapter<Person> personAdapter;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        this.listViewFriends = (ListView) findViewById(R.id.listViewFriends); 
        
        //TODO dummy dataset. to be replaced with the correct data
        ArrayList<Person> friends = new ArrayList<Person>();
        friends.add(new Friend(1, "Juri", "Strumpflohner", "Hello World!"));
        friends.add(new Friend(2, "Matthias", "Braunhofer", "Hello FriendConnect!"));
        
        this.personAdapter = createAdapter(friends);
        listViewFriends.setAdapter(this.personAdapter);
    }
    
    //TODO extract to a generic interface?? Necessary??
    private PersonAdapter createAdapter(List data){
    	return new PersonAdapter(this, R.layout.friendlistrowitem, data);
    }

	public void update(Observable observable, Object data) {
		//provocate rebinding of the friendlist
		this.personAdapter.notifyDataSetChanged();
	}
}