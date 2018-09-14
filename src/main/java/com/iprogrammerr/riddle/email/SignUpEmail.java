package com.iprogrammerr.riddle.email;

public class SignUpEmail implements Email {

    private final String sender;
    private final String recipent;
    private final String activatingLink;

    public SignUpEmail(String sender, String recipent, String activatingLink) {
	this.sender = sender;
	this.recipent = recipent;
	this.activatingLink = activatingLink;
    }

    @Override
    public String sender() {
	return sender;
    }

    @Override
    public String recipent() {
	return recipent;
    }

    @Override
    public String subject() {
	return "Sign Up";
    }

    @Override
    public String text() {
	return "Congratulations, You have signed up! To activate your account click link: " + activatingLink;
    }

}
