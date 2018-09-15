package com.iprogrammerr.riddle.user;

import java.sql.ResultSet;

import com.iprogrammerr.bright.server.model.KeyValue;
import com.iprogrammerr.bright.server.model.KeysValues;
import com.iprogrammerr.bright.server.model.StringsObjects;
import com.iprogrammerr.riddle.database.DatabaseRecord;
import com.iprogrammerr.riddle.database.DatabaseSession;
import com.iprogrammerr.riddle.database.QueryTemplate;

public class DatabaseUser implements User {

    private long id;
    private DatabaseSession session;
    private QueryTemplate template;
    private KeysValues columns;

    public DatabaseUser(DatabaseSession session, QueryTemplate queryTemplate, ResultSet resultSet) throws Exception {
	this.id = resultSet.getLong("id");
	this.session = session;
	this.template = queryTemplate;
	this.columns = new StringsObjects().add("name", resultSet.getString("name"))
		.add("email", resultSet.getString("email")).add("password", resultSet.getString("password"))
		.add("active", resultSet.getBoolean("active")).add("role", resultSet.getString("role"));
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
	    columns.add("name", resultSet.getString("name"));
	    columns.add("email", resultSet.getString("email"));
	    columns.add("password", resultSet.getString("password"));
	    columns.add("active", resultSet.getBoolean("active"));
	    columns.add("role", resultSet.getString("role"));
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
