package com.iprogrammerr.riddle.database;

import java.sql.Connection;

public interface Database {

    Connection connection() throws Exception;

    void close();
}
