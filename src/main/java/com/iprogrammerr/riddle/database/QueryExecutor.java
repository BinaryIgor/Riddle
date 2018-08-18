package com.iprogrammerr.riddle.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.iprogrammerr.riddle.exception.creation.CreationException;
import com.iprogrammerr.riddle.exception.database.DeleteException;
import com.iprogrammerr.riddle.exception.database.NoResultException;
import com.iprogrammerr.riddle.exception.database.UpdateException;

public class QueryExecutor {

    private DatabaseConnectionManager connectionManager;

    public QueryExecutor(DatabaseConnectionManager connectionManager) {
	this.connectionManager = connectionManager;
    }

    public <T> T executeSelect(String sql, QueryResultToObjectConverter<T> converter) {
	try (Connection connection = connectionManager.getConnection()) {
	    Statement statement = connection.createStatement();
	    ResultSet resultSet = statement.executeQuery(sql);
	    resultSet.next();
	    return converter.convert(resultSet);
	} catch (Exception exception) {
	    exception.printStackTrace();
	    throw new NoResultException();
	}
    }

    public long executeCreate(String sql) {
	try (Connection connection = connectionManager.getConnection()) {
	    PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	    preparedStatement.executeUpdate();
	    ResultSet resultSet = preparedStatement.getGeneratedKeys();
	    resultSet.next();
	    return resultSet.getLong(1);
	} catch (Exception exception) {
	    exception.printStackTrace();
	    throw new CreationException(exception.getMessage());
	}
    }

    public void executeUpdate(String sql) {
	executeModyfing(sql, ExecuteMode.UPDATE);
    }

    public void executeDelete(String sql) {
	executeModyfing(sql, ExecuteMode.DELETE);
    }

    private void executeModyfing(String sql, ExecuteMode mode) {
	try (Connection connection = connectionManager.getConnection()) {
	    Statement statement = connection.createStatement();
	    statement.executeUpdate(sql);
	} catch (Exception exception) {
	    exception.printStackTrace();
	    if (mode.equals(ExecuteMode.UPDATE)) {
		throw new UpdateException(exception.getMessage());
	    }
	    throw new DeleteException(exception.getMessage());
	}
    }

    private enum ExecuteMode {
	UPDATE, DELETE
    }
}
