package com.iprogrammerr.riddle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.iprogrammerr.riddle.database.SqlQueryTemplate;
import com.iprogrammerr.riddle.model.database.DatabaseTable;

public class SqlQueryTemplateTest {

    private static final String PARAM_SIGN = "?";
    private SqlQueryTemplate queryTemplate;

    @Before
    public void setup() {
	queryTemplate = new SqlQueryTemplate(PARAM_SIGN.charAt(0));
    }

    @Test
    public void selectTest() throws Exception {
	String queryTemplate = "select user.*, user_role.id as rid, user_role.name as rname from user "
		+ "inner join user_role on user.user_role_id = user_role.id where user.id = ?";
	Long id = 1L;
	String query = this.queryTemplate.query(queryTemplate, id);
	assertTrue(query.indexOf(id.toString()) > 0);
	String restoredFromQueryTemplate = restoreTemplateFromQuery(query, id);
	assertEquals(restoredFromQueryTemplate, queryTemplate);
    }

    @Test
    public void insertTest() throws Exception {
	String name = "Igor";
	String email = "ceigor94@gmail.com";
	String password = "ig15dada";
	String query = queryTemplate
		.insert(new DatabaseTable("user").put("name", name).put("email", email).put("password", password));
	System.out.println(query);
	assertTrue(query.indexOf(name) > 0);
	assertTrue(query.indexOf(email) > 0);
	assertTrue(query.indexOf(password) > 0);
    }

    @Test
    public void updateTest() throws Exception {
	String name = "Igor";
	Boolean active = true;
	String password = "szczupak";
	String email = "szczupak@gmail.com";
	Long id = 2L;
	String query = this.queryTemplate.update(new DatabaseTable("user").put("name", name).put("active", active)
		.put("password", password).put("email", email), "id = ?", id);
	System.out.println(query);
	assertTrue(query.indexOf(name) > 0);
	assertTrue(query.indexOf(active.toString()) > 0);
	assertTrue(query.indexOf(password) > 0);
	assertTrue(query.indexOf(email) > 0);
	assertTrue(query.indexOf(id.toString()) > 0);
    }

    @Test
    public void deleteTest() throws Exception {
	String queryTemplate = "delete from user where name like ?";
	String patternToSearch = "%ig%";
	String query = this.queryTemplate.query(queryTemplate, patternToSearch);
	System.out.println(query);
	assertTrue(query.indexOf(patternToSearch) > 0);
	String restoredFromQueryTemplate = restoreTemplateFromQuery(query, patternToSearch);
	assertEquals(restoredFromQueryTemplate, queryTemplate);
    }

    private String restoreTemplateFromQuery(String query, Object... values) {
	String restoredFromTemplateQuery = query;
	for (Object value : values) {
	    String stringedValue = value.getClass().isAssignableFrom(String.class) ? "'" + value + "'"
		    : value.toString();
	    restoredFromTemplateQuery = restoredFromTemplateQuery.replace(stringedValue, PARAM_SIGN);
	}
	return restoredFromTemplateQuery;
    }

}
