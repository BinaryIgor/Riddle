package com.iprogrammerr.riddle.respondent.user;

import com.iprogrammerr.bright.server.request.MatchedRequest;
import com.iprogrammerr.bright.server.respondent.Respondent;
import com.iprogrammerr.bright.server.response.Response;
import com.iprogrammerr.bright.server.response.body.JsonResponseBody;
import com.iprogrammerr.bright.server.response.template.BadRequestResponse;
import com.iprogrammerr.bright.server.response.template.OkResponse;
import com.iprogrammerr.riddle.model.Columns;
import com.iprogrammerr.riddle.model.TypedMap;
import com.iprogrammerr.riddle.response.body.SignInBody;
import com.iprogrammerr.riddle.security.Encryption;
import com.iprogrammerr.riddle.security.token.JsonWebToken;
import com.iprogrammerr.riddle.security.token.Token;
import com.iprogrammerr.riddle.security.token.TokenTemplate;
import com.iprogrammerr.riddle.user.ToEditUser;
import com.iprogrammerr.riddle.user.User;
import com.iprogrammerr.riddle.user.json.ToEditJsonUser;
import com.iprogrammerr.riddle.user.request.RequestUser;
import com.iprogrammerr.riddle.users.Users;

public final class EditUserProfileRespondent implements Respondent {

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String BOUNDARY_PREFIX = "boundary=";
    private final Users users;
    private final TokenTemplate accessTokenTemplate;
    private final TokenTemplate refreshTokenTemplate;
    private final String authorizationHeader;
    private final Encryption encryption;

    public EditUserProfileRespondent(Users users, TokenTemplate accessTokenTemplate, TokenTemplate refreshTokenTemplate,
	    String authorizationHeader, Encryption encryption) {
	this.users = users;
	this.accessTokenTemplate = accessTokenTemplate;
	this.refreshTokenTemplate = refreshTokenTemplate;
	this.authorizationHeader = authorizationHeader;
	this.encryption = encryption;
    }

    @Override
    public Response response(MatchedRequest request) {
	Response response;
	try {
	    User user = new RequestUser(request, this.users, this.accessTokenTemplate, this.authorizationHeader).user();
	    String boundary = request.header(CONTENT_TYPE).split(BOUNDARY_PREFIX)[1].trim();
	    TypedMap toUpdate = new Columns();
	    ToEditUser toEditUser = new ToEditJsonUser(new String(request.body()));
	    if (toEditUser.hasEmail()) {
		toUpdate.put("email", toEditUser.email());
	    }
	    if (toEditUser.hasPasswords()) {
		checkPassword(user, toEditUser.oldPassword());
		toUpdate.put("password", encryption.hash(toEditUser.newPassword()));
	    }
	    boolean newToken = toEditUser.hasName();
	    if (newToken) {
		toUpdate.put("name", toEditUser.name());
	    }
	    user.change(toUpdate);
	    if (newToken) {
		Token accessToken = new JsonWebToken(user.name(), this.accessTokenTemplate);
		Token refreshToken = new JsonWebToken(user.name(), this.refreshTokenTemplate);
		response = new OkResponse(
			new JsonResponseBody(new SignInBody(user.role(), accessToken, refreshToken).content()));
	    } else {
		response = new OkResponse();
	    }
	} catch (Exception exception) {
	    response = new BadRequestResponse(exception.getMessage());
	}
	return response;
    }

    private void checkPassword(User user, String password) throws Exception {
	String hashed = encryption.hash(password);
	if (!hashed.equals(user.password())) {
	    throw new Exception("Provided password is not a valid one.");
	}
    }

}
