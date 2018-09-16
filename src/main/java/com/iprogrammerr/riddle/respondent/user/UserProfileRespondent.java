package com.iprogrammerr.riddle.respondent.user;

import com.iprogrammerr.bright.server.request.MatchedRequest;
import com.iprogrammerr.bright.server.respondent.Respondent;
import com.iprogrammerr.bright.server.response.BadRequestResponse;
import com.iprogrammerr.bright.server.response.NoContentResponse;
import com.iprogrammerr.bright.server.response.OkResponse;
import com.iprogrammerr.bright.server.response.Response;
import com.iprogrammerr.bright.server.response.body.JsonResponseBody;
import com.iprogrammerr.riddle.database.DatabaseSession;
import com.iprogrammerr.riddle.database.QueryTemplate;
import com.iprogrammerr.riddle.response.body.UserProfileBody;
import com.iprogrammerr.riddle.user.DatabaseUser;
import com.iprogrammerr.riddle.user.MockedUserProfile;
import com.iprogrammerr.riddle.user.User;
import com.iprogrammerr.riddle.user.UserProfile;

public class UserProfileRespondent implements Respondent {

    private final DatabaseSession session;
    private final QueryTemplate template;

    public UserProfileRespondent(DatabaseSession session, QueryTemplate template) {
	this.session = session;
	this.template = template;
    }

    // TODO unmock user profile!
    @Override
    public Response respond(MatchedRequest request) throws Exception {
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
	    UserProfile userProfile = new MockedUserProfile();
	    return new OkResponse(new JsonResponseBody(
		    new UserProfileBody(user.name(), user.email(), userProfile.points()).content()));
	} catch (Exception exception) {
	    exception.printStackTrace();
	    return new NoContentResponse();
	}
    }

    // TODO get username from token and then find user
    private User fromToken(MatchedRequest request) throws Exception {
	throw new Exception();
    }
}
