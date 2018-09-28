package com.iprogrammerr.riddle;

import org.junit.Test;

import com.iprogrammerr.riddle.database.DatabaseSession;
import com.iprogrammerr.riddle.database.QueryResultMapping;
import com.iprogrammerr.riddle.database.ValidatedQueryDatabaseSession;

public class ValidatedQueryDatabaseSessionTest {

    private final ValidatedQueryDatabaseSession session;

    public ValidatedQueryDatabaseSessionTest() {
	this.session = new ValidatedQueryDatabaseSession(new DatabaseSession() {

	    @Override
	    public void update(String query) throws Exception {

	    }

	    @Override
	    public <T> T select(String query, QueryResultMapping<T> mapping) throws Exception {
		throw new Exception("This is a mock, therefore this operation is not supported");
	    }

	    @Override
	    public void delete(String query) throws Exception {

	    }

	    @Override
	    public long create(String query) throws Exception {
		return 0;
	    }
	});
    }

    @Test(expected = Exception.class)
    public void alter() throws Exception {
	this.session.select("alter table user add super", resultSet -> new Object());
    }

    @Test(expected = Exception.class)
    public void create() throws Exception {
	this.session.select("create table super", resultSet -> new Object());
    }

    @Test(expected = Exception.class)
    public void drop() throws Exception {
	this.session.select("drop table user", resultSet -> new Object());
    }

    @Test(expected = Exception.class)
    public void deleteAsterisk() throws Exception {
	this.session.select("delete * from user", resultSet -> new Object());
    }

    @Test(expected = Exception.class)
    public void deleteEverything() throws Exception {
	this.session.select("delete from user", resultSet -> new Object());
    }

    public void deleteConctete() throws Exception {
	this.session.select("delete from user where id=1", resultSet -> new Object());
    }
}
