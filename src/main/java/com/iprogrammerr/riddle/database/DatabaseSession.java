package com.iprogrammerr.riddle.database;

public interface DatabaseSession {

    <T> T select(String query, QueryResultMapping<T> mapping) throws Exception;

    long create(String query) throws Exception;

    void update(String query) throws Exception;

    void delete(String query) throws Exception;
}
