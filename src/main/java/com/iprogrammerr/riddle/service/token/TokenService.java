package com.iprogrammerr.riddle.service.token;

import com.iprogrammerr.riddle.model.Token;
import com.iprogrammerr.riddle.model.Token.TokenType;
import com.iprogrammerr.riddle.service.crud.UserService;

public class TokenService {

    private UserService userService;

    public TokenService(UserService userService) {
	this.userService = userService;
    }

    public Token createAccessToken(String username, String role) {
	return createToken(username, role, TokenType.ACCESS_TOKEN);
    }

    public Token createRefreshToken(String username, String role) {
	return createToken(username, role, TokenType.REFRESH_TOKEN);
    }

    private Token createToken(String username, String role, TokenType tokenType) {
	return null;
    }
}
