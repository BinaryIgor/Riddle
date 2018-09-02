package com.iprogrammerr.riddle.database;

public interface QueryCreator {

    String create(String template, Object...values);
}
