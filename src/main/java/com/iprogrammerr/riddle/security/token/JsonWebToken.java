package com.iprogrammerr.riddle.security.token;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public final class JsonWebToken implements Token {

    private final TokenTemplate template;
    private final String username;
    private long validity;
    private String token;

    public JsonWebToken(String username, TokenTemplate template) {
	this.template = template;
	this.username = username;
	this.token = "";
    }

    @Override
    public String value() {
	if (token.isEmpty()) {
	    create();
	}
	return token;
    }

    @Override
    public long expirationDate() {
	if (token.isEmpty()) {
	    create();
	}
	return validity;

    }

    private void create() {
	validity = template.validity() + System.currentTimeMillis();
	token = Jwts.builder().setSubject(username).claim(template.typeKey(), template.type())
		.setExpiration(new Date(validity)).signWith(SignatureAlgorithm.HS512, template.secret()).compact();
    }

}
