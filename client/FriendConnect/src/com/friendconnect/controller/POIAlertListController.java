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

package com.friendconnect.controller;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.friendconnect.adapters.POIAlertAdapter;
import com.friendconnect.main.IFriendConnectApplication;
import com.friendconnect.model.FriendConnectUser;
import com.friendconnect.model.POIAlert;
import com.friendconnect.model.RPCRemoteMappings;
import com.friendconnect.services.IXMLRPCService;
import com.friendconnect.xmlrpc.IAsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class POIAlertListController extends AbstractController<FriendConnectUser> {
	private int layoutId;
	private IXMLRPCService xmlRPCService;
	
	public POIAlertListController() {
	}
	
	public void retrievePOIAlerts(){
		xmlRPCService.sendRequest(RPCRemoteMappings.RETRIEVEPOIALERTS, null, new IAsyncCallback<List<POIAlert>>() {
			public void onSuccess(List<POIAlert> result) {
				if(result == null){
					onFailure(new Exception("Result was null"));
				}
				
				model.setPoiAlerts(result);
				
				notifyStopProgress();
			}

			public void onFailure(Throwable throwable) {
				Log.e(POIAlertListController.class.getCanonicalName(), "Problem retrieving POI alerts:" + throwable.getMessage());
				notifyStopProgress();
			}
		}, POIAlert.class);
	}
	
	/**
	 * This method will be called by the POIAlertListActivity for removing
	 * a POI alert from the POI alert list.
	 */
	public void removePOIAlert(final String poiAlertId) {
		Object[] params = {poiAlertId};
		xmlRPCService.sendRequest(RPCRemoteMappings.REMOVEPOIALERT, params, new IAsyncCallback<Boolean>() {

			public void onFailure(Throwable throwable) {
				notifyStopProgress();
			}

			public void onSuccess(Boolean result) {
				removePOIAlertFromModel(poiAlertId);
				notifyStopProgress();
			}
		}, Boolean.class);
	}
	
	private void removePOIAlertFromModel(String poiAlertId) {
		List<POIAlert> poiAlerts = model.getPoiAlerts();
		for (int i=0; i < poiAlerts.size(); i++){
			POIAlert poiAlert = poiAlerts.get(i);
			if (poiAlert.getId().equals(poiAlertId)){
				model.removePOIAlert(poiAlert);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public POIAlertAdapter getAdapter(Context context) {
		return new POIAlertAdapter(context, this.layoutId, this.model.getPoiAlerts());
	}
	
	public void setLayoutId(int layoutId){
		this.layoutId = layoutId;
	}
	
	@Inject
	public void setApplication(IFriendConnectApplication application) {
		this.registerModel(application.getApplicationModel());
	}

	@Inject
	public void setXmlRPCService(IXMLRPCService xmlRPCService) {
		this.xmlRPCService = xmlRPCService;
	}
}
