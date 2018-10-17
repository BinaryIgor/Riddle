package com.iprogrammerr.riddle.user;

import com.iprogrammerr.riddle.model.KeyValue;
import com.iprogrammerr.riddle.model.TypedMap;

public interface User {

    long id();

    String name() throws Exception;

    String email() throws Exception;

    String password() throws Exception;

    boolean active() throws Exception;

    String role() throws Exception;

    void change(KeyValue column) throws Exception;

    void change(TypedMap columns) throws Exception;
}
