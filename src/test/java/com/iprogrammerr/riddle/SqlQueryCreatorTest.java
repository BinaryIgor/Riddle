package com.iprogrammerr.riddle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.iprogrammerr.riddle.database.SqlQueryCreator;
import com.iprogrammerr.riddle.exception.database.QueryCreatorException;

public class SqlQueryCreatorTest {

    private static final String PARAM_SIGN = "?";
    private SqlQueryCreator queryCreator;
    private String queryTemplate;

    @Before
    public void setup() {
	queryCreator = new SqlQueryCreator(PARAM_SIGN.charAt(0));
    }

    @Test(expected = QueryCreatorException.class)
    public void incorrectQueryTest() {
	queryTemplate = "insert into user (?,?,?,?,?)";
	queryCreator.create(queryTemplate, 1, "bug");
    }

    @Test
    public void selectTest() {
	queryTemplate = "select user.*, user_role.id as rid, user_role.name as rname from user "
		+ "inner join user_role on user.user_role_id = user_role.id where user.id = ?";
	Long id = 1L;
	String query = queryCreator.create(queryTemplate, id);
	assertTrue(query.indexOf(id.toString()) > 0);
	String restoredFromQueryTemplate = restoreTemplateFromQuery(query, id);
	assertEquals(restoredFromQueryTemplate, queryTemplate);
    }

    @Test
    public void insertTest() {
	queryTemplate = "insert into user (name, email, password) values (?, ?, ?)";
	String name = "Igor";
	String email = "ceigor94@gmail.com";
	String password = "ig15dada";
	String query = queryCreator.create(queryTemplate, name, email, password);
	assertTrue(query.indexOf(name) > 0);
	assertTrue(query.indexOf(email) > 0);
	assertTrue(query.indexOf(password) > 0);
	String restoredFromQueryTemplate = restoreTemplateFromQuery(query, name, email, password);
	assertEquals(restoredFromQueryTemplate, queryTemplate);
    }

    @Test
    public void updateTest() {
	queryTemplate = "update user set name=?, active=?, password=?, email=? where user.id = ?";
	String name = "Igor";
	Boolean active = true;
	String password = "szczupak";
	String email = "szczupak@gmail.com";
	Long id = 2L;
	String query = queryCreator.create(queryTemplate, name, active, password, email, id);
	assertTrue(query.indexOf(name) > 0);
	assertTrue(query.indexOf(active.toString()) > 0);
	assertTrue(query.indexOf(password) > 0);
	assertTrue(query.indexOf(email) > 0);
	assertTrue(query.indexOf(id.toString()) > 0);
	String restoredFromQueryTemplate = restoreTemplateFromQuery(query, name, active, password, email, id);
	assertEquals(restoredFromQueryTemplate, queryTemplate);
    }

    @Test
    public void deleteTest() {
	queryTemplate = "delete from user where name like ?";
	String patternToSearch = "%ig%";
	String query = queryCreator.create(queryTemplate, patternToSearch);
	assertTrue(query.indexOf(patternToSearch) > 0);
	String restoredFromQueryTemplate = restoreTemplateFromQuery(query, patternToSearch);
	assertEquals(restoredFromQueryTemplate, queryTemplate);
    }

    private String restoreTemplateFromQuery(String query, Object... values) {
	String restoredFromTemplateQuery = query;
	for (Object value: values) {
	    String stringedValue = value.getClass().isAssignableFrom(String.class) ? "'" + value + "'" : value.toString();
	    restoredFromTemplateQuery = restoredFromTemplateQuery.replace(stringedValue, PARAM_SIGN);
	}
	return restoredFromTemplateQuery;
    }

}
