package com.iprogrammerr.riddle.user;

public interface ToSignInUser {

    String nameOrEmail() throws Exception;

    String password() throws Exception;
}
