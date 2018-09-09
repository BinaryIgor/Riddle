package com.iprogrammerr.riddle.database;

import com.iprogrammerr.riddle.model.database.Table;

public interface QueryTemplate {

    String query(String template, Object... values) throws Exception;

    String insert(Table table) throws Exception;

    String update(Table table, String whereTemplate, Object... values) throws Exception;
}
