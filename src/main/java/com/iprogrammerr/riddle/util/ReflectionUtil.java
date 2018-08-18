package com.iprogrammerr.riddle.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtil {

    private static final String GETTER_PREFIX = "get";

    public static <T> List<Object> getAllAccessibleFields(Class<T> clazz, T object) {
	List<Object> accessibleFields = new ArrayList<>();
	Field[] fields = clazz.getFields();
	if (fields == null || fields.length < 1) {
	    return accessibleFields;
	}
	for (Field field : fields) {
	    try {
		if (field.isAccessible()) {
		    accessibleFields.add(field.get(object));
		}
	    } catch (IllegalArgumentException | IllegalAccessException exception) {
		exception.printStackTrace();
	    }
	}
	List<Method> getters = getAllGetters(clazz);
	if (getters.isEmpty()) {
	    return accessibleFields;
	}
	for (Method getter : getters) {
	    try {
		accessibleFields.add(getter.invoke(object));
	    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
		exception.printStackTrace();
	    }
	}
	return accessibleFields;
    }

    public static List<Method> getAllGetters(Class clazz) {
	List<Method> getters = new ArrayList<>();
	Method[] methods = clazz.getMethods();
	if (methods == null || methods.length < 1) {
	    return getters;
	}
	for (Method method : methods) {
	    if (Modifier.isPublic(method.getModifiers()) && method.getName().startsWith(GETTER_PREFIX)) {
		getters.add(method);
	    }
	}
	return getters;
    }

}
