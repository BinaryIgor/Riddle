package com.iprogrammerr.riddle.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public final class SqlDatabaseSession implements DatabaseSession {

    private final Database database;

    public SqlDatabaseSession(Database database) {
	this.database = database;
    }

    @Override
    public <T> T select(String sql, QueryResultMapping<T> mapping) throws Exception {
	try (Connection connection = this.database.connect()) {
	    Statement statement = connection.createStatement();
	    ResultSet resultSet = statement.executeQuery(sql);
	    resultSet.next();
	    return mapping.map(resultSet);
	}
    }

    @Override
    public long create(String sql) throws Exception {
	try (Connection connection = this.database.connect()) {
	    PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	    preparedStatement.executeUpdate();
	    ResultSet resultSet = preparedStatement.getGeneratedKeys();
	    resultSet.next();
	    return resultSet.getLong(1);
	}
    }

    @Override
    public void update(String sql) throws Exception {
	try (Connection connection = this.database.connect()) {
	    Statement statement = connection.createStatement();
	    statement.executeUpdate(sql);
	}
    }

    @Override
    public void delete(String sql) throws Exception {
	try (Connection connection = this.database.connect()) {
	    Statement statement = connection.createStatement();
	    statement.executeUpdate(sql);
	}
    }
}
