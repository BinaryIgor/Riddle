package com.iprogrammerr.riddle.user.database;

import java.sql.ResultSet;

import com.iprogrammerr.riddle.database.DatabaseRecord;
import com.iprogrammerr.riddle.database.DatabaseSession;
import com.iprogrammerr.riddle.database.QueryTemplate;
import com.iprogrammerr.riddle.model.Columns;
import com.iprogrammerr.riddle.model.KeyValue;
import com.iprogrammerr.riddle.model.TypedMap;
import com.iprogrammerr.riddle.user.User;

public final class DatabaseUser implements User {

    private final long id;
    private final DatabaseSession session;
    private final QueryTemplate template;
    private final TypedMap columns;

    public DatabaseUser(DatabaseSession session, QueryTemplate queryTemplate, ResultSet resultSet) throws Exception {
	this(resultSet.getLong("id"), session, queryTemplate,
		new Columns().put("name", resultSet.getString("name")).put("email", resultSet.getString("email"))
			.put("password", resultSet.getString("password")).put("active", resultSet.getBoolean("active"))
			.put("role", resultSet.getString("role")));
    }

    public DatabaseUser(long id, DatabaseSession session, QueryTemplate queryTemplate, TypedMap columns) {
	this.id = id;
	this.session = session;
	this.template = queryTemplate;
	this.columns = columns;
    }

    public DatabaseUser(long id, DatabaseSession session, QueryTemplate queryTemplate) {
	this(id, session, queryTemplate, new Columns());
    }

    @Override
    public String name() throws Exception {
	if (!this.columns.has("name")) {
	    allColumns();
	}
	return this.columns.stringValue("name");
    }

    @Override
    public String email() throws Exception {
	if (!this.columns.has("email")) {
	    allColumns();
	}
	return this.columns.stringValue("email");
    }

    @Override
    public String password() throws Exception {
	if (!this.columns.has("password")) {
	    allColumns();
	}
	return this.columns.stringValue("password");
    }

    @Override
    public boolean active() throws Exception {
	if (!this.columns.has("active")) {
	    allColumns();
	}
	return this.columns.booleanValue("active");
    }

    @Override
    public String role() throws Exception {
	if (!this.columns.has("role")) {
	    allColumns();
	}
	return this.columns.stringValue("role");
    }

    private void allColumns() throws Exception {
	String selectAllTemplate = "select user.*, user_role.name as role from user inner join user_role on "
		+ " user.user_role_id = user_role.id where user.id = ?";
	this.session.select(this.template.query(selectAllTemplate, this.id), resultSet -> {
	    this.columns.put("name", resultSet.getString("name"));
	    this.columns.put("email", resultSet.getString("email"));
	    this.columns.put("password", resultSet.getString("password"));
	    this.columns.put("active", resultSet.getBoolean("active"));
	    this.columns.put("role", resultSet.getString("role"));
	    return this.columns;
	});
    }

    @Override
    public String toString() {
	return this.columns.toString();
    }

    @Override
    public long id() {
	return this.id;
    }

    @Override
    public void change(TypedMap columns) throws Exception {
	String query = this.template.updateQuery(new DatabaseRecord("user", columns), "id = ?", this.id);
	this.session.update(query);
	allColumns();
    }

    @Override
    public void change(KeyValue column) throws Exception {
	String query = this.template.updateQuery(new DatabaseRecord("user").put(column.key(), column.value()), "id = ?",
		this.id);
	this.session.update(query);
	allColumns();
    }

}
