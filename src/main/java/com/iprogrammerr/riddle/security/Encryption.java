package com.iprogrammerr.riddle.security;

import com.iprogrammerr.riddle.user.User;

public interface Encryption {

    String encrypted(User user) throws Exception;

    String hash(String base);

}
