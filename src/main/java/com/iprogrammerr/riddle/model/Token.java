package com.iprogrammerr.riddle.model;

public class Token {

    private String value;
    private long expirationDate;

    public String getValue() {
	return value;
    }

    public long getExpirationDate() {
	return expirationDate;
    }

    public void setValue(String value) {
	this.value = value;
    }

    public void setExpirationDate(long expirationDate) {
	this.expirationDate = expirationDate;
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
