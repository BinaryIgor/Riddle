package com.iprogrammerr.riddle.database;

import com.iprogrammerr.riddle.model.Record;

public interface QueryTemplate {

    String query(String template, Object... values) throws Exception;

    String insertQuery(Record table) throws Exception;

    String updateQuery(Record table, String whereTemplate, Object... values) throws Exception;
}
