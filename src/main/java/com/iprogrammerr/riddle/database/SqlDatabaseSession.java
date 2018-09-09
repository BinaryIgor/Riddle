package com.iprogrammerr.riddle.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class SqlDatabaseSession implements DatabaseSession {

    private Database database;

    public SqlDatabaseSession(Database database) {
	this.database = database;
    }

    @Override
    public <T> T select(String sql, QueryResultMapping<T> mapping) throws Exception {
	try (Connection connection = database.connect()) {
	    System.out.println(sql);
	    Statement statement = connection.createStatement();
	    ResultSet resultSet = statement.executeQuery(sql);
	    resultSet.next();
	    return mapping.map(resultSet);
	} catch (Exception exception) {
	    throw exception;
	}
    }

    @Override
    public long create(String sql) throws Exception {
	try (Connection connection = database.connect()) {
	    PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	    preparedStatement.executeUpdate();
	    ResultSet resultSet = preparedStatement.getGeneratedKeys();
	    resultSet.next();
	    return resultSet.getLong(1);
	} catch (Exception exception) {
	    throw exception;
	}
    }

    @Override
    public void update(String sql) throws Exception {
	modyfing(sql, ExecutionMode.UPDATE);
    }

    @Override
    public void delete(String sql) throws Exception {
	modyfing(sql, ExecutionMode.DELETE);
    }

    private void modyfing(String sql, ExecutionMode mode) throws Exception {
	try (Connection connection = database.connect()) {
	    Statement statement = connection.createStatement();
	    statement.executeUpdate(sql);
	} catch (Exception exception) {
	    throw exception;
	}
    }

    private enum ExecutionMode {
	UPDATE, DELETE
    }
}
