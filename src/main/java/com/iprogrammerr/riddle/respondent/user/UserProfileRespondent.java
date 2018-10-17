package com.iprogrammerr.riddle.respondent.user;

import com.iprogrammerr.bright.server.request.MatchedRequest;
import com.iprogrammerr.bright.server.respondent.Respondent;
import com.iprogrammerr.bright.server.response.Response;
import com.iprogrammerr.bright.server.response.body.JsonResponseBody;
import com.iprogrammerr.bright.server.response.template.BadRequestResponse;
import com.iprogrammerr.bright.server.response.template.OkResponse;
import com.iprogrammerr.riddle.response.body.UserBody;
import com.iprogrammerr.riddle.security.token.TokenTemplate;
import com.iprogrammerr.riddle.user.User;
import com.iprogrammerr.riddle.user.request.RequestUser;
import com.iprogrammerr.riddle.users.Users;

public final class UserProfileRespondent implements Respondent {

    private final Users users;
    private final TokenTemplate tokenTemplate;
    private final String authorizationHeader;

    public UserProfileRespondent(Users users, TokenTemplate tokenTemplate, String authorizationHeader) {
	this.users = users;
	this.tokenTemplate = tokenTemplate;
	this.authorizationHeader = authorizationHeader;
    }

    @Override
    public Response response(MatchedRequest request) {
	Response response;
	try {
	    User user = new RequestUser(request, this.users, this.tokenTemplate, this.authorizationHeader).user();
	    response = new OkResponse(
		    new JsonResponseBody(new UserBody(user.name(), user.email(), user.password()).content()));
	} catch (Exception exception) {
	    response = new BadRequestResponse(exception.getMessage());
	}
	return response;
    }
}
