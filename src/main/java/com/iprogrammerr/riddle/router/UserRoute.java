package com.iprogrammerr.riddle.router;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;

import com.iprogrammerr.riddle.entity.User;
import com.iprogrammerr.riddle.entity.UserRole;
import com.iprogrammerr.riddle.exception.crud.DuplicateEntryException;
import com.iprogrammerr.riddle.exception.router.NotResolvedRouteException;
import com.iprogrammerr.riddle.exception.security.UnauthenticatedException;
import com.iprogrammerr.riddle.exception.security.UnauthorizedException;
import com.iprogrammerr.riddle.model.ToSignInUser;
import com.iprogrammerr.riddle.model.Username;
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

    private void signIn(HttpServletRequest request, HttpServletResponse response) {
	ToSignInUser toSignInUser = getBody(ToSignInUser.class, request);
	validationService.validateObject(ToSignInUser.class, toSignInUser);
	User user = userService.getUserByNameOrEmail(toSignInUser.nameEmail);
	String encryptedPassword = encryptionService.encrypt(toSignInUser.password);
	if (!encryptedPassword.equals(user.getPassword())) {
	    throw new UnauthenticatedException("Invalid password.");
	}
	UserRole role = user.getUserRole();
	Token accessToken = securityService.createAccessToken(user.getName(), role.getName());
	Token refreshToken = securityService.createRefreshToken(user.getName(), role.getName());
	setBody(new SignInResponse(role.getName(), accessToken, refreshToken), response);
	response.setStatus(HttpStatus.OK_200);
    }

    private void signUp(HttpServletRequest request, HttpServletResponse response) {
	User user = getBody(User.class, request);
	validationService.validateEntity(User.class, user);
	String encryptedPassword = encryptionService.encrypt(user.getPassword());
	user.setPassword(encryptedPassword);
	UserRole userRole = userService.getUserRoleByName(UserRole.Role.PLAYER.getTranslation());
	user.setUserRole(userRole);
	if (userService.existsByEmail(user.getEmail())) {
	    throw new DuplicateEntryException("Given email is already taken.");
	}
	if (userService.existsByName(user.getName())) {
	    throw new DuplicateEntryException("Given name is already taken.");
	}
	long id = userService.create(user);
	String userHash = encryptionService.getToSendUserHash(user);
	String activatingLink = activationLinkBase + "?id=" + id + "&activate=" + userHash;
	emailService.sendSignUpEmail(user.getEmail(), activatingLink);
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
	validationService.validateObject(Activator.class, activator);
	User user = userService.get(activator.id);
	String userHash = encryptionService.getToSendUserHash(user);
	if (!userHash.equals(activator.hash)) {
	    throw new UnauthorizedException("Wrong activating hash.");
	}
	user.setActive(true);
	userService.update(user);
	setBody(new Username(user.getName()), response);
	response.setStatus(HttpStatus.OK_200);
    }

    @Override
    public void resolveGetRequest(String path, HttpServletRequest request, HttpServletResponse response) {
	if (path.equals(PROFILE_PATH)) {

	} else {
	    throw new NotResolvedRouteException();
	}
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

}
