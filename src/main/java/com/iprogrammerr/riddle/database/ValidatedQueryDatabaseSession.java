package com.iprogrammerr.riddle.database;

import java.util.regex.Pattern;

public final class ValidatedQueryDatabaseSession implements DatabaseSession {

    private static final Pattern SAFE_DELETE = Pattern.compile("(delete from [^\\s]+ where )");
    private final DatabaseSession base;

    public ValidatedQueryDatabaseSession(DatabaseSession base) {
	this.base = base;
    }

    @Override
    public <T> T select(String query, QueryResultMapping<T> mapping) throws Exception {
	validate(query);
	return this.base.select(query, mapping);
    }

    @Override
    public long create(String query) throws Exception {
	validate(query);
	return this.base.create(query);
    }

    @Override
    public void update(String query) throws Exception {
	validate(query);
	this.base.update(query);
    }

    @Override
    public void delete(String query) throws Exception {
	validate(query);
	this.base.delete(query);
    }

    // TODO is it enough?
    private void validate(String query) throws Exception {
	query = query.toLowerCase();
	String illegallKeyword = illegalKeyword(query);
	if (!illegallKeyword.isEmpty()) {
	    throw new Exception(String.format("Query can not contain %s keyword", illegallKeyword));
	}
	if (dangerousDelete(query)) {
	    throw new Exception("Query contains dangerous delete statement");
	}

    }

    private String illegalKeyword(String query) {
	String illegalKeyword = "";
	if (query.contains("alter")) {
	    illegalKeyword = "alter";
	} else if (query.contains("create")) {
	    illegalKeyword = "create";
	} else if (query.contains("drop")) {
	    illegalKeyword = "drop";
	}
	return illegalKeyword;
    }

    private boolean dangerousDelete(String query) {
	return query.contains("delete * from") || (query.contains("delete") && !SAFE_DELETE.matcher(query).find());
    }

}
