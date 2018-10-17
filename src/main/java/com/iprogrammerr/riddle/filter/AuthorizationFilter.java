package com.iprogrammerr.riddle.filter;

import com.iprogrammerr.bright.server.filter.Filter;
import com.iprogrammerr.bright.server.request.Request;
import com.iprogrammerr.bright.server.response.Response;
import com.iprogrammerr.bright.server.response.template.ForbiddenResponse;
import com.iprogrammerr.bright.server.response.template.OkResponse;

public final class AuthorizationFilter implements Filter {

    private final String headerKey;
    private final String headerValuePrefix;

    public AuthorizationFilter(String headerKey, String headerValuePrefix) {
	this.headerKey = headerKey;
	this.headerValuePrefix = headerValuePrefix;
    }

    @Override
    public Response response(Request request) {
	boolean forbidden;
	if (request.hasHeader(headerKey)) {
	    try {
		String authorizationValue = request.header(headerKey);
		if (authorizationValue.startsWith(headerValuePrefix)) {
		    forbidden = false;
		} else {
		    forbidden = true;
		}
	    } catch (Exception excetpion) {
		forbidden = true;
	    }
	} else {
	    forbidden = true;
	}
	return forbidden ? new ForbiddenResponse("Authorization with Bearer prefix is required") : new OkResponse();
    }

}
