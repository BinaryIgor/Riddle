package com.iprogrammerr.riddle.security.token;

public interface TokenTemplate {

    byte[] secret();

    long validity();

    String typeKey();

    String type();
}
