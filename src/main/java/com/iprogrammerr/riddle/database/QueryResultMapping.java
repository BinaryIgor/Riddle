package com.iprogrammerr.riddle.database;

import java.sql.ResultSet;

public interface QueryResultMapping<T> {

    T map(ResultSet resultSet) throws Exception;
}
