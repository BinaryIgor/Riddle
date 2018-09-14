package com.iprogrammerr.riddle.security.token;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.iprogrammerr.bright.server.model.KeysValues;
import com.iprogrammerr.bright.server.model.StringsObjects;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JsonWebTokenDecryption implements TokenDecryption {

    private final String toDecrypt;
    private final TokenTemplate template;
    private Optional<Claims> claims;

    public JsonWebTokenDecryption(String toDecrypt, TokenTemplate template) {
	this.toDecrypt = toDecrypt;
	this.template = template;
	this.claims = Optional.empty();
    }

    @Override
    public String subject() throws Exception {
	readClaims();
	String username = claims.get().getSubject();
	if (username == null || username.isEmpty()) {
	    throw new Exception("Username is not present");
	}
	return username;
    }

    @Override
    public KeysValues additional() throws Exception {
	readClaims();
	Claims claims = this.claims.get();
	Iterator<String> claimsKeys = claims.keySet().iterator();
	List<String> toReadValuesKeys = new ArrayList<>();
	while (claimsKeys.hasNext()) {
	    String key = claimsKeys.next();
	    if (!key.equals(template.typeKey())) {
		toReadValuesKeys.add(key);
	    }
	}
	KeysValues additional = new StringsObjects();
	if (!toReadValuesKeys.isEmpty()) {
	    for (String key : toReadValuesKeys) {
		additional.add(key, claims.get(key));
	    }
	}
	return additional;
    }

    void readClaims() throws Exception {
	if (claims.isPresent()) {
	    return;

	}
	claims = Optional.of(Jwts.parser().setSigningKey(template.secret()).parseClaimsJws(toDecrypt).getBody());
	String type = claims.get().get(template.typeKey(), String.class);
	if (type == null || !type.equals(template.type())) {
	    throw new Exception(String.format("%s is not a refresh token type", type));
	}
    }
}
