package com.iprogrammerr.riddle.router.security;

import java.util.ArrayList;
import java.util.List;

import com.iprogrammerr.riddle.model.security.SecuredPath;

public class SecurityConfiguration {

    public static final String SECRET = "SecGeneRetRator";
    public static final long ACCES_TOKEN_EXPIRATION_TIME = 3_600_000L;
    public static final long REFRESH_TOKEN_EXPIRATION_TIME = 86_400_000L;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_ROLE_CLAIM = "role";
    public static final String TOKEN_TYPE_KEY = "tokenType";
    public static final List<SecuredPath> SECURED_PATHS = new ArrayList<>();
    static {
	// SECURED_PATHS.add(new SecuredPath(UserRole.Role.PLAYER.value,
	// "/user/profile"));
    }

    public enum TokenType {
	ACCESS_TOKEN("accessToken"), REFRESH_TOKEN("refreshToken");

	private String value;

	TokenType(String value) {
	    this.value = value;
	}

	public String getValue() {
	    return value;
	}

	public static boolean isAccessToken(String tokenType) {
	    return ACCESS_TOKEN.value.equals(tokenType);
	}
    }
}
