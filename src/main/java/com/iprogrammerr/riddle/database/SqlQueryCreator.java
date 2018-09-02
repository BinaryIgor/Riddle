package com.iprogrammerr.riddle.database;

import java.util.ArrayList;
import java.util.List;

import com.iprogrammerr.riddle.exception.database.QueryCreatorException;

public class SqlQueryCreator implements QueryCreator {

    private final char paramSign;

    public SqlQueryCreator() {
	this('?');
    }

    public SqlQueryCreator(char paramSign) {
	this.paramSign = paramSign;
    }

    @Override
    public String create(String template, Object... values) {
	List<Integer> paramsIndexes = readParamsIndexes(template);
	if (paramsIndexes.size() != values.length) {
	    throw new QueryCreatorException("Incorrect number of " + paramSign + " signes to given values");
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

    private List<Integer> readParamsIndexes(String template) {
	List<Integer> paramsIndexes = new ArrayList<>();
	char[] templateChars = template.toCharArray();
	for (int i = 0; i < templateChars.length; i++) {
	    if (templateChars[i] == paramSign) {
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

}
