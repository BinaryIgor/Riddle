package com.iprogrammerr.riddle.database;

import java.util.List;

import com.iprogrammerr.riddle.model.Columns;
import com.iprogrammerr.riddle.model.KeyValue;
import com.iprogrammerr.riddle.model.Record;
import com.iprogrammerr.riddle.model.TypedMap;

public final class DatabaseRecord implements Record {

    private final String name;
    private final TypedMap columns;

    public DatabaseRecord(String name) {
	this(name, new Columns());
    }

    public DatabaseRecord(String name, TypedMap columns) {
	this.name = name;
	this.columns = columns;
    }

    @Override
    public Record put(String key, Object value) {
	this.columns.put(key, value);
	return this;
    }

    @Override
    public List<KeyValue> columns() {
	return this.columns.keyValues();
    }

    @Override
    public String name() {
	return this.name;
    }
}
