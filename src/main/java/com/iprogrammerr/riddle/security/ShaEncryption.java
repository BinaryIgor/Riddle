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
    public String encrypted(String origin) {
	byte[] encrypted = digest.digest(origin.getBytes(StandardCharsets.UTF_8));
	return new String(encrypted, StandardCharsets.UTF_8);
    }

    @Override
    public String hash(String... base) {
	StringBuilder builder = new StringBuilder();
	for (String element : base) {
	    builder.append(element);
	}
	byte[] encrypted = digest.digest(builder.toString().getBytes(StandardCharsets.UTF_8));
	return Base64.getEncoder().encodeToString(encrypted).replaceAll("=", USER_HASH_EQUALS_REPLACEMENT);
    }

}
