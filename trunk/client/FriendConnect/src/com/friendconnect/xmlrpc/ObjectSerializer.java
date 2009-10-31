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
import java.util.HashMap;
import java.util.Map;

/**
 * Class for serializing/deserializing normal POJOs in a XML-RPC understandable
 * format.
 * 
 */
public class ObjectSerializer {

	public ObjectSerializer() {

	}

	/**
	 * Deserializes a given {@link Map<String, Object>} representing a POJO
	 * returned from a XML-RPC call into a POJO instance.
	 * 
	 * @param <T>
	 *            the type of the object to be instantiated
	 * @param map
	 *            of type {@link Map<String, Object>} containing the encoded
	 *            POJO object data
	 * @param clazz
	 *            the Class of the object to instantiate
	 * @return an instance of the deserialized POJO
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
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

	/**
	 * Serializes a given object into a XML-RPC understandable format,
	 * namely a {@link Map<String, Object>}.
	 * @param object
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Map<String, Object> serialize(Object object)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		Map<String, Object> result = new HashMap<String, Object>();

		Method[] methods = object.getClass().getMethods();
		for (Method method : methods) {
			if (isValidGetter(method)) {
				String propertyName = method.getName().replace("get", "");
				Object propertyValue = (Object) method.invoke(object, null);

				// is complex serializable?
				if (isComplexSerializable(method) && propertyValue != null) {
					propertyValue = serialize(propertyValue);
				}

				if (propertyValue != null)
					result.put(propertyName, propertyValue);
			}
		}

		return result;
	}

	private Method getMethodByName(String methodName, Class clazz) {
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (method.getName().equals(methodName))
				return method;
		}

		return null;
	}

	private Class getClassFromAnnotation(Method setter) {
		ComplexSerializableType ann = (ComplexSerializableType) getSerializableAnnotation(setter);
		return ann.clazz();
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

	/**
	 * Checks whether the given method is a valid getter. Valid getters are -
	 * belonging to the implemented model - not JRE native getters like
	 * "getClass" - comply with the Java getter standard (i.e. return type, no
	 * parameters)
	 * 
	 * @param method
	 * @return
	 */
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
