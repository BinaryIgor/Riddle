package com.iprogrammerr.riddle.service.validation;

import java.util.List;

import com.iprogrammerr.riddle.entity.User;
import com.iprogrammerr.riddle.exception.validation.InvalidItemException;
import com.iprogrammerr.riddle.util.ReflectionUtil;
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

    public <T> void validateNotNullFieldsRule(T object) {
	List<Object> fields = ReflectionUtil.getAllAccessibleFields(object);
	for (Object field : fields) {
	    if (field == null) {
		throw new InvalidItemException("Fields can not be null.");
	    }
	}
    }
}
