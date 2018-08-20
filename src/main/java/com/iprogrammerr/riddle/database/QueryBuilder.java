package com.iprogrammerr.riddle.database;

import com.iprogrammerr.riddle.exception.database.QueryException;

public class QueryBuilder {

    private final StringBuilder builder;

    public QueryBuilder() {
	builder = new StringBuilder();
    }

    public QueryBuilder select(String... columns) {
	builder.append("select ").append(getCommaSeparatedString(columns)).append(" ");
	return this;
    }

    public QueryBuilder from(String... tables) {
	builder.append("from ").append(getCommaSeparatedString(tables)).append(" ");
	return this;
    }

    public QueryBuilder insertInto(String table, String... columns) {
	builder.append("insert into ").append(table).append("(").append(getCommaSeparatedString(columns)).append(") ");
	return this;
    }

    public QueryBuilder values(Object... values) {
	builder.append("values(");
	if (values.length == 0) {
	    builder.append(toString(values[0]));
	} else {
	    for (int i = 0; i < values.length; i++) {
		builder.append(toString(values[i]));
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

    public QueryBuilder set(Object... keysValues) {
	if ((keysValues.length % 2) != 0) {
	    throw QueryException.createIncorrectKeysToValuesNumber();
	}
	builder.append("set ");
	for (int i = 0; i < keysValues.length; i += 2) {
	    boolean string = keysValues[i].getClass().isAssignableFrom(String.class);
	    if (!string) {
		throw QueryException.createIncorretKeyException();
	    }
	    builder.append((String) keysValues[i]).append("=").append(toString(keysValues[i + 1]));
	    if (i < (keysValues.length - 2)) {
		builder.append(",");
	    }
	}
	builder.append(" ");
	return this;
    }

    public QueryBuilder deleteFrom(String table) {
	builder.append("delete from ").append(table).append(" ");
	return this;
    }

    public QueryBuilder where(String vaue) {
	builder.append("where ").append(vaue).append(" ");
	return this;
    }

    private QueryBuilder operatorTo(String operator, Object value) {
	builder.append(operator).append(" ").append(toString(value)).append(" ");
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
	builder.append("like '").append(pattern).append("' ");
	return this;
    }

    public QueryBuilder in(Object... values) {
	builder.append("in ");
	boolean strings = values[0].getClass().isAssignableFrom(String.class);
	if (values.length == 0) {
	    builder.append(strings ? "'" + values[0] + "'" : values[0].toString());
	} else if (strings) {
	    builder.append(getCommaSeparatedAsValuesString((String[]) values));
	} else {
	    builder.append(getCommaSeparatedString((String[]) values));
	}
	builder.append(" ");
	return this;
    }

    public QueryBuilder between(Object first, Object second) {
	builder.append("between ").append(toString(first)).append(" and ").append(toString(second)).append(" ");
	return this;
    }

    public QueryBuilder notBetween(Object first, Object second) {
	builder.append("not between ").append(toString(first)).append(" and ").append(toString(second)).append(" ");
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
	return join(JoinType.INNER, table);
    }

    public QueryBuilder leftJoin(String table) {
	return join(JoinType.LEFT, table);
    }

    public QueryBuilder rightJoin(String table) {
	return join(JoinType.RIGHT, table);
    }

    public QueryBuilder fullJoin(String table) {
	return join(JoinType.FULL, table);
    }

    private QueryBuilder join(JoinType type, String table) {
	builder.append(type.value).append(" join ").append(table).append(" ");
	return this;
    }

    public QueryBuilder on(String column) {
	builder.append("on ").append(column).append(" ");
	return this;
    }

    public QueryBuilder orderBy(String... columns) {
	builder.append("order by ").append(getCommaSeparatedString(columns)).append(" ");
	return this;
    }

    public QueryBuilder asc() {
	builder.append("asc ");
	return this;
    }

    public QueryBuilder desc() {
	builder.append("desc ");
	return this;
    }

    public QueryBuilder groupBy(String... columns) {
	builder.append("group by ").append(getCommaSeparatedString(columns)).append(" ");
	return this;
    }

    public QueryBuilder having(String value) {
	builder.append("having ").append(value).append(" ");
	return this;
    }

    public QueryBuilder whereExists() {
	builder.append("where exists ");
	return this;
    }

    public QueryBuilder whereNotExists() {
	builder.append("where not exists ");
	return this;
    }

    public QueryBuilder any() {
	builder.append("any ");
	return this;
    }

    public QueryBuilder all() {
	builder.append("all ");
	return this;
    }

    public QueryBuilder union() {
	builder.append("union ");
	return this;
    }

    public QueryBuilder unionAll() {
	builder.append("union all ");
	return this;
    }

    public QueryBuilder min(String columnName) {
	return function(FunctionType.MIN, columnName);
    }

    public QueryBuilder max(String columnName) {
	return function(FunctionType.MAX, columnName);
    }

    public QueryBuilder count(String columnName) {
	return function(FunctionType.COUNT, columnName);
    }

    public QueryBuilder avg(String columnName) {
	return function(FunctionType.AVG, columnName);
    }

    public QueryBuilder sum(String columnName) {
	return function(FunctionType.SUM, columnName);
    }

    private QueryBuilder function(FunctionType type, String columnName) {
	builder.append(type.value).append("(").append(columnName).append(") ");
	return this;
    }

    private String getCommaSeparatedString(String[] strings) {
	if (strings.length == 1) {
	    return strings[0];
	}
	return String.join(",", strings);
    }

    private String getCommaSeparatedAsValuesString(String[] strings) {
	if (strings.length == 1) {
	    return "'" + strings[0] + "'";
	}
	return "'" + String.join("','", strings) + "'";
    }

    private String toString(Object object) {
	if (object.getClass().isAssignableFrom(String.class)) {
	    return "'" + object + "'";
	}
	return object.toString();
    }

    public QueryBuilder as(String alias) {
	builder.append("as ").append(alias).append(" ");
	return this;
    }

    public String build() {
	String query = builder.toString();
	return query.substring(0, query.length() - 1);
    }

    private enum JoinType {
	INNER("inner"), LEFT("left"), RIGHT("right"), FULL("full outer");

	public String value;

	JoinType(String value) {
	    this.value = value;
	}
    }

    private enum FunctionType {
	MIN("min"), MAX("max"), COUNT("count"), AVG("avg"), SUM("sum");

	public String value;

	private FunctionType(String value) {
	    this.value = value;
	}
    }
}
