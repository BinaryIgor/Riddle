package com.iprogrammerr.riddle.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Token {

    private String value;
    private long expirationDate;

    @JsonCreator
    public Token(@JsonProperty("value") String value, @JsonProperty("expirationDate") long expirationDate) {
	this.value = value;
	this.expirationDate = expirationDate;
    }

    public String getValue() {
	return value;
    }

    public long getExpirationDate() {
	return expirationDate;
    }
}
