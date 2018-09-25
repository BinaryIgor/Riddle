package com.iprogrammerr.riddle.respondent.user;

import org.json.JSONObject;

import com.iprogrammerr.bright.server.model.StringsObjects;
import com.iprogrammerr.bright.server.request.MatchedRequest;
import com.iprogrammerr.bright.server.respondent.Respondent;
import com.iprogrammerr.bright.server.response.Response;
import com.iprogrammerr.bright.server.response.body.JsonResponseBody;
import com.iprogrammerr.bright.server.response.template.BadRequestResponse;
import com.iprogrammerr.bright.server.response.template.OkResponse;
import com.iprogrammerr.bright.server.response.template.UnauthorizedResponse;
import com.iprogrammerr.riddle.database.DatabaseSession;
import com.iprogrammerr.riddle.database.QueryTemplate;
import com.iprogrammerr.riddle.security.Encryption;
import com.iprogrammerr.riddle.user.DatabaseUser;
import com.iprogrammerr.riddle.user.User;

public class UserActivationRespondent implements Respondent {

    private final DatabaseSession session;
    private final QueryTemplate template;
    private final Encryption encryption;

    public UserActivationRespondent(DatabaseSession session, QueryTemplate template, Encryption encryption) {
	this.session = session;
	this.template = template;
	this.encryption = encryption;
    }

    @Override
    public Response respond(MatchedRequest request) {
	try {
	    JSONObject jsonObject = new JSONObject(new String(request.body()));
	    long id = jsonObject.getLong("id");
	    String hash = jsonObject.getString("hash");
	    User user = new DatabaseUser(id, session, template);
	    String userHash = encryption.hash(user.name());
	    if (!userHash.equals(hash)) {
		return new UnauthorizedResponse("Wrong activating hash");
	    }
	    user.update(new StringsObjects().add("active", true));
	    return new OkResponse(new JsonResponseBody(new JSONObject().put("username", user.name()).toString()));
	} catch (Exception exception) {
	    exception.printStackTrace();
	    return new BadRequestResponse(exception.getMessage());
	}
    }

}
