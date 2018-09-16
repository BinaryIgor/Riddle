package com.iprogrammerr.riddle.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class ShaEncryption implements Encryption {

    private static final String USER_HASH_EQUALS_REPLACEMENT = "S";
    private final MessageDigest digest;

    public ShaEncryption() throws Exception {
	digest = MessageDigest.getInstance("SHA-256");
    }

    @Override
    public String hash(String base) {
	byte[] encrypted = digest.digest(base.getBytes(StandardCharsets.UTF_8));
	return Base64.getEncoder().encodeToString(encrypted).replaceAll("=", USER_HASH_EQUALS_REPLACEMENT);
    }

}
