package com.iprogrammerr.riddle.database;

import java.sql.Connection;

import com.mchange.v2.c3p0.ComboPooledDataSource;

//Is this data source good enough?
public final class SqlDatabase implements Database {

    private final ComboPooledDataSource dataSource;

    public SqlDatabase(String user, String password, String jdbcUrl) {
	dataSource = new ComboPooledDataSource();
	dataSource.setJdbcUrl(jdbcUrl);
	dataSource.setUser(user);
	dataSource.setPassword(password);
    }

    @Override
    public Connection connect() throws Exception {
	return dataSource.getConnection();
    }

    @Override
    public void close() {
	dataSource.close();
    }

}
