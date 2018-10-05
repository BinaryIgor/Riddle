package com.iprogrammerr.riddle.security.token;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.iprogrammerr.bright.server.model.KeysValues;
import com.iprogrammerr.bright.server.model.StringsObjects;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public final class JsonWebTokenDecryption implements TokenDecryption {

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
	String username = this.claims.get().getSubject();
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
	    if (!key.equals(this.template.typeKey())) {
		toReadValuesKeys.add(key);
	    }
	}
	KeysValues additional = new StringsObjects();
	if (!toReadValuesKeys.isEmpty()) {
	    for (String key : toReadValuesKeys) {
		additional.put(key, claims.get(key));
	    }
	}
	return additional;
    }

    private void readClaims() throws Exception {
	if (this.claims.isPresent()) {
	    return;

	}
	this.claims = Optional.of(Jwts.parser().setSigningKey(this.template.secret())
		.parseClaimsJws(this.toDecrypt.replaceAll("Bearer ", "")).getBody());
	String type = this.claims.get().get(this.template.typeKey(), String.class);
	if (type == null || !type.equals(this.template.type())) {
	    throw new Exception(String.format("%s is not a %s token type", type, this.template.type()));
	}
    }
}
