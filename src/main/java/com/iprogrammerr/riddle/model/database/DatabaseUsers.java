package com.iprogrammerr.riddle.model.database;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.iprogrammerr.riddle.database.DatabaseSession;
import com.iprogrammerr.riddle.database.QueryTemplate;

public class DatabaseUsers implements Users {

    private static final String PLAYER_USER_ROLE = "player";
    private DatabaseSession session;
    private QueryTemplate queryTemplate;

    public DatabaseUsers(DatabaseSession session, QueryTemplate queryTemplate) {
	this.session = session;
	this.queryTemplate = queryTemplate;
    }

    @Override
    public Iterable<User> all() {
	String query = "select user.*, user_role.name as role from user inner join user_role "
		+ "on user.user_role_id = user_role.id";
	return many(query);
    }

    @Override
    public User create(String name, String email, String password) throws Exception {
	Table userTable = new DatabaseTable("user").put("name", name).put("email", email).put("password", password);
	long id = session.create(queryTemplate.insert(userTable));
	return new DatabaseUser(id, session, queryTemplate);
    }

    // TODO proper validation
    @Override
    public User create(String json) throws Exception {
	JSONObject jsonObject = new JSONObject(json);
	String name = jsonObject.optString("name");
	if (name.length() < 3) {
	    throw new Exception("User name must have at least 3 characters");
	}
	String email = jsonObject.optString("email");
	if (email.length() < 3) {
	    throw new Exception("User email must have at least 3 characters");
	}
	String password = jsonObject.optString("password");
	if (password.length() < 5) {
	    throw new Exception("User password must have at least 5 characters");
	}
	String insertQuery = queryTemplate.insert(new DatabaseTable("user").put("name", name));
	long id = session.create(insertQuery);
	return new DatabaseUser(id, session, queryTemplate);
    }

    @Override
    public Iterable<User> active() {
	String query = "select user.*, user_role.name as role from user inner join user_role "
		+ "on user.user_role_id = user_role.id where user.active = true";
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

}
