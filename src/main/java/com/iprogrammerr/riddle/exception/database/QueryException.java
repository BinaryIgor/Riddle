package com.iprogrammerr.riddle.exception.database;

public class QueryException extends RuntimeException {

    private QueryException(String message) {
	super(message);
    }

    public static QueryException createIncorrectKeysToValuesNumber() {
	return new QueryException("There is incorrect keys to values number");
    }

    public static QueryException createIncorrectKeysToValuesNumber(int keysNumber, int valuesNumber) {
	return new QueryException("There was " + keysNumber + "keys and " + valuesNumber + " values");
    }

    public static QueryException createIncorretKeyException() {
	return new QueryException("There was a value where key is expected");
    }
}
