package com.iprogrammerr.riddle.respondent.user;

import com.iprogrammerr.bright.server.request.MatchedRequest;
import com.iprogrammerr.bright.server.respondent.Respondent;
import com.iprogrammerr.bright.server.response.Response;
import com.iprogrammerr.bright.server.response.body.JsonResponseBody;
import com.iprogrammerr.bright.server.response.template.NoContentResponse;
import com.iprogrammerr.bright.server.response.template.OkResponse;
import com.iprogrammerr.bright.server.response.template.UnauthenticatedResponse;
import com.iprogrammerr.riddle.response.body.SignInBody;
import com.iprogrammerr.riddle.security.Encryption;
import com.iprogrammerr.riddle.security.token.JsonWebToken;
import com.iprogrammerr.riddle.security.token.Token;
import com.iprogrammerr.riddle.security.token.TokenTemplate;
import com.iprogrammerr.riddle.user.ToSignInJsonUser;
import com.iprogrammerr.riddle.user.ToSignInUser;
import com.iprogrammerr.riddle.user.User;
import com.iprogrammerr.riddle.users.Users;

public class SignInRespondent implements Respondent {

    private final Users users;
    private final Encryption encryption;
    private final TokenTemplate accessTokenTemplate;
    private final TokenTemplate refreshTokenTemplate;

    public SignInRespondent(Users users, Encryption encryption, TokenTemplate accessTokenTemplate,
	    TokenTemplate refreshTokenTemplate) {
	this.users = users;
	this.encryption = encryption;
	this.accessTokenTemplate = accessTokenTemplate;
	this.refreshTokenTemplate = refreshTokenTemplate;

    }

    @Override
    public Response respond(MatchedRequest request) {
	try {
	    ToSignInUser toSignInUser = new ToSignInJsonUser(new String(request.body()));
	    User user = users.user(toSignInUser.nameOrEmail());
	    String encryptedPassword = encryption.hash(toSignInUser.password());
	    if (!encryptedPassword.equals(user.password())) {
		return new UnauthenticatedResponse("Invalid password");
	    }
	    if (!user.active()) {
		return new UnauthenticatedResponse("User needs to be activated first");
	    }
	    Token accessToken = new JsonWebToken(user.name(), accessTokenTemplate);
	    Token refreshToken = new JsonWebToken(user.name(), refreshTokenTemplate);
	    return new OkResponse(
		    new JsonResponseBody(new SignInBody(user.role(), accessToken, refreshToken).content()));
	} catch (Exception exception) {
	    exception.printStackTrace();
	    return new NoContentResponse();
	}
    }

}
