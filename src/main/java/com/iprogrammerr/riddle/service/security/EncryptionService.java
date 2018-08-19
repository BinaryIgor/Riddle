package com.iprogrammerr.riddle.service.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import com.iprogrammerr.riddle.entity.User;
import com.iprogrammerr.riddle.exception.creation.CreationException;

public class EncryptionService {

    private static final String USER_HASH_EQUALS_REPLACEMENT = "S";
    private final MessageDigest digest;

    public EncryptionService() {
	try {
	    digest = MessageDigest.getInstance("SHA-256");
	} catch (NoSuchAlgorithmException exception) {
	    exception.printStackTrace();
	    throw new CreationException(exception.getMessage());
	}
    }

    public String encrypt(String toEncrypt) {
	byte[] encrypted = digest.digest(toEncrypt.getBytes(StandardCharsets.UTF_8));
	return new String(encrypted, StandardCharsets.UTF_8);
    }

    public String getToSendUserHash(User user) {
	String toHash = user.getEmail() + user.getName() + user.getPassword();
	byte[] encrypted = digest.digest(toHash.getBytes(StandardCharsets.UTF_8));
	return Base64.getEncoder().encodeToString(encrypted).replaceAll("=", USER_HASH_EQUALS_REPLACEMENT);
    }
}
