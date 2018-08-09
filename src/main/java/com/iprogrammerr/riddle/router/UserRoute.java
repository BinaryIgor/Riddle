package com.iprogrammerr.riddle.router;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;

import com.iprogrammerr.riddle.entity.User;
import com.iprogrammerr.riddle.exception.NotResolvedRouteException;
import com.iprogrammerr.riddle.model.ToSignInUser;
import com.iprogrammerr.riddle.service.crud.UserService;
import com.iprogrammerr.riddle.service.json.JsonService;
import com.iprogrammerr.riddle.service.token.TokenService;
import com.iprogrammerr.riddle.service.validation.ValidationService;

public class UserRoute extends Route {

    private static final String SIGN_IN_PATH = "sign-in";
    private static final String SIGN_UP_PATH = "sign-up";
    private ValidationService validationService;
    private UserService userService;
    private TokenService tokenService;

    public UserRoute(UserService userService, ValidationService validationService, TokenService tokenService,
	    JsonService jsonService) {
	super("user", jsonService);
	this.validationService = validationService;
	this.userService = userService;
	this.tokenService = tokenService;
    }

    public void signIn(ToSignInUser toSignInUser) {
	validationService.validateObject(ToSignInUser.class, toSignInUser);
	System.out.println(toSignInUser);
	User user = userService.getUserByNameOrEmail(toSignInUser.nameEmail);
    }

    public void signUp(User user) {
	validationService.validateEntity(User.class, user);
    }

    @Override
    public void resolveGetRequest(String path, HttpServletRequest request, HttpServletResponse response) {
	throw new NotResolvedRouteException();
    }

    @Override
    public void resolvePostRequest(String path, HttpServletRequest request, HttpServletResponse response) {
	if (path.equals(SIGN_IN_PATH)) {
	    ToSignInUser user = resolveBody(ToSignInUser.class, request);
	    signIn(user);
	    response.setStatus(HttpStatus.OK_200);
	} else if (path.equals(SIGN_UP_PATH)) {
	    User user = resolveBody(User.class, request);
	    signUp(user);
	    response.setStatus(HttpStatus.CREATED_201);
	} else {
	    throw new NotResolvedRouteException();
	}
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
