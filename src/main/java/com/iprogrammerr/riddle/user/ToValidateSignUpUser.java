package com.iprogrammerr.riddle.user;

public final class ToValidateSignUpUser implements ValidatableToSignUpUser {

    private final ToSignUpUser base;

    public ToValidateSignUpUser(ToSignUpUser base) {
	this.base = base;
    }

    @Override
    public String name() throws Exception {
	return this.base.name();
    }

    @Override
    public String email() throws Exception {
	return this.base.email();
    }

    @Override
    public String password() throws Exception {
	return this.base.password();
    }

    @Override
    public void validate() throws Exception {
	String name = this.base.name();
	if (name.length() < 3) {
	    throw new Exception("Name has to have at least 3 characters!");
	}
	String email = this.base.email();
	boolean validEmail = email.contains("@") && email.indexOf('.') > 0 && !email.endsWith(".")
		&& email.length() > 6;
	if (!validEmail) {
	    throw new Exception("Email has to have valid syntax and at least 6 characters");
	}
	String password = this.base.password();
	if (password.length() < 6) {
	    throw new Exception("Pasword has to have at least 6 characters");
	}
    }

}
