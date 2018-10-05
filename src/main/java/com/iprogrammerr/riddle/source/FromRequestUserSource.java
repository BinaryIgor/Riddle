package com.iprogrammerr.riddle.source;

import com.iprogrammerr.bright.server.request.MatchedRequest;
import com.iprogrammerr.riddle.security.token.JsonWebTokenDecryption;
import com.iprogrammerr.riddle.security.token.TokenTemplate;
import com.iprogrammerr.riddle.user.User;
import com.iprogrammerr.riddle.users.Users;

public final class FromRequestUserSource implements UserSource {

    private final MatchedRequest source;
    private final Users users;
    private final TokenTemplate tokenTemplate;
    private final String authorizationHeaderKey;

    public FromRequestUserSource(MatchedRequest source, Users users, TokenTemplate tokenTemplate,
	    String authorizationHeaderKey) {
	this.source = source;
	this.users = users;
	this.tokenTemplate = tokenTemplate;
	this.authorizationHeaderKey = authorizationHeaderKey;
    }

    @Override
    public User user() throws Exception {
	User user;
	if (this.source.hasPathVariable("id", Long.class)) {
	    long id = this.source.pathVariable("id", Long.class);
	    if (id < 1) {
		throw new Exception("id must be a positive number");
	    }
	    user = this.users.user(id);
	} else {
	    String name = new JsonWebTokenDecryption(this.source.header(this.authorizationHeaderKey),
		    this.tokenTemplate).subject();
	    user = this.users.user(name);
	}
	return user;
    }

}
