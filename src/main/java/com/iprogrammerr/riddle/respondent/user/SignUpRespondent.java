package com.iprogrammerr.riddle.respondent.user;

import com.iprogrammerr.bright.server.request.MatchedRequest;
import com.iprogrammerr.bright.server.respondent.Respondent;
import com.iprogrammerr.bright.server.response.Response;
import com.iprogrammerr.bright.server.response.template.BadRequestResponse;
import com.iprogrammerr.bright.server.response.template.CreatedResponse;
import com.iprogrammerr.riddle.email.EmailServer;
import com.iprogrammerr.riddle.security.Encryption;
import com.iprogrammerr.riddle.user.ToValidateSignUpUser;
import com.iprogrammerr.riddle.user.ValidatableToSignUpUser;
import com.iprogrammerr.riddle.user.json.ToSignUpJsonUser;
import com.iprogrammerr.riddle.users.Users;

public final class SignUpRespondent implements Respondent {

    private final String activatingLinkBase;
    private final Users users;
    private final EmailServer emailServer;
    private final Encryption encryption;

    public SignUpRespondent(String activatingLinkBase, Users users, EmailServer emailServer, Encryption encryption) {
	this.activatingLinkBase = activatingLinkBase;
	this.users = users;
	this.emailServer = emailServer;
	this.encryption = encryption;
    }

    @Override
    public Response response(MatchedRequest request) {
	try {
	    ValidatableToSignUpUser toSignUpUser = new ToValidateSignUpUser(
		    new ToSignUpJsonUser(new String(request.body())));
	    toSignUpUser.validate();
	    if (this.users.exists(toSignUpUser.name())) {
		return new BadRequestResponse(toSignUpUser.name() + " is taken already");
	    }
	    if (this.users.exists(toSignUpUser.email())) {
		return new BadRequestResponse(toSignUpUser.email() + " is taken already");
	    }
	    String hashedPassword = this.encryption.hash(toSignUpUser.password());
	    long id = this.users.createPlayer(toSignUpUser.name(), toSignUpUser.email(), hashedPassword);
	    String userHash = this.encryption.hash(toSignUpUser.name());
	    String activatingLink = String.format("%s?id=%d&activate=%s", this.activatingLinkBase, id, userHash);
	    this.emailServer.sendSigningUp(toSignUpUser.email(), activatingLink);
	    return new CreatedResponse("Account has been created. Check your email to make it active.");
	} catch (Exception exception) {
	    exception.printStackTrace();
	    return new BadRequestResponse(exception.getMessage());
	}
    }

}
