package com.iprogrammerr.riddle.respondent.user;

import com.iprogrammerr.bright.server.request.MatchedRequest;
import com.iprogrammerr.bright.server.respondent.Respondent;
import com.iprogrammerr.bright.server.response.Response;
import com.iprogrammerr.bright.server.response.body.JsonResponseBody;
import com.iprogrammerr.bright.server.response.template.BadRequestResponse;
import com.iprogrammerr.bright.server.response.template.NoContentResponse;
import com.iprogrammerr.bright.server.response.template.OkResponse;
import com.iprogrammerr.riddle.database.DatabaseSession;
import com.iprogrammerr.riddle.database.QueryTemplate;
import com.iprogrammerr.riddle.response.body.UserBody;
import com.iprogrammerr.riddle.security.token.JsonWebTokenDecryption;
import com.iprogrammerr.riddle.security.token.TokenTemplate;
import com.iprogrammerr.riddle.user.DatabaseUser;
import com.iprogrammerr.riddle.user.User;
import com.iprogrammerr.riddle.users.Users;

public final class UserProfileRespondent implements Respondent {

    private final DatabaseSession session;
    private final QueryTemplate template;
    private final Users users;
    private final TokenTemplate tokenTemplate;
    private final String authorizationHeader;

    public UserProfileRespondent(DatabaseSession session, QueryTemplate template, Users users,
	    TokenTemplate tokenTemplate, String authorizationHeader) {
	this.session = session;
	this.template = template;
	this.users = users;
	this.tokenTemplate = tokenTemplate;
	this.authorizationHeader = authorizationHeader;
    }

    @Override
    public Response respond(MatchedRequest request) {
	try {
	    User user;
	    if (request.hasPathVariable("id", Long.class)) {
		long id = request.pathVariable("id", Long.class);
		if (id < 1) {
		    return new BadRequestResponse("id must be a positive number");
		}
		user = new DatabaseUser(id, session, template);
	    } else {
		user = fromToken(request);
	    }
	    return new OkResponse(
		    new JsonResponseBody(new UserBody(user.name(), user.email(), user.password()).content()));
	} catch (Exception exception) {
	    exception.printStackTrace();
	    return new NoContentResponse();
	}
    }

    private User fromToken(MatchedRequest request) throws Exception {
	String token = request.header(authorizationHeader);
	String name = new JsonWebTokenDecryption(token, tokenTemplate).subject();
	return users.user(name);
    }
}
