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

package com.friendconnect.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

/**
 * Utility class for activities
 */
public class ActivityUtils {
	
	/**
	 * Initializes a confirmation dialog
	 * @param context
	 * @param title
	 * @param message
	 * @return
	 */
	public static AlertDialog.Builder createConfirmationDialog(Context context, String title, String message) {
		AlertDialog.Builder ad = new AlertDialog.Builder(context); 
		ad.setTitle(title);
		ad.setMessage(message); 
		ad.setCancelable(true); 
		return ad;
	}
	
	/**
	 * Initializes a dialog that displays a view
	 * @param activity
	 * @param layoutId
	 * @param titleId
	 * @param iconId
	 * @return
	 */
	public static AlertDialog createViewDialog(Activity activity, int layoutId, int titleId, int iconId) {
		LayoutInflater li = LayoutInflater.from(activity);
		View view = li.inflate(layoutId, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		AlertDialog dialog = builder.create();
		dialog.setTitle(titleId);
		dialog.setView(view);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setIcon(iconId);
		return dialog;
	}
	
	/**
	 * Shows a standard toast
	 * @param context
	 * @param textId
	 * @param duration
	 */
	public static void showToast(Context context, int textId, int duration) {
		Toast.makeText(context, textId, duration).show();
	}
	
	public static void showToast(Context context, String text, int duration){
		Toast.makeText(context, text, duration).show();
	}
}
