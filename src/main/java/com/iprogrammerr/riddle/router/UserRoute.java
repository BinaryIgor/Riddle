package com.iprogrammerr.riddle.router;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;

import com.iprogrammerr.riddle.entity.User;
import com.iprogrammerr.riddle.entity.UserRole;
import com.iprogrammerr.riddle.exception.NotResolvedRouteException;
import com.iprogrammerr.riddle.model.ActivatingLink;
import com.iprogrammerr.riddle.model.SignInResponse;
import com.iprogrammerr.riddle.model.ToSignInUser;
import com.iprogrammerr.riddle.model.Token;
import com.iprogrammerr.riddle.service.crud.UserService;
import com.iprogrammerr.riddle.service.email.EmailService;
import com.iprogrammerr.riddle.service.json.JsonService;
import com.iprogrammerr.riddle.service.security.SecurityService;
import com.iprogrammerr.riddle.service.validation.ValidationService;

public class UserRoute extends Route {

    private static final String SIGN_IN_PATH = "sign-in";
    private static final String SIGN_UP_PATH = "sign-up";
    private static final String REFRESH_TOKEN_PATH = "token-refresh";
    private static final String ACTIVATE_PATH = "activate";
    // TODO think about solution
    private static final String ACTIVATE_LINK_BASE = "http://www.iprogrammerr.com:9000/riddle/user/" + ACTIVATE_PATH;
    private ValidationService validationService;
    private UserService userService;
    private SecurityService securityService;
    private EmailService emailService;

    public UserRoute(UserService userService, ValidationService validationService, SecurityService securityService,
	    EmailService emailService, JsonService jsonService) {
	super("user", jsonService);
	this.validationService = validationService;
	this.userService = userService;
	this.securityService = securityService;
	this.emailService = emailService;
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

    private ActivatingLink signUp(User user) {
	validationService.validateEntity(User.class, user);
	UserRole userRole = userService.getUserRoleByName(UserRole.Role.PLAYER.getTranslation());
	user.setUserRole(userRole);
	long id = userService.create(user);
	System.out.println(user);
	String activatingLink = ACTIVATE_LINK_BASE + "?id=" + id;
	return new ActivatingLink(activatingLink);
    }

    private Token refreshToken(String refreshToken) {
	return securityService.createAccessToken(refreshToken);
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
	    ActivatingLink activatingLink = signUp(user);
	    setBody(activatingLink, response);
	    response.setStatus(HttpStatus.CREATED_201);
	} else if (path.equals(REFRESH_TOKEN_PATH)) {
	    Token refreshToken = getBody(Token.class, request);
	    Token accessToken = refreshToken(refreshToken.getValue());
	    setBody(accessToken, response);
	    response.setStatus(HttpStatus.OK_200);
	} else if (path.equals(ACTIVATE_PATH)) {
	    long id = getParameter(Long.class, "id", request);
	    userService.activateUser(id);
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
