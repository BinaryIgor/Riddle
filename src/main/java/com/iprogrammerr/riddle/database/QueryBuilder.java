package com.iprogrammerr.riddle.database;

public class QueryBuilder {

    private final StringBuilder builder;

    public QueryBuilder() {
	builder = new StringBuilder();
    }

    public QueryBuilder select(String... columns) {
	builder.append("select ");
	if (columns.length == 1) {
	    builder.append(columns[0]);
	} else {
	    builder.append(getCommaSeparatedString(columns));
	}
	builder.append(" ");
	return this;
    }

    public QueryBuilder from(String... tables) {
	builder.append("from ");
	if (tables.length == 1) {
	    builder.append(tables[0]);
	} else {
	    builder.append(getCommaSeparatedString(tables));
	}
	builder.append(" ");
	return this;
    }

    public QueryBuilder insertInto(String table, String... columns) {
	builder.append("insert into ").append(table).append("(");
	if (columns.length == 1) {
	    builder.append(columns[0]);
	} else {
	    builder.append(getCommaSeparatedString(columns));
	}
	builder.append(") ");
	return this;
    }

    public QueryBuilder values(Object... values) {
	builder.append("values(");
	boolean string;
	if (values.length == 0) {
	    string = values[0].getClass().isAssignableFrom(String.class);
	    builder.append(string ? "'" + values[0] + "'" : values[0].toString());
	} else {
	    for (int i = 0; i < values.length; i++) {
		string = values[i].getClass().isAssignableFrom(String.class);
		builder.append(string ? "'" + values[i] + "'" : values[i].toString());
		if (i != values.length - 1) {
		    builder.append(",");
		}
	    }
	}
	builder.append(") ");
	return this;
    }

    public QueryBuilder update(String table) {
	builder.append("update ").append(table).append(" ");
	return this;
    }

    public QueryBuilder where(String column) {
	builder.append("where ").append(column).append(" ");
	return this;
    }

    private QueryBuilder operatorTo(String operator, Object value) {
	builder.append(operator).append(" ");
	if (value.getClass().isAssignableFrom(String.class)) {
	    builder.append("'").append(value).append("'");
	} else {
	    builder.append(value.toString());
	}
	builder.append(" ");
	return this;
    }

    private QueryBuilder operatorTo(String operator, String column) {
	builder.append(operator).append(" ").append(column).append(" ");
	return this;
    }

    public QueryBuilder isEqualTo(Object value) {
	return operatorTo("=", value);
    }

    public QueryBuilder isEqualTo(String column) {
	return operatorTo("=", column);
    }

    public QueryBuilder isNotEqualTo(Object value) {
	return operatorTo("<>", value);
    }

    public QueryBuilder isNotEqualTo(String column) {
	return operatorTo("<>", column);
    }

    public QueryBuilder like(String pattern) {
	builder.append("like").append(pattern).append(" ");
	return this;
    }

    public QueryBuilder in(Object... values) {
	builder.append("in ");
	boolean strings = values[0].getClass().isAssignableFrom(String.class);
	if (values.length == 0) {
	    builder.append(strings ? "'" + values[0] + "'" : values[0].toString());
	} else {
	    builder.append(getCommaSeparatedAsValuesString((String[]) values));
	}
	builder.append(" ");
	return this;
    }

    public QueryBuilder and(String column) {
	builder.append("and ").append(column).append(" ");
	return this;
    }

    public QueryBuilder or(String column) {
	builder.append("or ").append(column).append(" ");
	return this;
    }

    public QueryBuilder innerJoin(String table) {
	builder.append("inner join ").append(table).append(" ");
	return this;
    }

    public QueryBuilder on(String column) {
	builder.append("on ").append(column).append(" ");
	return this;
    }

    private String getCommaSeparatedString(String[] strings) {
	return String.join(",", strings);
    }

    private String getCommaSeparatedAsValuesString(String[] strings) {
	return "'" + String.join("','", strings) + "'";
    }

    public String build() {
	String query = builder.toString();
	return query.substring(0, query.length() - 1);
    }

}
