package com.iprogrammerr.riddle.user;

public class ToValidateSignUpUser implements ValidatableToSignUpUser {

    private final ToSignUpUser base;

    public ToValidateSignUpUser(ToSignUpUser base) {
	this.base = base;
    }

    @Override
    public String name() throws Exception {
	return base.name();
    }

    @Override
    public String email() throws Exception {
	return base.email();
    }

    @Override
    public String password() throws Exception {
	return base.password();
    }

    @Override
    public void validate() throws Exception {
	String name = base.name();
	if (name.length() < 3) {
	    throw new Exception("Name has to have at least 3 characters!");
	}
	String email = base.email();
	boolean validEmail = email.contains("@") && email.indexOf('.') > 0 && !email.endsWith(".")
		&& email.length() > 6;
	if (!validEmail) {
	    throw new Exception("Email has to have valid syntax and at least 6 characters");
	}
	String password = base.password();
	if (password.length() < 6) {
	    throw new Exception("Pasword has to have at least 6 characters");
	}

    }

}
