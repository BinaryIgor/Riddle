package com.iprogrammerr.riddle.respondent.user;

import com.iprogrammerr.bright.server.model.KeysValues;
import com.iprogrammerr.bright.server.model.StringsObjects;
import com.iprogrammerr.bright.server.request.MatchedRequest;
import com.iprogrammerr.bright.server.respondent.Respondent;
import com.iprogrammerr.bright.server.response.Response;
import com.iprogrammerr.bright.server.response.template.BadRequestResponse;
import com.iprogrammerr.bright.server.response.template.OkResponse;
import com.iprogrammerr.riddle.security.Encryption;
import com.iprogrammerr.riddle.security.token.TokenTemplate;
import com.iprogrammerr.riddle.source.FromRequestUserSource;
import com.iprogrammerr.riddle.user.ToEditUser;
import com.iprogrammerr.riddle.user.User;
import com.iprogrammerr.riddle.user.json.ToEditJsonUser;
import com.iprogrammerr.riddle.users.Users;

public final class EditUserProfileRespondent implements Respondent {

    private final Users users;
    private final TokenTemplate tokenTemplate;
    private final String authorizationHeader;
    private final Encryption encryption;

    public EditUserProfileRespondent(Users users, TokenTemplate tokenTemplate, String authorizationHeader,
	    Encryption encryption) {
	this.users = users;
	this.tokenTemplate = tokenTemplate;
	this.authorizationHeader = authorizationHeader;
	this.encryption = encryption;
    }

    @Override
    public Response respond(MatchedRequest request) {
	Response response;
	try {
	    User user = new FromRequestUserSource(request, this.users, this.tokenTemplate, this.authorizationHeader)
		    .collect();
	    KeysValues toUpdate = new StringsObjects();
	    ToEditUser toEditUser = new ToEditJsonUser(new String(request.body()));
	    if (toEditUser.hasName()) {
		toUpdate.put("name", toEditUser.name());
	    }
	    if (toEditUser.hasEmail()) {
		toUpdate.put("email", toEditUser.email());
	    }
	    if (toEditUser.hasPassword()) {
		toUpdate.put("password", encryption.hash(toEditUser.password()));
	    }
	    user.update(toUpdate);
	    response = new OkResponse();
	} catch (Exception exception) {
	    response = new BadRequestResponse(exception.getMessage());
	}
	return response;
    }

}
