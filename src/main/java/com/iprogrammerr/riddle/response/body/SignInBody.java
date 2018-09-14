package com.iprogrammerr.riddle.response.body;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.iprogrammerr.riddle.security.token.Token;

public class SignInBody implements JsonBody {

    private String role;
    private Token accessToken;
    private Token refreshToken;

    public SignInBody(String role, Token accessToken, Token refreshToken) {
	this.role = role;
	this.accessToken = accessToken;
	this.refreshToken = refreshToken;
    }

    @Override
    public String content() {
	JSONObject jsonObject = new JSONObject();
	jsonObject.put("role", role);
	Map<String, Object> token = new HashMap<>();
	token.put("value", accessToken.value());
	token.put("expirationDate", accessToken.expirationDate());
	jsonObject.put("accessToken", token);
	token.put("value", refreshToken.value());
	token.put("expirationDate", refreshToken.expirationDate());
	jsonObject.put("refreshToken", token);
	return jsonObject.toString();
    }

}
