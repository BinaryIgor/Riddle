package com.iprogrammerr.riddle;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.iprogrammerr.riddle.entity.User;
import com.iprogrammerr.riddle.exception.validation.InvalidItemException;
import com.iprogrammerr.riddle.model.security.Activator;
import com.iprogrammerr.riddle.service.validation.ValidationService;

public class ValidationServiceTest {

    private ValidationService validationService;

    @Before
    public void setup() {
	validationService = new ValidationService();
    }

    @Test(expected = InvalidItemException.class)
    public void reflectionTest() {
	Activator activator = new Activator(1, null);
	validationService.validateNotNullFieldsRule(activator);
    }

    @Test
    public void validateUserTest() {
	User user = new User("ceigor94@gmail.com", "Igor", "adfakf3");
	validationService.validateUser(user);
	user = new User("ceigor94@gmail.com", "Igor", "ad");
	for (int i = 0; i < 4; i++) {
	    boolean exceptionThrown = false;
	    try {
		validationService.validateUser(getInvalidUser(i));
	    } catch (InvalidItemException exception) {
		exceptionThrown = true;
	    }
	    assertTrue(exceptionThrown);
	}
    }

    private User getInvalidUser(int version) {
	if (version == 0) {
	    return new User("cei", "Igor", "ad13ddada");
	}
	if (version == 1) {
	    return new User("ceigor.com", "Igor", "ad13ddada");
	}
	if (version == 2) {
	    return new User("ceigor@gmail.com", "I", "ad13ddada");
	}
	return new User("ceigor@gmail.com", "I", "ad");

    }

}
