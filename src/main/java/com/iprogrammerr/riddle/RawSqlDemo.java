package com.iprogrammerr.riddle;

import com.iprogrammerr.riddle.database.DatabaseConnectionManager;
import com.iprogrammerr.riddle.database.QueryExecutor;
import com.iprogrammerr.riddle.model.database.User;
import com.iprogrammerr.riddle.service.crud.UserService;

public class RawSqlDemo {

    public static void main(String[] args) {
	String username = "riddleadmin";
	String password = "rig1594or";
	String jdbcUrl = "jdbc:mysql://localhost:3306/riddle?useSSL=false&useUnicode=true&serverTimezone=UTC";
	try {
	    System.out.println("Connecting to database: " + jdbcUrl);
	    DatabaseConnectionManager connectionManager = new DatabaseConnectionManager(username, password, jdbcUrl);
	    QueryExecutor executor = new QueryExecutor(connectionManager);
	    UserService userService = new UserService(executor);
	    User user = userService.getUser(20);
	    System.out.println(user);
	} catch (Exception exception) {
	    exception.printStackTrace();
	}
    }
}
