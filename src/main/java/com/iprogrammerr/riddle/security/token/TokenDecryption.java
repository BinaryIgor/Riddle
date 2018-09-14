package com.iprogrammerr.riddle.security.token;

import com.iprogrammerr.bright.server.model.KeysValues;

public interface TokenDecryption {

    String subject() throws Exception;

    KeysValues additional() throws Exception;
}
