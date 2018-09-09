package com.iprogrammerr.riddle.database;

import java.sql.Connection;

public interface Database {

    Connection connect() throws Exception;

    void close();
}
