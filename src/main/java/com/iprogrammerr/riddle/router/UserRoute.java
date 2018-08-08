package com.iprogrammerr.riddle.router;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;

import com.iprogrammerr.riddle.entity.User;
import com.iprogrammerr.riddle.exception.NotResolvedRouteException;
import com.iprogrammerr.riddle.service.JsonService;
import com.iprogrammerr.riddle.service.crud.UserService;

public class UserRoute extends Route {

    private final String SIGN_UP_PATH = "sign-up";
    private UserService userService;

    public UserRoute(UserService userService, JsonService jsonService) {
	super("user", jsonService);
	this.userService = userService;
    }

    public void signUp(User user) {
	System.out.println("Signed uped as = " + user);

    }

    @Override
    public void resolveGetRequest(String path, HttpServletRequest request, HttpServletResponse response) {
	throw new NotResolvedRouteException();
    }

    @Override
    public void resolvePostRequest(String path, HttpServletRequest request, HttpServletResponse response) {
	if (path.equals(SIGN_UP_PATH)) {
	    User user = resolveBody(User.class, request);
	    signUp(user);
	}
	response.setStatus(HttpStatus.CREATED_201);

    }

    @Override
    public void resolvePutRequest(String path, HttpServletRequest request, HttpServletResponse response) {
	throw new NotResolvedRouteException();
    }

    @Override
    public void resolveDeleteRequest(String path, HttpServletRequest request, HttpServletResponse response) {
	throw new NotResolvedRouteException();
    }

}
