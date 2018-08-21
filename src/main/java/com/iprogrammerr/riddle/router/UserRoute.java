package com.iprogrammerr.riddle.router;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;

import com.iprogrammerr.riddle.exception.database.DuplicateEntryException;
import com.iprogrammerr.riddle.exception.request.RequestParameterException;
import com.iprogrammerr.riddle.exception.router.NotResolvedRouteException;
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

public class UserRoute extends Route {

    private static final String SIGN_IN_PATH = "sign-in";
    private static final String SIGN_UP_PATH = "sign-up";
    private static final String REFRESH_TOKEN_PATH = "token-refresh";
    private static final String ACTIVATE_PATH = "activate";
    private static final String PROFILE_PATH = "profile";
    private String activationLinkBase;
    private ValidationService validationService;
    private UserService userService;
    private SecurityService securityService;
    private EncryptionService encryptionService;
    private EmailService emailService;

    public UserRoute(String activationLinkBase, UserService userService, ValidationService validationService,
	    SecurityService securityService, EncryptionService cryptographyService, EmailService emailService,
	    JsonService jsonService) {
	super("user", jsonService);
	this.activationLinkBase = activationLinkBase;
	this.validationService = validationService;
	this.userService = userService;
	this.securityService = securityService;
	this.encryptionService = cryptographyService;
	this.emailService = emailService;
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

    private void signIn(HttpServletRequest request, HttpServletResponse response) {
	ToSignInUser toSignInUser = getBody(ToSignInUser.class, request);
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
	response.setStatus(HttpStatus.OK_200);
    }

    private void signUp(HttpServletRequest request, HttpServletResponse response) {
	ToSignUpUser toSignUpUser = getBody(ToSignUpUser.class, request);
	if (userService.existsByEmail(toSignUpUser.getEmail())) {
	    throw new DuplicateEntryException("Given email is already taken.");
	}
	if (userService.existsByName(toSignUpUser.getName())) {
	    throw new DuplicateEntryException("Given name is already taken.");
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
	response.setStatus(HttpStatus.CREATED_201);
    }

    private void refreshToken(HttpServletRequest request, HttpServletResponse response) {
	Token refreshToken = getBody(Token.class, request);
	Token accessToken = securityService.createAccessToken(refreshToken.getValue());
	setBody(accessToken, response);
	response.setStatus(HttpStatus.OK_200);
    }

    private void activateUser(HttpServletRequest request, HttpServletResponse response) {
	Activator activator = getBody(Activator.class, request);
	User user = userService.getUser(activator.getId());
	String userHash = encryptionService.getToSendUserHash(user);
	if (!userHash.equals(activator.getHash())) {
	    throw new UnauthorizedException("Wrong activating hash.");
	}
	userService.activateUser(user.getId());
	setBody(new Username(user.getName()), response);
	response.setStatus(HttpStatus.OK_200);
    }

    @Override
    public void resolveGetRequest(String path, HttpServletRequest request, HttpServletResponse response) {
	if (path.equals(PROFILE_PATH)) {
	    getProfile(request, response);
	} else {
	    throw new NotResolvedRouteException();
	}
    }

    private void getProfile(HttpServletRequest request, HttpServletResponse response) {
	long id = getUrlLastVariable(Long.class, request.getRequestURI());
	if (id < 1) {
	    throw RequestParameterException.createPositiveNumberRequiredException();
	}
	UserProfile userProfile = userService.getUserProfile(id);
	setBody(userProfile, response);
	response.setStatus(HttpStatus.OK_200);
    }

}
