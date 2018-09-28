package com.iprogrammerr.riddle.database;

import java.util.List;

import com.iprogrammerr.bright.server.model.KeyValue;
import com.iprogrammerr.bright.server.model.KeysValues;
import com.iprogrammerr.bright.server.model.StringsObjects;
import com.iprogrammerr.riddle.model.Record;

public final class DatabaseRecord implements Record {

    private final String name;
    private final KeysValues columns;

    public DatabaseRecord(String name) {
	this.name = name;
	this.columns = new StringsObjects();
    }

    public DatabaseRecord(String name, KeysValues columns) {
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
	return this.columns.keysValues();
    }

    @Override
    public String name() {
	return this.name;
    }
}
