package com.iprogrammerr.riddle.filter;

import com.iprogrammerr.bright.server.filter.RequestFilter;
import com.iprogrammerr.bright.server.request.Request;
import com.iprogrammerr.bright.server.response.Response;
import com.iprogrammerr.bright.server.response.template.ForbiddenResponse;
import com.iprogrammerr.bright.server.response.template.OkResponse;

public final class AuthorizationFilter implements RequestFilter {

    private final String headerKey;
    private final String headerValuePrefix;

    public AuthorizationFilter(String headerKey, String headerValuePrefix) {
	this.headerKey = headerKey;
	this.headerValuePrefix = headerValuePrefix;
    }

    @Override
    public Response filter(Request request) {
	boolean forbidden = !request.hasHeader(headerKey) || !validHeader(request);
	return forbidden ? new ForbiddenResponse("Authorization with Bearer prefix is required") : new OkResponse();
    }

    private boolean validHeader(Request request) {
	boolean valid;
	try {
	    String authorizationValue = request.header(headerKey);
	    if (authorizationValue.startsWith(headerValuePrefix)) {
		valid = true;
	    } else {
		valid = false;
	    }
	} catch (Exception excetpion) {
	    valid = false;
	}
	return valid;
    }

}
