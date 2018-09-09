package com.iprogrammerr.riddle.model.database;

import java.util.List;

import com.iprogrammerr.bright.server.model.KeyValue;
import com.iprogrammerr.bright.server.model.KeysValues;
import com.iprogrammerr.bright.server.model.StringsObjects;

public class DatabaseTable implements Table {

    private final String name;
    private final KeysValues columns;

    public DatabaseTable(String name) {
	this.name = name;
	this.columns = new StringsObjects();
    }

    public DatabaseTable(String name, KeysValues columns) {
	this.name = name;
	this.columns = columns;
    }

    @Override
    public Table put(String key, Object value) {
	columns.add(key, value);
	return this;
    }

    @Override
    public List<KeyValue> columns() {
	return columns.keysValues();
    }

    @Override
    public String name() {
	return name;
    }

}
