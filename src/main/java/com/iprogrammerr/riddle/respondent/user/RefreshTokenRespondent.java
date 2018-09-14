package com.iprogrammerr.riddle.respondent.user;

import org.json.JSONObject;

import com.iprogrammerr.bright.server.header.JsonContentTypeHeader;
import com.iprogrammerr.bright.server.request.MatchedRequest;
import com.iprogrammerr.bright.server.respondent.Respondent;
import com.iprogrammerr.bright.server.response.OkResponse;
import com.iprogrammerr.bright.server.response.Response;
import com.iprogrammerr.bright.server.response.UnauthorizedResponse;
import com.iprogrammerr.riddle.response.body.NewAccessTokenBody;
import com.iprogrammerr.riddle.security.token.JsonWebToken;
import com.iprogrammerr.riddle.security.token.JsonWebTokenDecryption;
import com.iprogrammerr.riddle.security.token.Token;
import com.iprogrammerr.riddle.security.token.TokenDecryption;
import com.iprogrammerr.riddle.security.token.TokenTemplate;
import com.iprogrammerr.riddle.user.Users;

public class RefreshTokenRespondent implements Respondent {

    private final Users users;
    private final TokenTemplate accessTokenTemplate;
    private final TokenTemplate refreshTokenTemplate;

    public RefreshTokenRespondent(Users users, TokenTemplate accessTokenTemplate, TokenTemplate refreshTokenTemplate) {
	this.users = users;
	this.accessTokenTemplate = accessTokenTemplate;
	this.refreshTokenTemplate = refreshTokenTemplate;
    }

    @Override
    public Response respond(MatchedRequest request) throws Exception {
	try {
	    JSONObject tokenJson = new JSONObject(new String(request.body()));
	    TokenDecryption decryption = new JsonWebTokenDecryption(tokenJson.getString("value"), refreshTokenTemplate);
	    String username = decryption.subject();
	    if (!users.exists(username)) {
		return new UnauthorizedResponse("Given user does not exist");
	    }
	    Token acessToken = new JsonWebToken(username, accessTokenTemplate);
	    return new OkResponse(new JsonContentTypeHeader(), new NewAccessTokenBody(acessToken).content());
	} catch (Exception exception) {
	    exception.printStackTrace();
	    return new UnauthorizedResponse(exception.getMessage());
	}
    }

}
