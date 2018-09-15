package com.iprogrammerr.riddle;

import java.util.Iterator;

import com.iprogrammerr.bright.server.model.StringObject;
import com.iprogrammerr.riddle.database.Database;
import com.iprogrammerr.riddle.database.DatabaseSession;
import com.iprogrammerr.riddle.database.QueryTemplate;
import com.iprogrammerr.riddle.database.SqlDatabase;
import com.iprogrammerr.riddle.database.SqlDatabaseSession;
import com.iprogrammerr.riddle.database.SqlQueryTemplate;
import com.iprogrammerr.riddle.user.DatabaseUser;
import com.iprogrammerr.riddle.user.User;
import com.iprogrammerr.riddle.users.DatabaseUsers;
import com.iprogrammerr.riddle.users.Users;

public class RawSqlDemo {

    public static void main(String[] args) {
	String username = "riddleadmin";
	String password = "rig1594or";
	String jdbcUrl = "jdbc:mysql://localhost:3306/riddle?useSSL=false&useUnicode=true&serverTimezone=UTC";
	try {
	    System.out.println("Connecting to database: " + jdbcUrl);
	    Database database = new SqlDatabase(username, password, jdbcUrl);
	    DatabaseSession session = new SqlDatabaseSession(database);
	    QueryTemplate queryTemplate = new SqlQueryTemplate();
	    User user = new DatabaseUser(20, session, queryTemplate);
	    System.out.println("User name = " + user.name());
	    System.out.println(user.toString());
	    Users users = new DatabaseUsers(session, queryTemplate);
	    Iterator<User> cachedUsers = users.all().iterator();
	    while (cachedUsers.hasNext()) {
		System.out.println(cachedUsers.next().email());
	    }
	    user.update(new StringObject("name", user.name() + "I"));
	    System.out.println("Updated name = " + user.name());

	} catch (Exception exception) {
	    exception.printStackTrace();
	}
    }
}
