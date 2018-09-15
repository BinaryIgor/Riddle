package com.iprogrammerr.riddle.respondent.user;

import org.json.JSONObject;

import com.iprogrammerr.bright.server.header.JsonContentTypeHeader;
import com.iprogrammerr.bright.server.request.MatchedRequest;
import com.iprogrammerr.bright.server.respondent.Respondent;
import com.iprogrammerr.bright.server.response.OkResponse;
import com.iprogrammerr.bright.server.response.Response;
import com.iprogrammerr.bright.server.response.UnauthorizedResponse;
import com.iprogrammerr.bright.server.response.BadRequestResponse;
import com.iprogrammerr.riddle.database.DatabaseSession;
import com.iprogrammerr.riddle.database.QueryTemplate;
import com.iprogrammerr.riddle.security.Encryption;
import com.iprogrammerr.riddle.user.DatabaseUser;
import com.iprogrammerr.riddle.user.User;

public class UserActivationRespondent implements Respondent {

    private final DatabaseSession session;
    private final QueryTemplate template;
    private final Encryption encrypton;

    public UserActivationRespondent(DatabaseSession session, QueryTemplate template, Encryption encrypton) {
	this.session = session;
	this.template = template;
	this.encrypton = encrypton;
    }

    @Override
    public Response respond(MatchedRequest request) throws Exception {
	try {
	    JSONObject jsonObject = new JSONObject(new String(request.body()));
	    long id = jsonObject.getLong("id");
	    String hash = jsonObject.getString("hash");
	    User user = new DatabaseUser(id, session, template);
	    String userHash = encrypton.encrypted(user);
	    if (!userHash.equals(hash)) {
		return new UnauthorizedResponse("Wrong activating hash");
	    }
	    return new OkResponse(new JsonContentTypeHeader(),
		    new JSONObject().put("username", user.name()).toString());
	} catch (Exception exception) {
	    exception.printStackTrace();
	    return new BadRequestResponse(exception.getMessage());
	}
    }

}
