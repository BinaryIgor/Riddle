package com.iprogrammerr.riddle.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.iprogrammerr.riddle.exception.creation.CreationException;
import com.iprogrammerr.riddle.exception.database.DeleteException;
import com.iprogrammerr.riddle.exception.database.NoResultException;
import com.iprogrammerr.riddle.exception.database.UpdateException;

public class SqlQueryExecutor implements QueryExecutor<Long>{

    private Database database;

    public SqlQueryExecutor(Database database) {
	this.database = database;
    }

    public <T> T select(String sql, QueryResultToObjectConverter<T> converter) {
	try (Connection connection = database.connect()) {
	    System.out.println(sql);
	    Statement statement = connection.createStatement();
	    ResultSet resultSet = statement.executeQuery(sql);
	    resultSet.next();
	    return converter.convert(resultSet);
	} catch (Exception exception) {
	    throw new NoResultException(exception);
	}
    }

    public Long create(String sql) {
	try (Connection connection = database.connect()) {
	    PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	    preparedStatement.executeUpdate();
	    ResultSet resultSet = preparedStatement.getGeneratedKeys();
	    resultSet.next();
	    return resultSet.getLong(1);
	} catch (Exception exception) {
	    throw new CreationException(exception.getMessage());
	}
    }

    public void update(String sql) {
	modyfing(sql, ExecutionMode.UPDATE);
    }

    public void delete(String sql) {
	modyfing(sql, ExecutionMode.DELETE);
    }

    private void modyfing(String sql, ExecutionMode mode) {
	try (Connection connection = database.connect()) {
	    Statement statement = connection.createStatement();
	    statement.executeUpdate(sql);
	} catch (Exception exception) {
	    exception.printStackTrace();
	    if (mode.equals(ExecutionMode.UPDATE)) {
		throw new UpdateException(exception.getMessage());
	    }
	    throw new DeleteException(exception.getMessage());
	}
    }

    private enum ExecutionMode {
	UPDATE, DELETE
    }
}
