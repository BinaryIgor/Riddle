package com.iprogrammerr.riddle.database;

public class SqlQueryBuilder {

    private final StringBuilder builder;

    public SqlQueryBuilder() {
	builder = new StringBuilder();
    }

    public SqlQueryBuilder select(String... columns) {
	builder.append("select ").append(getCommaSeparatedString(columns)).append(" ");
	return this;
    }

    public SqlQueryBuilder from(String... tables) {
	builder.append("from ").append(getCommaSeparatedString(tables)).append(" ");
	return this;
    }

    public SqlQueryBuilder insertInto(String table) {
	builder.append("insert into ").append(table).append(" ");
	return this;
    }

    public SqlQueryBuilder keys(String... keys) {
	builder.append("(").append(getCommaSeparatedString(keys)).append(") ");
	return this;
    }

    public SqlQueryBuilder values(Object... values) {
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

    public SqlQueryBuilder update(String table) {
	builder.append("update ").append(table).append(" ");
	return this;
    }

    public SqlQueryBuilder set(Object... keysValues) throws Exception {
	if ((keysValues.length % 2) != 0) {
	    throw new Exception("Key values number is incorrect");
	}
	builder.append("set ");
	for (int i = 0; i < keysValues.length; i += 2) {
	    boolean string = keysValues[i].getClass().isAssignableFrom(String.class);
	    if (!string) {
		throw new Exception("Every key should be a string");
	    }
	    builder.append((String) keysValues[i]).append("=").append(toString(keysValues[i + 1]));
	    if (i < (keysValues.length - 2)) {
		builder.append(",");
	    }
	}
	builder.append(" ");
	return this;
    }

    public SqlQueryBuilder deleteFrom(String table) {
	builder.append("delete from ").append(table).append(" ");
	return this;
    }

    public SqlQueryBuilder where(String value) {
	builder.append("where ").append(value).append(" ");
	return this;
    }

    private SqlQueryBuilder operatorTo(String operator, Object columnOrValue, boolean column) {
	builder.append(operator).append(" ").append(column ? columnOrValue : toString(columnOrValue)).append(" ");
	return this;
    }

    public SqlQueryBuilder isEqualToValue(Object value) {
	return operatorTo("=", value, false);
    }

    public SqlQueryBuilder isEqualToColumn(String column) {
	return operatorTo("=", column, true);
    }

    public SqlQueryBuilder isNotEqualToValue(Object value) {
	return operatorTo("<>", value, false);
    }

    public SqlQueryBuilder isNotEqualToColumn(String column) {
	return operatorTo("<>", column, true);
    }

    public SqlQueryBuilder like(String pattern) {
	builder.append("like '").append(pattern).append("' ");
	return this;
    }

    public SqlQueryBuilder in(Object... values) {
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

    public SqlQueryBuilder between(Object first, Object second) {
	builder.append("between ").append(toString(first)).append(" and ").append(toString(second)).append(" ");
	return this;
    }

    public SqlQueryBuilder notBetween(Object first, Object second) {
	builder.append("not between ").append(toString(first)).append(" and ").append(toString(second)).append(" ");
	return this;
    }

    public SqlQueryBuilder and(String column) {
	builder.append("and ").append(column).append(" ");
	return this;
    }

    public SqlQueryBuilder or(String column) {
	builder.append("or ").append(column).append(" ");
	return this;
    }

    public SqlQueryBuilder innerJoin(String table) {
	return join(JoinType.INNER, table);
    }

    public SqlQueryBuilder leftJoin(String table) {
	return join(JoinType.LEFT, table);
    }

    public SqlQueryBuilder rightJoin(String table) {
	return join(JoinType.RIGHT, table);
    }

    public SqlQueryBuilder fullJoin(String table) {
	return join(JoinType.FULL, table);
    }

    private SqlQueryBuilder join(JoinType type, String table) {
	builder.append(type.value).append(" join ").append(table).append(" ");
	return this;
    }

    public SqlQueryBuilder on(String column) {
	builder.append("on ").append(column).append(" ");
	return this;
    }

    public SqlQueryBuilder orderBy(String... columns) {
	builder.append("order by ").append(getCommaSeparatedString(columns)).append(" ");
	return this;
    }

    public SqlQueryBuilder asc() {
	builder.append("asc ");
	return this;
    }

    public SqlQueryBuilder desc() {
	builder.append("desc ");
	return this;
    }

    public SqlQueryBuilder groupBy(String... columns) {
	builder.append("group by ").append(getCommaSeparatedString(columns)).append(" ");
	return this;
    }

    public SqlQueryBuilder having(String value) {
	builder.append("having ").append(value).append(" ");
	return this;
    }

    public SqlQueryBuilder whereExists() {
	builder.append("where exists ");
	return this;
    }

    public SqlQueryBuilder whereNotExists() {
	builder.append("where not exists ");
	return this;
    }

    public SqlQueryBuilder any() {
	builder.append("any ");
	return this;
    }

    public SqlQueryBuilder all() {
	builder.append("all ");
	return this;
    }

    public SqlQueryBuilder union() {
	builder.append("union ");
	return this;
    }

    public SqlQueryBuilder unionAll() {
	builder.append("union all ");
	return this;
    }

    public SqlQueryBuilder min(String columnName) {
	return function(FunctionType.MIN, columnName);
    }

    public SqlQueryBuilder max(String columnName) {
	return function(FunctionType.MAX, columnName);
    }

    public SqlQueryBuilder count(String columnName) {
	return function(FunctionType.COUNT, columnName);
    }

    public SqlQueryBuilder avg(String columnName) {
	return function(FunctionType.AVG, columnName);
    }

    public SqlQueryBuilder sum(String columnName) {
	return function(FunctionType.SUM, columnName);
    }

    private SqlQueryBuilder function(FunctionType type, String columnName) {
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

    public SqlQueryBuilder as(String alias) {
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
