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

package com.friendconnect.xmlrpc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ObjectDeserializer<T> {
	private Class<T> clazz;

	public ObjectDeserializer(Class<T> clazz) {
		this.clazz = clazz;
	}

	public T deSerialize(Map<String, Object> map)
			throws NoSuchMethodException, InvocationTargetException,
			IllegalArgumentException, IllegalAccessException {
		T instance = createInstance();

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			Method setter = this.clazz.getMethod("set"
					+ correctFieldName(entry.getKey()), entry.getValue()
					.getClass());
			setter.invoke(instance, entry.getValue());
		}

		return instance;
	}
	
	public Map<String, Object> serialize(T object){
		Map<String, Object> result = new HashMap<String, Object>();
		
		//TODO deserialize into the HashMap
		
		return result;
	}

	private String correctFieldName(String fieldName) {
		return fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1, fieldName.length());
	}

	private T createInstance() {
		try {
			return this.clazz.newInstance();
		} catch (Exception ex) {
			// TODO dangerous
		}

		return null;
	}
}
