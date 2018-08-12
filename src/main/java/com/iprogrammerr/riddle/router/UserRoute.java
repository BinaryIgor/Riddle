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
    private String activatingLink;
    private ValidationService validationService;
    private UserService userService;
    private SecurityService securityService;
    private EmailService emailService;

    public UserRoute(String activatingLinkBase, UserService userService, ValidationService validationService,
	    SecurityService securityService, EmailService emailService, JsonService jsonService) {
	super("user", jsonService);
	this.activatingLink = activatingLinkBase + "/user/" + ACTIVATE_PATH;
	this.validationService = validationService;
	this.userService = userService;
	this.securityService = securityService;
	this.emailService = emailService;
    }

    private void signIn(HttpServletRequest request, HttpServletResponse response) {
	ToSignInUser toSignInUser = getBody(ToSignInUser.class, request);
	validationService.validateObject(ToSignInUser.class, toSignInUser);
	System.out.println(toSignInUser);
	User user = userService.getUserByNameOrEmail(toSignInUser.nameEmail);
	UserRole role = user.getUserRole();
	Token accessToken = securityService.createAccessToken(user.getName(), role.getName());
	Token refreshToken = securityService.createRefreshToken(user.getName(), role.getName());
	setBody(new SignInResponse(role.getName(), accessToken, refreshToken), response);
	response.setStatus(HttpStatus.OK_200);
    }

    private void signUp(HttpServletRequest request, HttpServletResponse response) {
	User user = getBody(User.class, request);
	System.out.println("User before creating " + user);
	validationService.validateEntity(User.class, user);
	UserRole userRole = userService.getUserRoleByName(UserRole.Role.PLAYER.getTranslation());
	user.setUserRole(userRole);
	long id = 1;// userService.create(user);
	System.out.println("User after creating " + user);
	ActivatingLink link = new ActivatingLink(activatingLink + "?id=" + id);
	emailService.sendSignUpEmail(user.getEmail(), link.getActivatingLink());
	setBody(activatingLink, response);
	response.setStatus(HttpStatus.CREATED_201);
    }

    private void refreshToken(HttpServletRequest request, HttpServletResponse response) {
	Token refreshToken = getBody(Token.class, request);
	Token accessToken = securityService.createAccessToken(refreshToken.getValue());
	setBody(accessToken, response);
	response.setStatus(HttpStatus.OK_200);
    }

    private void activateUser(HttpServletRequest request, HttpServletResponse response) {
	long id = getParameter(Long.class, "id", request);
	userService.activateUser(id);
	response.setStatus(HttpStatus.OK_200);
    }

    @Override
    public void resolveGetRequest(String path, HttpServletRequest request, HttpServletResponse response) {
	throw new NotResolvedRouteException();
    }

    @Override
    public void resolvePostRequest(String path, HttpServletRequest request, HttpServletResponse response) {
	if (path.equals(SIGN_IN_PATH)) {
	    signIn(request, response);
	} else if (path.equals(SIGN_UP_PATH)) {
	    signUp(request, response);
	} else if (path.equals(REFRESH_TOKEN_PATH)) {
	    refreshToken(request, response);
	} else if (path.equals(ACTIVATE_PATH)) {
	    activateUser(request, response);
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
