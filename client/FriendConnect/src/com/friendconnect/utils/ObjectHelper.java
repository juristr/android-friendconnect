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

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.friendconnect.annotations.ComplexSerializableType;
import com.friendconnect.annotations.NotRecursiveSync;

/**
 * Compares and synchronizes two objects
 * 
 */
public class ObjectHelper {

	public ObjectHelper() {

	}

	/**
	 * synchronizes two object graphs of the same type, i.e. - Friend.class -
	 * String name - int id - Location.class - double latitude - double
	 * longitude
	 * 
	 * @param original
	 *            the original object
	 * @param updated
	 *            the new updated object
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public void syncObjectGraph(Object original, Object updated)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, SecurityException, NoSuchMethodException {
		// get all getters
		List<Method> getters = getGetterMethods(original);

		// start synching
		for (Method method : getters) {
			Object valueOrig = method.invoke(original, null);
			Object valueNew = method.invoke(updated, null);

			if (valueOrig == null || valueNew == null) {
				setValue(method, original, valueNew);
			} else {
				if (isComplexObjectType(method.getReturnType())) {
					// complex object
					if (valueOrig == null || valueNew == null || isNotRecursiveSynchable(method)) {
						setValue(method, original, valueNew);
					} else {						
						syncObjectGraph(valueOrig, valueNew);
					}
				} else {
					boolean equal = (((Comparable) valueOrig)
							.compareTo((Comparable) valueNew) == 0);

					if (!equal) {
						// have to sync: retrieve appropriate setter
						setValue(method, original, valueNew);
					}
				}
			}
		}
	}

	private void setValue(Method method, Object original, Object valueNew)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		String setterName = "set" + method.getName().replace("get", "");
		Method setter = original.getClass().getMethod(setterName,
				method.getReturnType());
		setter.invoke(original, valueNew);
	}

	private boolean traverseRecursively(Method getter, Object original) throws SecurityException, NoSuchMethodException{
		String setterName = "set" + getter.getName().replace("get", "");
		Method setter = original.getClass().getMethod(setterName,
				getter.getReturnType());
		
		return isNotRecursiveSynchable(setter);
	}
	
	private boolean isNotRecursiveSynchable(Method method) {
		return getNotRecursiveSyncAnnotation(method) != null;
	}

	private Annotation getNotRecursiveSyncAnnotation(Method method) {
		Annotation[] annotations = method.getAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation instanceof NotRecursiveSync)
				return annotation;
		}

		return null;
	}
	
	/**
	 * Determines whether an object is of a complex type. This is done with a
	 * "hack" by verifying whether it is inside the package "com.friendconnect"
	 * and not a JRE class
	 * 
	 * @param clazz
	 *            the object's class
	 * @return true if the object is a complex type, false otherwise
	 */
	private boolean isComplexObjectType(Class<?> clazz) {
		Package pkg = clazz.getPackage();
		if (pkg == null)
			return false;
		else
			return pkg.getName().contains("com.friendconnect");
	}

	/**
	 * Retrieves a list of getter methods for the given object
	 * 
	 * @param object
	 *            the object from which to fetch the getters
	 * @return {@link List<Method>} of getters
	 */
	private List<Method> getGetterMethods(Object object) {
		List<Method> getters = new ArrayList<Method>();
		for (Method method : object.getClass().getMethods()) {
			if (isValidGetter(method))
				getters.add(method);
		}

		return getters;
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
}
