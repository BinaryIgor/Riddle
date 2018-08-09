package com.iprogrammerr.riddle.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ToSignInUser {

    @NotNull
    @Size(min = 5, max = 20)
    public String nameEmail;

    @NotNull
    @Size(min = 3, max = 50)
    public String password;

    @Override
    public String toString() {
	return "ToSignInUser [nameEmail=" + nameEmail + ", password=" + password + "]";
    }

}
