package com.iprogrammerr.riddle.security.token;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JsonWebToken implements Token {

    private TokenTemplate template;
    private String username;
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
	    createToken();
	}
	return token;
    }

    @Override
    public long expirationDate() {
	if (token.isEmpty()) {
	    createToken();
	}
	return validity;

    }

    private void createToken() {
	validity = template.validity() + System.currentTimeMillis();
	token = Jwts.builder().setSubject(username).claim(template.typeKey(), template.type())
		.setExpiration(new Date(validity)).signWith(SignatureAlgorithm.HS512, template.secret()).compact();
    }

}
