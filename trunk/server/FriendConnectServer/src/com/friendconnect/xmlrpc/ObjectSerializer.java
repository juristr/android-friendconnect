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

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.friendconnect.model.User;


public class ObjectSerializer {
	// private Class<T> clazz;

	public ObjectSerializer() {
		// this.clazz = clazz;
	}

	@SuppressWarnings("unchecked")
	public <T> T deSerialize(Map<String, Object> map, Class clazz)
			throws NoSuchMethodException, InvocationTargetException,
			IllegalArgumentException, IllegalAccessException {
		T instance = (T) createInstance(clazz);

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String propertyName = correctFieldName(entry.getKey());
			Object propertyValue = entry.getValue();
			
			Method setter = getMethodByName("set" + propertyName, clazz);

			if (propertyValue instanceof HashMap) {
				Class subClazz = getClassFromAnnotation(setter);
				propertyValue = deSerialize(
						(Map<String, Object>) propertyValue, subClazz);
			}

			setter.invoke(instance, propertyValue);
		}

		return instance;
	}

	private Method getMethodByName(String methodName, Class clazz) {
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if(method.getName().equals(methodName))
				return method;
		}
		
		return null;
	}

	public Map<String, Object> serialize(Object object)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		Map<String, Object> result = new HashMap<String, Object>();

		Method[] methods = object.getClass().getMethods();
		for (Method method : methods) {
			if (isValidGetter(method)) {
				String propertyName = method.getName().replace("get", "");
				Object propertyValue = (Object) method.invoke(object, null);

				// is serializable?
				if(isSerializable(method)) {
					
					if (propertyValue instanceof List<?>) {
						List<Map<String, Object>> serializedList = new ArrayList<Map<String, Object>>();
						for (Object o : (List<?>) propertyValue) {
							Map<String, Object> temp = serialize(o);
							serializedList.add(temp);
						}
						propertyValue = serializedList;
					}

					// is complex serializable?
					else if (isComplexSerializable(method) && propertyValue != null) {
						
						propertyValue = serialize(propertyValue);
					}

					if (propertyValue != null)
						result.put(propertyName, propertyValue);
				}
			}
		}

		return result;
	}

	private Class getClassFromAnnotation(Method setter) {
		ComplexSerializableType ann = (ComplexSerializableType) getSerializableAnnotation(setter);
		return ann.clazz();
	}

	private boolean isSerializable(Method method) {
		return getNotSerializableAnnotation(method) == null;
	}
	
	private Annotation getNotSerializableAnnotation(Method method) {
		Annotation[] annotations = method.getAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation instanceof NotSerializable)
				return annotation;
		}
		return null;
	}
	
	private boolean isComplexSerializable(Method method) {
		return getSerializableAnnotation(method) != null;
	}

	private Annotation getSerializableAnnotation(Method method) {
		Annotation[] annotations = method.getAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation instanceof ComplexSerializableType)
				return annotation;
		}

		return null;
	}

	private boolean isValidGetter(Method method) {
		if (method.getName().contains("Class"))
			return false;
		if (!method.getName().startsWith("get"))
			return false;
		if (method.getParameterTypes().length != 0)
			return false;
		if (void.class.equals(method.getReturnType()))
			return false;
		return true;
	}

	private String correctFieldName(String fieldName) {
		return fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1, fieldName.length());
	}

	private Object createInstance(Class clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception ex) {
			// TODO dangerous
		}

		return null;
	}
}
