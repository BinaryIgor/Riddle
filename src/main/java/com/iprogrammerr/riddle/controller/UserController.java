package com.iprogrammerr.riddle.controller;

import java.util.ArrayList;
import java.util.List;

import com.iprogrammerr.bright.server.constants.ResponseCode;
import com.iprogrammerr.bright.server.method.RequestMethod;
import com.iprogrammerr.bright.server.pattern.UrlPattern;
import com.iprogrammerr.bright.server.respondent.HttpRespondent;
import com.iprogrammerr.bright.server.response.Response;
import com.iprogrammerr.riddle.configuration.SecurityConfiguration;
import com.iprogrammerr.riddle.exception.database.NoResultException;
import com.iprogrammerr.riddle.exception.http.RequestParameterException;
import com.iprogrammerr.riddle.exception.security.UnauthenticatedException;
import com.iprogrammerr.riddle.exception.security.UnauthorizedException;
import com.iprogrammerr.riddle.exception.validation.TokenParsingException;
import com.iprogrammerr.riddle.model.UserProfile;
import com.iprogrammerr.riddle.model.Username;
import com.iprogrammerr.riddle.model.security.Activator;
import com.iprogrammerr.riddle.service.crud.UserService;
import com.iprogrammerr.riddle.service.email.EmailService;
import com.iprogrammerr.riddle.service.json.JsonService;
import com.iprogrammerr.riddle.service.security.EncryptionService;
import com.iprogrammerr.riddle.service.security.SecurityService;
import com.iprogrammerr.riddle.service.validation.ValidationService;
import com.iprogrammerr.riddle.user.User;

public class UserController implements Controller {

    private static final String MAIN_PATH = "user";
    private String activationLinkBase;
    private ValidationService validationService;
    private UserService userService;
    private SecurityService securityService;
    private EncryptionService encryptionService;
    private EmailService emailService;
    private JsonService jsonService;
    private List<HttpRespondent> requestResolvers;

    public UserController(String activationLinkBase, UrlPattern urlPatternParser, UserService userService,
	    ValidationService validationService, SecurityService securityService, EncryptionService encryptionService,
	    EmailService emailService, JsonService jsonService) {
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

    private void createRequestResolvers(UrlPattern urlPatternParser) {
	requestResolvers
		.add(new HttpRespondent(MAIN_PATH + "/sign-in", RequestMethod.POST, urlPatternParser, this::signIn));
	requestResolvers.add(new HttpRespondent(MAIN_PATH + "/sign-up", RequestMethod.POST, this::signUp));
	requestResolvers.add(new HttpRespondent(MAIN_PATH + "/token-refresh", RequestMethod.POST, this::refreshToken));
	requestResolvers.add(new HttpRespondent(MAIN_PATH + "/activate", RequestMethod.POST, this::activateUser));
	requestResolvers.add(new HttpRespondent(MAIN_PATH + "/profile/{id:int}", RequestMethod.GET, this::getProfile));
	requestResolvers.add(new HttpRespondent(MAIN_PATH + "/profile", RequestMethod.GET, this::getProfileByToken));
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
    public List<HttpRespondent> createRequestResolvers() {
	return requestResolvers;
    }

}
