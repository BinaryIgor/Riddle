package com.iprogrammerr.riddle.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ActivatingLink {

    private String activatingLink;

    @JsonCreator
    public ActivatingLink(@JsonProperty("activatingLink") String activatingLink) {
	this.activatingLink = activatingLink;
    }

    public String getActivatingLink() {
	return activatingLink;
    }

}
