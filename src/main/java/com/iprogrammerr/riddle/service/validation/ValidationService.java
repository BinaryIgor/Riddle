package com.iprogrammerr.riddle.service.validation;

public class ValidationService {

    public <T> void validateObject(Class<T> clazz, T object) {
    }

    public <Entity> void validateEntity(Class<Entity> clazz, Entity entity) {
	validateObject(clazz, entity);
    }

}
