package com.iprogrammerr.riddle.model.database;

import com.iprogrammerr.bright.server.model.KeyValue;
import com.iprogrammerr.bright.server.model.KeysValues;
import com.iprogrammerr.riddle.database.DatabaseSession;
import com.iprogrammerr.riddle.database.QueryTemplate;

public class WritableDatabaseUser implements WritableUser {

    private DatabaseSession session;
    private QueryTemplate queryTemplate;
    private User user;

    public WritableDatabaseUser(User user, DatabaseSession session, QueryTemplate queryTemplate) {
	this.user = user;
	this.session = session;
	this.queryTemplate = queryTemplate;
    }

    @Override
    public long id() {
	return user.id();
    }

    @Override
    public String name() throws Exception {
	return user.name();
    }

    @Override
    public String email() throws Exception {
	return user.email();
    }

    @Override
    public String password() throws Exception {
	return user.password();
    }

    @Override
    public boolean active() throws Exception {
	return user.active();
    }

    @Override
    public String role() throws Exception {
	return user.role();
    }

    @Override
    public void update(KeysValues columns) throws Exception {
	String query = queryTemplate.update(new DatabaseTable("user", columns), "id = ?", user.id());
	session.update(query);
    }

    @Override
    public void update(KeyValue column) throws Exception {
	String query = queryTemplate.update(new DatabaseTable("user").put(column.key(), column.value()), "id = ?",
		user.id());
	session.update(query);
    }

}
