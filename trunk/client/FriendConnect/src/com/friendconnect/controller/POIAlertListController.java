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
import com.friendconnect.xmlrpc.ObjectSerializer;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class POIAlertListController extends AbstractController<FriendConnectUser> {
	private int layoutId;
	private IXMLRPCService xmlRPCService;
	private ObjectSerializer serializer;

	public POIAlertListController() {
	}

	public POIAlert getPoiAlertById(String id) {
		for (POIAlert alert : model.getPoiAlerts()) {
			if (alert.getId().equals(id)) {
				return alert;
			}
		}

		return null;
	}

	public void retrievePOIAlerts() {
		xmlRPCService.sendRequest(RPCRemoteMappings.RETRIEVEPOIALERTS, null,
				new IAsyncCallback<List<POIAlert>>() {
					public void onSuccess(List<POIAlert> result) {
						if (result == null) {
							onFailure(new Exception("Result was null"));
						}

						model.setPoiAlerts(result);

						notifyStopProgress();
					}

					public void onFailure(Throwable throwable) {
						Log.e(POIAlertListController.class.getCanonicalName(),
								"Problem retrieving POI alerts:" + throwable.getMessage());
						notifyStopProgress();
					}
				}, POIAlert.class);
	}

	public void addPOIAlert(final POIAlert alert) {
		try {
			Object serializedAlert = serializer.serialize(alert);

			xmlRPCService.sendRequest(RPCRemoteMappings.ADDPOIALERT,
					new Object[] { serializedAlert }, new IAsyncCallback<String>() {

						public void onSuccess(String result) {
							if (result != null && !result.equals("")) {
								// just add if it was successful
								alert.setId(result);
								model.addPoiAlert(alert);
							} else {
								onFailure(new Exception(alert.getTitle()));
							}
							notifyStopProgress();
						}

						public void onFailure(Throwable throwable) {
							Log.e(POIAlertListController.class.getCanonicalName(),
									"Error adding POI alert: " + throwable.getMessage());
							notifyStopProgress();
						}

					}, Boolean.class);
		} catch (Exception ex) {
			Log.e(POIAlertListController.class.getCanonicalName(), ex.getMessage());
		}
	}

	/**
	 * This method will be called by the POIAlertListActivity for removing a POI
	 * alert from the POI alert list.
	 */
	public void removePOIAlert(final String poiAlertId) {
		Object[] params = { poiAlertId };
		xmlRPCService.sendRequest(RPCRemoteMappings.REMOVEPOIALERT, params,
				new IAsyncCallback<Boolean>() {

					public void onSuccess(Boolean result) {
						if (result) {
							removePOIAlertFromModel(poiAlertId);
						} else {
							onFailure(new Exception("Alert id " + poiAlertId));
						}
						notifyStopProgress();
					}

					public void onFailure(Throwable throwable) {
						Log.e(POIAlertListController.class.getCanonicalName(),
								"Problem when removing alert: " + throwable.getMessage());
						notifyStopProgress();
					}

				}, Boolean.class);
	}

	private void removePOIAlertFromModel(String poiAlertId) {
		List<POIAlert> poiAlerts = model.getPoiAlerts();
		for (int i = 0; i < poiAlerts.size(); i++) {
			POIAlert poiAlert = poiAlerts.get(i);
			if (poiAlert.getId().equals(poiAlertId)) {
				model.removePoiAlert(poiAlert);
				break;
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public POIAlertAdapter getAdapter(Context context) {
		return new POIAlertAdapter(context, this.layoutId, this.model.getPoiAlerts());
	}

	public void setLayoutId(int layoutId) {
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

	@Inject
	public void setSerializer(ObjectSerializer serializer) {
		this.serializer = serializer;
	}
}
