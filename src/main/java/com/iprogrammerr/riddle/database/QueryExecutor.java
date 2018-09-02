package com.iprogrammerr.riddle.database;

public interface QueryExecutor<Id> {

    <T> T select(String query, QueryResultToObjectConverter<T> converter);
    
    Id create(String query);
    
    void update(String query);
    
    void delete(String query);
}
