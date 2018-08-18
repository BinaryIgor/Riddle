package com.iprogrammerr.riddle.model.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Activator {

    private long id;
    private String hash;

    @JsonCreator
    public Activator(@JsonProperty("id") long id, @JsonProperty("hash") String hash) {
	this.id = id;
	this.hash = hash;
    }

    public long getId() {
	return id;
    }

    public String getHash() {
	return hash;
    }

}
