package com.iprogrammerr.riddle.security.token;

public class JsonWebTokenTemplate implements TokenTemplate {

    private static final byte[] SECRET = "SecRanByt".getBytes();
    private final String type;
    private final long validity;

    public JsonWebTokenTemplate(String type, long validity) {
	this.type = type;
	this.validity = validity;
    }

    @Override
    public byte[] secret() {
	return SECRET;
    }

    @Override
    public String typeKey() {
	return "type";
    }

    @Override
    public String type() {
	return type;
    }

    @Override
    public long validity() {
	return validity;
    }

}
