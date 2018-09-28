package com.iprogrammerr.riddle.database;

import java.util.ArrayList;
import java.util.List;

import com.iprogrammerr.bright.server.model.KeyValue;
import com.iprogrammerr.riddle.model.Record;

public final class SqlQueryTemplate implements QueryTemplate {

    private final char paramSign;

    public SqlQueryTemplate() {
	this('?');
    }

    public SqlQueryTemplate(char paramSign) {
	this.paramSign = paramSign;
    }

    private List<Integer> readParamsIndexes(String template) {
	List<Integer> paramsIndexes = new ArrayList<>();
	char[] templateChars = template.toCharArray();
	for (int i = 0; i < templateChars.length; i++) {
	    if (templateChars[i] == this.paramSign) {
		paramsIndexes.add(i);
	    }
	}
	return paramsIndexes;
    }

    private String toStringValue(Object value) {
	if (String.class.isAssignableFrom(value.getClass())) {
	    return "'" + value + "'";
	}
	return value.toString();
    }

    @Override
    public String query(String template, Object... values) throws Exception {
	List<Integer> paramsIndexes = readParamsIndexes(template);
	if (paramsIndexes.size() != values.length) {
	    throw new Exception("Incorrect number of " + paramSign + " signes to given values");
	}
	StringBuilder queryBuilder = new StringBuilder();
	int beginIndex = 0;
	for (int i = 0; i < paramsIndexes.size(); i++) {
	    queryBuilder.append(template.substring(beginIndex, paramsIndexes.get(i))).append(toStringValue(values[i]));
	    beginIndex = paramsIndexes.get(i) + 1;
	}
	queryBuilder.append(template.substring(paramsIndexes.get(paramsIndexes.size() - 1) + 1));
	return queryBuilder.toString();
    }

    @Override
    public String insert(Record table) throws Exception {
	StringBuilder builder = new StringBuilder();
	builder.append("insert into ").append(table.name()).append(" (");
	List<KeyValue> columns = table.columns();
	if (columns.isEmpty()) {
	    throw new Exception("Table is empty");
	}
	StringBuilder keysBuilder = new StringBuilder();
	StringBuilder valuesBuilder = new StringBuilder();
	KeyValue column = columns.get(0);
	keysBuilder.append(column.key());
	valuesBuilder.append(toStringValue(column.value()));
	for (int i = 1; i < columns.size(); i++) {
	    column = columns.get(i);
	    keysBuilder.append(", ").append(column.key());
	    valuesBuilder.append(", ").append(toStringValue(column.value()));
	}
	keysBuilder.append(")");
	valuesBuilder.append(")");
	return builder.append(keysBuilder).append(" values (").append(valuesBuilder).toString();
    }

    @Override
    public String update(Record table, String whereTemplate, Object... values) throws Exception {
	StringBuilder builder = new StringBuilder();
	builder.append("update ").append(table.name()).append(" set ");
	List<KeyValue> columns = table.columns();
	if (columns.isEmpty()) {
	    throw new Exception("Table is empty");
	}
	KeyValue column = columns.get(0);
	builder.append(column.key()).append("=").append(toStringValue(column.value()));
	for (int i = 1; i < columns.size(); i++) {
	    column = columns.get(i);
	    builder.append(", ").append(column.key()).append("=").append(toStringValue(column.value()));
	}
	return builder.append(" where ").append(query(whereTemplate, values)).toString();
    }

}
