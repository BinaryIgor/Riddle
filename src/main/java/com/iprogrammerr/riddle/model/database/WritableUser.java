package com.iprogrammerr.riddle.model.database;

import com.iprogrammerr.bright.server.model.KeyValue;
import com.iprogrammerr.bright.server.model.KeysValues;

public interface WritableUser extends User {

    void update(KeysValues columns) throws Exception;

    void update(KeyValue column) throws Exception;
}
