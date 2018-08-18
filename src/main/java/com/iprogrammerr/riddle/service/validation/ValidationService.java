package com.iprogrammerr.riddle.service.validation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.iprogrammerr.riddle.entity.User;
import com.iprogrammerr.riddle.exception.validation.InvalidItemException;
import com.iprogrammerr.riddle.util.StringUtil;

public class ValidationService {

    private static final String FIELD_MUST_HAVE_AT_LEAST_X_CHARACTERS_TEMPLATE = "%s must have at least %d characters.";
    private static final String FIELD_MUST_HAVE_AT_LEAST_X_CHARACTERS_AND_CONTAINS_X_CHARACTER_TEMPLATE = "%s must have at least %d characters and contains a '%c'";

    public void validateUser(User user) {
	if (StringUtil.isNullOrShorterThan(user.getPassword(), 6) || !user.getEmail().contains("@")) {
	    throw new InvalidItemException(String
		    .format(FIELD_MUST_HAVE_AT_LEAST_X_CHARACTERS_AND_CONTAINS_X_CHARACTER_TEMPLATE, "Email", 6, '@'));
	}
	if (StringUtil.isNullOrShorterThan(user.getName(), 3)) {
	    throw new InvalidItemException(String.format(FIELD_MUST_HAVE_AT_LEAST_X_CHARACTERS_TEMPLATE, "Name", 6));
	}
	if (StringUtil.isNullOrShorterThan(user.getPassword(), 6)) {
	    throw new InvalidItemException(
		    String.format(FIELD_MUST_HAVE_AT_LEAST_X_CHARACTERS_TEMPLATE, "Password", 6));
	}
    }

    public <T> void validateNotNullFieldsRule(Class<T> clazz, T object) {
	Field[] fields = clazz.getFields();
	Method[] methods = clazz.getMethods();
	if (fields != null) {
	    for (Field field : fields) {
		try {
		    if (field.isAccessible() && field.get(object) == null) {
			throw new InvalidItemException(field.getName() + " is null.");
		    }
		} catch (IllegalArgumentException | IllegalAccessException exception) {
		    exception.printStackTrace();
		}
	    }
	}
	if (methods != null) {

	}
    }
}
