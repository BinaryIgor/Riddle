package com.iprogrammerr.riddle.user;

import java.util.ArrayList;
import java.util.List;

import com.iprogrammerr.riddle.database.DatabaseSession;
import com.iprogrammerr.riddle.database.QueryTemplate;
import com.iprogrammerr.riddle.model.database.DatabaseRecord;
import com.iprogrammerr.riddle.model.database.Record;

public class DatabaseUsers implements Users {

    private static final String USER_SELECT_QUERY = "select user.*, user_role.name as role from user inner join user_role "
	    + "on user.user_role_id = user_role.id";
    private static final String USER_EXISTS_QUERY = "select count(1) from user";
    private DatabaseSession session;
    private QueryTemplate queryTemplate;

    public DatabaseUsers(DatabaseSession session, QueryTemplate queryTemplate) {
	this.session = session;
	this.queryTemplate = queryTemplate;
    }

    @Override
    public Iterable<User> all() {
	return many(USER_SELECT_QUERY);
    }

    @Override
    public long createPlayer(String name, String email, String password) throws Exception {
	Record user = new DatabaseRecord("user").put("name", name).put("email", email).put("password", password);
	return session.create(queryTemplate.insert(user));
    }

    @Override
    public Iterable<User> active() {
	String query = USER_SELECT_QUERY + " where user.active = true";
	return many(query);
    }

    private Iterable<User> many(String query) {
	try {
	    return session.select(query, resultSet -> {
		List<User> users = new ArrayList<>();
		do {
		    users.add(new DatabaseUser(session, queryTemplate, resultSet));
		} while (resultSet.next());
		return users;
	    });
	} catch (Exception exception) {
	    return new ArrayList<>();
	}
    }

    @Override
    public User user(String nameOrEmail) throws Exception {
	String query = USER_SELECT_QUERY;
	if (nameOrEmail.contains("@")) {
	    query += "where user.email = ?";
	} else {
	    query += "where user.name = ?";
	}
	return session.select(queryTemplate.query(query, nameOrEmail),
		resultSet -> new DatabaseUser(session, queryTemplate, resultSet));
    }

    @Override
    public boolean exists(String nameOrEmail) {
	String query = USER_EXISTS_QUERY;
	if (nameOrEmail.contains("@")) {
	    query += "where user.email = ?";
	} else {
	    query += "where user.name = ?";
	}
	try {
	    return session.select(queryTemplate.query(query, nameOrEmail), resultSet -> resultSet.getLong(1) > 0);
	} catch (Exception exception) {
	    exception.printStackTrace();
	    return false;
	}
    }

}
