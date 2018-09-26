package com.iprogrammerr.riddle.source;

import com.iprogrammerr.riddle.user.User;

public interface UserSource {
    User collect() throws Exception;
}
