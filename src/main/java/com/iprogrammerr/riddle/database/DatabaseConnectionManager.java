package com.iprogrammerr.riddle.database;

import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DatabaseConnectionManager {

    private ComboPooledDataSource dataSource;

    public DatabaseConnectionManager(String user, String password, String jdbcUrl) {
	dataSource = new ComboPooledDataSource();
	dataSource.setJdbcUrl(jdbcUrl);
	dataSource.setUser(user);
	dataSource.setPassword(password);
    }

    public Connection getConnection() throws SQLException {
	return dataSource.getConnection();
    }

    public void close() {
	dataSource.close();
    }

}
