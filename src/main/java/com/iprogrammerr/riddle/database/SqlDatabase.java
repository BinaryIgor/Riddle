package com.iprogrammerr.riddle.database;

import java.sql.Connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public final class SqlDatabase implements Database {

    private final HikariDataSource dataSource;

    public SqlDatabase(String username, String password, String jdbcUrl) {
	HikariConfig config = new HikariConfig();
	config.setUsername(username);
	config.setPassword(password);
	config.setJdbcUrl(jdbcUrl);
	this.dataSource = new HikariDataSource(config);
    }

    public SqlDatabase(HikariDataSource dataSource) {
	this.dataSource = dataSource;
    }

    @Override
    public Connection connection() throws Exception {
	return this.dataSource.getConnection();
    }

    @Override
    public void close() {
	this.dataSource.close();
    }

}
