package com.iprogrammerr.riddle.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface QueryToObjectConverter<T> {

    T convert(ResultSet resultSet) throws SQLException;

}
