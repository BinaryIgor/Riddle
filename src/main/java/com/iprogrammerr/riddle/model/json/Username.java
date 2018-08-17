package com.iprogrammerr.riddle.model.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Username {

    public String name;

    @JsonCreator
    public Username(@JsonProperty("name") String name) {
	this.name = name;
    }
}
