package com.iprogrammerr.riddle.service.security;

import java.util.Date;

import com.iprogrammerr.riddle.configuration.SecurityConfiguration;
import com.iprogrammerr.riddle.configuration.SecurityConfiguration.TokenType;
import com.iprogrammerr.riddle.exception.validation.TokenParsingException;
import com.iprogrammerr.riddle.model.security.TokenData;
import com.iprogrammerr.riddle.service.crud.UserService;
import com.iprogrammerr.riddle.user.User;
import com.iprogrammerr.riddle.user.UsersRoles;
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
	UsersRoles role = user.getUserRole();
	if (!tokenData.getRole().equals(role.getName())) {
	    throw new TokenParsingException("Wrong user role.");
	}
	return createToken(user.getName(), role.getName(), TokenType.ACCESS_TOKEN);
    }

    public String getUsernameFromToken(String token) {
	Claims claims = Jwts.parser().setSigningKey(SecurityConfiguration.SECRET.getBytes()).parseClaimsJws(token)
		.getBody();
	String username = claims.getSubject();
	if (username == null) {
	    throw new TokenParsingException();
	}
	return username;
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

    public boolean validateToken(String token) {
	Claims claims = Jwts.parser().setSigningKey(SecurityConfiguration.SECRET.getBytes()).parseClaimsJws(token)
		.getBody();
	String username = claims.getSubject();
	if (StringUtil.isNullOrEmpty(username)) {
	    return false;
	}
	String role = claims.get(SecurityConfiguration.TOKEN_ROLE_CLAIM, String.class);
	if (StringUtil.isNullOrEmpty(role)) {
	    return false;
	}
	String tokenType = claims.get(SecurityConfiguration.TOKEN_TYPE_KEY, String.class);
	if (StringUtil.isNullOrEmpty(tokenType)) {
	    return false;
	}
	return true;
    }

}
