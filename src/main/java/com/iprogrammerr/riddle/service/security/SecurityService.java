package com.iprogrammerr.riddle.service.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.iprogrammerr.riddle.entity.User;
import com.iprogrammerr.riddle.entity.UserRole;
import com.iprogrammerr.riddle.exception.TokenParsingException;
import com.iprogrammerr.riddle.exception.UnauthenticatedException;
import com.iprogrammerr.riddle.exception.UnauthorizedException;
import com.iprogrammerr.riddle.model.SecuredPath;
import com.iprogrammerr.riddle.model.Token;
import com.iprogrammerr.riddle.model.TokenData;
import com.iprogrammerr.riddle.router.security.SecurityConfiguration;
import com.iprogrammerr.riddle.router.security.SecurityConfiguration.TokenType;
import com.iprogrammerr.riddle.service.crud.UserService;
import com.iprogrammerr.riddle.util.StringUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class SecurityService {

    private UserService userService;

    public SecurityService(UserService userService) {
	this.userService = userService;
    }

    public Token createAccessToken(String username, String role) {
	return createToken(username, role, TokenType.ACCESS_TOKEN);
    }

    public Token createRefreshToken(String username, String role) {
	return createToken(username, role, TokenType.REFRESH_TOKEN);
    }

    private Token createToken(String username, String role, TokenType tokenType) {
	long expirationDate = System.currentTimeMillis();
	if (TokenType.ACCESS_TOKEN.equals(tokenType)) {
	    expirationDate += SecurityConfiguration.ACCES_TOKEN_EXPIRATION_TIME;
	} else {
	    expirationDate += SecurityConfiguration.REFRESH_TOKEN_EXPIRATION_TIME;
	}
	String token = Jwts.builder().setSubject(username)
		.claim(SecurityConfiguration.TOKEN_TYPE_KEY, tokenType.getValue())
		.setExpiration(new Date(expirationDate))
		.signWith(SignatureAlgorithm.HS512, SecurityConfiguration.SECRET.getBytes()).compact();
	return new Token(token, expirationDate);
    }

    public Token createAccessToken(String refreshToken) {
	TokenData tokenData = parseToken(refreshToken);
	if (TokenType.isAccessToken(tokenData.getTokenType())) {
	    throw new TokenParsingException("Wrong token type");
	}
	User user = userService.getUserByName(tokenData.getUsername());
	UserRole role = user.getUserRole();
	if (!tokenData.getRole().equals(role.getName())) {
	    throw new TokenParsingException("Wrong user role.");
	}
	return createToken(user.getName(), role.getName(), TokenType.ACCESS_TOKEN);
    }

    private TokenData parseToken(String token) {
	Claims claims = Jwts.parser().setSigningKey(SecurityConfiguration.SECRET.getBytes()).parseClaimsJws(token)
		.getBody();
	String username = claims.getSubject();
	String role = claims.get(SecurityConfiguration.TOKEN_ROLE_CLAIM, String.class);
	String tokenType = claims.get(SecurityConfiguration.TOKEN_TYPE_KEY, String.class);
	boolean validToken = !StringUtil.isNullOrEmpty(username) && !StringUtil.isNullOrEmpty(role)
		&& !StringUtil.isNullOrEmpty(tokenType);
	if (!validToken) {
	    throw new TokenParsingException();
	}
	return new TokenData(username, role, tokenType);
    }

    public void check(HttpServletRequest request) {
	SecuredPath securedPath = getCurrentPath(request.getRequestURI());
	if (securedPath == null) {
	    return;
	}
	String token = request.getHeader(SecurityConfiguration.AUTHORIZATION_HEADER);
	if (token == null || token.isEmpty()) {
	    throw new UnauthenticatedException();
	}
	token = token.replace(SecurityConfiguration.TOKEN_PREFIX, "");
	TokenData tokenData = parseToken(token);
	User user = userService.getUserByName(tokenData.getUsername());
	if (!user.getUserRole().getName().equals(tokenData.getRole())) {
	    throw new UnauthorizedException();
	}
    }

    private SecuredPath getCurrentPath(String requestUrl) {
	for (SecuredPath path : SecurityConfiguration.SECURED_PATHS) {
	    if (requestUrl.contains(path.getPath())) {
		return path;
	    }
	}
	return null;
    }

}
