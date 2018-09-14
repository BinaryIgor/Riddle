package com.iprogrammerr.riddle.security.token;

public interface Token {

    String value();

    long expirationDate();
}
