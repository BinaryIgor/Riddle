package com.iprogrammerr.riddle.service.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.iprogrammerr.riddle.exception.validation.InvalidItemException;

public class ValidationService {

    private Validator validator;

    public ValidationService(Validator validator) {
	this.validator = validator;
    }

    public <T> void validateObject(Class<T> clazz, T object) {
	Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);
	if (!constraintViolations.isEmpty()) {
	    throw new InvalidItemException(getInvalidItemMessage(constraintViolations));
	}
    }

    public <Entity> void validateEntity(Class<Entity> clazz, Entity entity) {
	validateObject(clazz, entity);
    }

    private <T> String getInvalidItemMessage(Set<ConstraintViolation<T>> constraintViolations) {
	StringBuilder builder = new StringBuilder();
	for (ConstraintViolation<T> violation : constraintViolations) {
	    builder.append(violation.getPropertyPath()).append(":").append(violation.getMessage()).append(" ");
	}
	return builder.toString();
    }
}
