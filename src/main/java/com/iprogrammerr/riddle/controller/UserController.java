package com.iprogrammerr.riddle.controller;

import java.util.ArrayList;
import java.util.List;

import com.iprogrammerr.riddle.exception.database.NoResultException;
import com.iprogrammerr.riddle.exception.http.DuplicateEntryHttpException;
import com.iprogrammerr.riddle.exception.http.RequestParameterException;
import com.iprogrammerr.riddle.exception.security.UnauthenticatedException;
import com.iprogrammerr.riddle.exception.security.UnauthorizedException;
import com.iprogrammerr.riddle.model.database.User;
import com.iprogrammerr.riddle.model.database.UserRole;
import com.iprogrammerr.riddle.model.json.ToSignInUser;
import com.iprogrammerr.riddle.model.json.ToSignUpUser;
import com.iprogrammerr.riddle.model.json.UserProfile;
import com.iprogrammerr.riddle.model.json.Username;
import com.iprogrammerr.riddle.model.response.SignInResponse;
import com.iprogrammerr.riddle.model.security.Activator;
import com.iprogrammerr.riddle.model.security.Token;
import com.iprogrammerr.riddle.service.crud.UserService;
import com.iprogrammerr.riddle.service.email.EmailService;
import com.iprogrammerr.riddle.service.json.JsonService;
import com.iprogrammerr.riddle.service.security.EncryptionService;
import com.iprogrammerr.riddle.service.security.SecurityService;
import com.iprogrammerr.riddle.service.validation.ValidationService;
import com.iprogrammerr.simple.http.server.constants.RequestMethod;
import com.iprogrammerr.simple.http.server.constants.ResponseCode;
import com.iprogrammerr.simple.http.server.controller.Controller;
import com.iprogrammerr.simple.http.server.model.Request;
import com.iprogrammerr.simple.http.server.model.Response;
import com.iprogrammerr.simple.http.server.resolver.RequestResolver;

public class UserController extends JsonController implements Controller {

    private static final String MAIN_PATH = "user";
    private String activationLinkBase;
    private ValidationService validationService;
    private UserService userService;
    private SecurityService securityService;
    private EncryptionService encryptionService;
    private EmailService emailService;
    private final List<RequestResolver> requestResolvers;

    public UserController(String activationLinkBase, UserService userService, ValidationService validationService,
	    SecurityService securityService, EncryptionService encryptionService, EmailService emailService,
	    JsonService jsonService) {
	super(jsonService);
	this.activationLinkBase = activationLinkBase;
	this.validationService = validationService;
	this.userService = userService;
	this.securityService = securityService;
	this.encryptionService = encryptionService;
	this.emailService = emailService;
	this.requestResolvers = new ArrayList<>();
	createRequestResolvers();
    }

    private void createRequestResolvers() {
	requestResolvers.add(new RequestResolver(MAIN_PATH + "/sign-in", RequestMethod.POST, this::signIn));
	requestResolvers.add(new RequestResolver(MAIN_PATH + "/sign-up", RequestMethod.POST, this::signUp));
	requestResolvers.add(new RequestResolver(MAIN_PATH + "/token-refresh", RequestMethod.POST, this::refreshToken));
	requestResolvers.add(new RequestResolver(MAIN_PATH + "/activate", RequestMethod.POST, this::activateUser));
	requestResolvers.add(new RequestResolver(MAIN_PATH + "/profile/{id:int}", RequestMethod.GET, this::getProfile));
    }

    private void signIn(Request request, Response response) {
	ToSignInUser toSignInUser = getBody(ToSignInUser.class, request);
	try {
	    User user = userService.getUserByNameOrEmail(toSignInUser.getNameEmail());
	    String encryptedPassword = encryptionService.encrypt(toSignInUser.getPassword());
	    if (!encryptedPassword.equals(user.getPassword())) {
		throw UnauthenticatedException.createInvalidPasswordException();
	    }
	    if (!user.isActive()) {
		throw UnauthenticatedException.createNotActivatedUserException();
	    }
	    UserRole role = user.getUserRole();
	    Token accessToken = securityService.createAccessToken(user.getName(), role.getName());
	    Token refreshToken = securityService.createRefreshToken(user.getName(), role.getName());
	    setBody(new SignInResponse(role.getName(), accessToken, refreshToken), response);
	    response.setCode(ResponseCode.OK);
	} catch (NoResultException exception) {
	    exception.printStackTrace();
	    response.setCode(ResponseCode.NO_CONTENT);
	}
    }

    private void signUp(Request request, Response response) {
	ToSignUpUser toSignUpUser = getBody(ToSignUpUser.class, request);
	if (userService.existsByEmail(toSignUpUser.getEmail())) {
	    throw new DuplicateEntryHttpException("Given email is already taken.");
	}
	if (userService.existsByName(toSignUpUser.getName())) {
	    throw new DuplicateEntryHttpException("Given name is already taken.");
	}
	User user = new User(toSignUpUser.getEmail(), toSignUpUser.getName(), toSignUpUser.getPassword());
	validationService.validateUser(user);
	UserRole userRole = userService.getUserRoleByName(UserRole.Role.PLAYER.value);
	user.setPassword(encryptionService.encrypt(toSignUpUser.getPassword()));
	user.setUserRole(userRole);
	long id = userService.createUser(user);
	String userHash = encryptionService.getToSendUserHash(user);
	String activatingLink = activationLinkBase + "?id=" + id + "&activate=" + userHash;
	emailService.sendSignUpEmail(toSignUpUser.getEmail(), activatingLink);
	response.setCode(ResponseCode.CREATED);
    }

    private void refreshToken(Request request, Response response) {
	Token refreshToken = getBody(Token.class, request);
	Token accessToken = securityService.createAccessToken(refreshToken.getValue());
	setBody(accessToken, response);
	response.setCode(ResponseCode.OK);
    }

    private void activateUser(Request request, Response response) {
	Activator activator = getBody(Activator.class, request);
	User user = userService.getUser(activator.getId());
	String userHash = encryptionService.getToSendUserHash(user);
	if (!userHash.equals(activator.getHash())) {
	    throw new UnauthorizedException("Wrong activating hash.");
	}
	userService.activateUser(user.getId());
	setBody(new Username(user.getName()), response);
	response.setCode(ResponseCode.OK);
    }

    private void getProfile(Request request, Response response) {
	long id = request.getPathVariable("id", Long.class);
	if (id < 1) {
	    throw RequestParameterException.createPositiveNumberRequiredException();
	}
	try {
	    UserProfile userProfile = userService.getUserProfile(id);
	    setBody(userProfile, response);
	    response.setCode(ResponseCode.OK);
	} catch (NoResultException exception) {
	    exception.printStackTrace();
	    response.setCode(ResponseCode.NO_CONTENT);
	}
    }

    @Override
    public List<RequestResolver> getRequestResolvers() {
	return requestResolvers;
    }

}
