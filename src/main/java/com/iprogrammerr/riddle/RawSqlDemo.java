package com.iprogrammerr.riddle;

import com.iprogrammerr.riddle.database.SqlDatabase;
import com.iprogrammerr.riddle.database.SqlQueryExecutor;
import com.iprogrammerr.riddle.model.database.User;
import com.iprogrammerr.riddle.service.crud.UserService;

public class RawSqlDemo {

    public static void main(String[] args) {
	String username = "riddleadmin";
	String password = "rig1594or";
	String jdbcUrl = "jdbc:mysql://localhost:3306/riddle?useSSL=false&useUnicode=true&serverTimezone=UTC";
	try {
	    System.out.println("Connecting to database: " + jdbcUrl);
	    SqlDatabase connectionManager = new SqlDatabase(username, password, jdbcUrl);
	    SqlQueryExecutor executor = new SqlQueryExecutor(connectionManager);
	    UserService userService = new UserService(executor);
	    User user = userService.getUser(20);
	    System.out.println(user);
	} catch (Exception exception) {
	    exception.printStackTrace();
	}
    }
}
