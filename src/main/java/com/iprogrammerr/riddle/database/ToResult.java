package com.iprogrammerr.riddle.database;

import java.sql.ResultSet;

public interface ToResult {

    Iterable<QueryResultMapping> result(ResultSet resultSet) throws Exception;
}
