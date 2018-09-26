package com.iprogrammerr.riddle.user.database;

import java.sql.ResultSet;

import com.iprogrammerr.bright.server.model.KeyValue;
import com.iprogrammerr.bright.server.model.KeysValues;
import com.iprogrammerr.bright.server.model.StringsObjects;
import com.iprogrammerr.riddle.database.DatabaseRecord;
import com.iprogrammerr.riddle.database.DatabaseSession;
import com.iprogrammerr.riddle.database.QueryTemplate;
import com.iprogrammerr.riddle.user.User;

public final class DatabaseUser implements User {

    private final long id;
    private final DatabaseSession session;
    private final QueryTemplate template;
    private final KeysValues columns;

    public DatabaseUser(DatabaseSession session, QueryTemplate queryTemplate, ResultSet resultSet) throws Exception {
	this.id = resultSet.getLong("id");
	this.session = session;
	this.template = queryTemplate;
	this.columns = new StringsObjects().put("name", resultSet.getString("name"))
		.put("email", resultSet.getString("email")).put("password", resultSet.getString("password"))
		.put("active", resultSet.getBoolean("active")).put("role", resultSet.getString("role"));
    }

    public DatabaseUser(long id, DatabaseSession session, QueryTemplate queryTemplate) {
	this.id = id;
	this.session = session;
	this.template = queryTemplate;
	this.columns = new StringsObjects();
    }

    @Override
    public String name() throws Exception {
	if (!columns.has("name", String.class)) {
	    getAll();
	}
	return columns.value("name", String.class);
    }

    @Override
    public String email() throws Exception {
	if (!columns.has("email", String.class)) {
	    getAll();
	}
	return columns.value("email", String.class);
    }

    @Override
    public String password() throws Exception {
	if (!columns.has("password", String.class)) {
	    getAll();
	}
	return columns.value("password", String.class);
    }

    @Override
    public boolean active() throws Exception {
	if (!columns.has("active", Boolean.class)) {
	    getAll();
	}
	return columns.value("active", Boolean.class);
    }

    @Override
    public String role() throws Exception {
	if (!columns.has("role", String.class)) {
	    getAll();
	}
	return columns.value("role", String.class);
    }

    private void getAll() throws Exception {
	String selectAllTemplate = "select user.*, user_role.name as role from user inner join user_role on "
		+ " user.user_role_id = user_role.id where user.id = ?";
	session.select(template.query(selectAllTemplate, id), resultSet -> {
	    columns.put("name", resultSet.getString("name"));
	    columns.put("email", resultSet.getString("email"));
	    columns.put("password", resultSet.getString("password"));
	    columns.put("active", resultSet.getBoolean("active"));
	    columns.put("role", resultSet.getString("role"));
	    return columns;
	});
    }

    @Override
    public String toString() {
	return columns.toString();
    }

    @Override
    public long id() {
	return id;
    }

    @Override
    public void update(KeysValues columns) throws Exception {
	String query = template.update(new DatabaseRecord("user", columns), "id = ?", id);
	session.update(query);
	getAll();
    }

    @Override
    public void update(KeyValue column) throws Exception {
	String query = template.update(new DatabaseRecord("user").put(column.key(), column.value()), "id = ?", id);
	session.update(query);
	getAll();
    }

}
