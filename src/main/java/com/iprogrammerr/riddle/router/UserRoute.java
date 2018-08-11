package com.iprogrammerr.riddle.router;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;

import com.iprogrammerr.riddle.entity.User;
import com.iprogrammerr.riddle.entity.UserRole;
import com.iprogrammerr.riddle.exception.NotResolvedRouteException;
import com.iprogrammerr.riddle.model.SignInResponse;
import com.iprogrammerr.riddle.model.ToSignInUser;
import com.iprogrammerr.riddle.model.Token;
import com.iprogrammerr.riddle.service.crud.UserService;
import com.iprogrammerr.riddle.service.json.JsonService;
import com.iprogrammerr.riddle.service.security.SecurityService;
import com.iprogrammerr.riddle.service.validation.ValidationService;

public class UserRoute extends Route {

    private static final String SIGN_IN_PATH = "sign-in";
    private static final String SIGN_UP_PATH = "sign-up";
    private static final String REFRESH_TOKEN_PATH = "token-refresh";
    private ValidationService validationService;
    private UserService userService;
    private SecurityService securityService;

    public UserRoute(UserService userService, ValidationService validationService, SecurityService securityService,
	    JsonService jsonService) {
	super("user", jsonService);
	this.validationService = validationService;
	this.userService = userService;
	this.securityService = securityService;
    }

    private SignInResponse signIn(ToSignInUser toSignInUser) {
	validationService.validateObject(ToSignInUser.class, toSignInUser);
	System.out.println(toSignInUser);
	User user = userService.getUserByNameOrEmail(toSignInUser.nameEmail);
	UserRole role = user.getUserRole();
	Token accessToken = securityService.createAccessToken(user.getName(), role.getName());
	Token refreshToken = securityService.createRefreshToken(user.getName(), role.getName());
	return new SignInResponse(role.getName(), accessToken, refreshToken);
    }

    private void signUp(User user) {
	validationService.validateEntity(User.class, user);
    }

    private Token refreshToken(String refreshToken) {

	return null;
    }

    @Override
    public void resolveGetRequest(String path, HttpServletRequest request, HttpServletResponse response) {
	throw new NotResolvedRouteException();
    }

    @Override
    public void resolvePostRequest(String path, HttpServletRequest request, HttpServletResponse response) {
	if (path.equals(SIGN_IN_PATH)) {
	    ToSignInUser user = getBody(ToSignInUser.class, request);
	    setBody(signIn(user), response);
	    response.setStatus(HttpStatus.OK_200);
	} else if (path.equals(SIGN_UP_PATH)) {
	    User user = getBody(User.class, request);
	    signUp(user);
	    response.setStatus(HttpStatus.CREATED_201);
	} else if (path.equals(REFRESH_TOKEN_PATH)) {
	    Token refreshToken = getBody(Token.class, request);
	    Token accessToken = refreshToken(refreshToken.getValue());
	    setBody(accessToken, response);
	    response.setStatus(HttpStatus.OK_200);
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
