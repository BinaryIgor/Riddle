package com.iprogrammerr.riddle.model;

public class Field<T> {

    private String key;
    private T value;

    public Field(String key, T value) {
	this.key = key;
	this.value = value;
    }

    public String getKey() {
	return key;
    }

    public T getValue() {
	return value;
    }

}
