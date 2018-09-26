package com.iprogrammerr.riddle.user;

public interface ToEditUser {

    String email() throws Exception;

    boolean hasEmail();

    String name() throws Exception;

    boolean hasName();

    String password() throws Exception;

    boolean hasPassword();
}
