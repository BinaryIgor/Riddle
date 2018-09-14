package com.iprogrammerr.riddle.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Username {

    private String name;

    @JsonCreator
    public Username(@JsonProperty("name") String name) {
	this.name = name;
    }

    public String getName() {
	return name;
    }

}
