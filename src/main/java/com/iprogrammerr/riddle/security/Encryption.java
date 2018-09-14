package com.iprogrammerr.riddle.security;

public interface Encryption {

    String encrypted(String origin);

    String hash(String... base);

}
