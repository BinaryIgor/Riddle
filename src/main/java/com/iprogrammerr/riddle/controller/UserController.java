package com.iprogrammerr.riddle.controller;

import java.util.ArrayList;
import java.util.List;

import com.iprogrammerr.bright.server.constants.RequestMethod;
import com.iprogrammerr.bright.server.constants.ResponseCode;
import com.iprogrammerr.bright.server.model.ResolvedRequest;
import com.iprogrammerr.bright.server.parser.UrlPatternParser;
import com.iprogrammerr.bright.server.resolver.RequestResolver;
import com.iprogrammerr.bright.server.response.EmptyResponse;
import com.iprogrammerr.bright.server.response.JsonResponse;
import com.iprogrammerr.bright.server.response.Response;
import com.iprogrammerr.riddle.configuration.SecurityConfiguration;
import com.iprogrammerr.riddle.exception.database.NoResultException;
import com.iprogrammerr.riddle.exception.http.DuplicateEntryHttpException;
import com.iprogrammerr.riddle.exception.http.RequestParameterException;
import com.iprogrammerr.riddle.exception.security.UnauthenticatedException;
import com.iprogrammerr.riddle.exception.security.UnauthorizedException;
import com.iprogrammerr.riddle.exception.validation.TokenParsingException;
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

public class UserController implements Controller {

    private static final String MAIN_PATH = "user";
    private String activationLinkBase;
    private ValidationService validationService;
    private UserService userService;
    private SecurityService securityService;
    private EncryptionService encryptionService;
    private EmailService emailService;
    private JsonService jsonService;
    private List<RequestResolver> requestResolvers;

    public UserController(String activationLinkBase, UrlPatternParser urlPatternParser, UserService userService, ValidationService validationService,
	    SecurityService securityService, EncryptionService encryptionService, EmailService emailService,
	    JsonService jsonService) {
	this.activationLinkBase = activationLinkBase;
	this.validationService = validationService;
	this.userService = userService;
	this.securityService = securityService;
	this.encryptionService = encryptionService;
	this.emailService = emailService;
	this.jsonService = jsonService;
	this.requestResolvers = new ArrayList<>();
	createRequestResolvers(urlPatternParser);
    }

    private void createRequestResolvers(UrlPatternParser urlPatternParser) {
	requestResolvers.add(new RequestResolver(MAIN_PATH + "/sign-in", RequestMethod.POST, urlPatternParser,this::signIn));
	requestResolvers.add(new RequestResolver(MAIN_PATH + "/sign-up", RequestMethod.POST, this::signUp));
	requestResolvers.add(new RequestResolver(MAIN_PATH + "/token-refresh", RequestMethod.POST, this::refreshToken));
	requestResolvers.add(new RequestResolver(MAIN_PATH + "/activate", RequestMethod.POST, this::activateUser));
	requestResolvers.add(new RequestResolver(MAIN_PATH + "/profile/{id:int}", RequestMethod.GET, this::getProfile));
	requestResolvers.add(new RequestResolver(MAIN_PATH + "/profile", RequestMethod.GET, this::getProfileByToken));
    }

    private Response signIn(ResolvedRequest request) {
	ToSignInUser toSignInUser = jsonService.deserialize(ToSignInUser.class, request.getBody());
	try {
	    User user = userService.getUserByNameOrEmail(toSignInUser.getNameEmail());
	    String encryptedPassword = encryptionService.encrypt(toSignInUser.getPassword());
	    if (!encryptedPassword.equals(user.getPassword())) {
		throw new UnauthenticatedException("Password is invalid");
	    }
	    if (!user.isActive()) {
		throw new UnauthenticatedException("User needs to be activated first");
	    }
	    UserRole role = user.getUserRole();
	    Token accessToken = securityService.createAccessToken(user.getName(), role.getName());
	    Token refreshToken = securityService.createRefreshToken(user.getName(), role.getName());
	    String json = jsonService.serialize(new SignInResponse(role.getName(), accessToken, refreshToken));
	    return new JsonResponse(ResponseCode.OK, json);
	} catch (NoResultException exception) {
	    exception.printStackTrace();
	    return new EmptyResponse(ResponseCode.NO_CONTENT);
	}
    }

    private void signUp(Request request, Response response) {
	ToSignUpUser toSignUpUser = jsonService.deserialize(ToSignUpUser.class, request);
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
	Token refreshToken = jsonService.deserialize(Token.class, request);
	Token accessToken = securityService.createAccessToken(refreshToken.getValue());
	String jsonResponse = jsonService.serialize(accessToken);
	response.setJsonBody(jsonResponse);
	response.setCode(ResponseCode.OK);
    }

    private void activateUser(Request request, Response response) {
	Activator activator = jsonService.deserialize(Activator.class, request);
	User user = userService.getUser(activator.getId());
	String userHash = encryptionService.getToSendUserHash(user);
	if (!userHash.equals(activator.getHash())) {
	    throw new UnauthorizedException("Wrong activating hash.");
	}
	userService.activateUser(user.getId());
	String jsonResponse = jsonService.serialize(new Username(user.getName()));
	response.setJsonBody(jsonResponse);
	response.setCode(ResponseCode.OK);
    }

    private void getProfile(Request request, Response response) {
	long id = request.getPathVariable("id", Long.class);
	if (id < 1) {
	    throw RequestParameterException.createPositiveNumberRequiredException();
	}
	try {
	    UserProfile userProfile = userService.getUserProfile(id);
	    String jsonResponse = jsonService.serialize(userProfile);
	    response.setJsonBody(jsonResponse);
	    response.setCode(ResponseCode.OK);
	} catch (NoResultException exception) {
	    exception.printStackTrace();
	    response.setCode(ResponseCode.NO_CONTENT);
	}
    }

    private void getProfileByToken(Request request, Response response) {
	try {
	    String token = request.getHeader(RequestHeaderKey.AUTHORIZATION)
		    .replaceAll(SecurityConfiguration.TOKEN_PREFIX, "");
	    String username = securityService.getUsernameFromToken(token);
	    UserProfile userProfile = userService.getUserProfile(username);
	    String jsonResponse = jsonService.serialize(userProfile);
	    response.setJsonBody(jsonResponse);
	    response.setCode(ResponseCode.OK);
	} catch (TokenParsingException exception) {
	    exception.printStackTrace();
	    throw new UnauthenticatedException();
	} catch (NoResultException exception) {
	    exception.printStackTrace();
	    response.setCode(ResponseCode.NO_CONTENT);
	}
    }

    @Override
    public List<RequestResolver> createRequestResolvers() {
	return requestResolvers;
    }

}
