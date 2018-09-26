package com.iprogrammerr.riddle.database;

import java.util.List;
import java.util.Map;

import com.iprogrammerr.bright.server.model.KeyValue;
import com.iprogrammerr.riddle.model.Record;

public final class RemappedDatabaseRecord implements Record {

    private final Record base;
    private final Map<String, String> remappings;

    public RemappedDatabaseRecord(String name, Map<String, String> remappings) {
	this(new DatabaseRecord(name), remappings);
    }

    public RemappedDatabaseRecord(Record base, Map<String, String> remappings) {
	this.base = base;
	this.remappings = remappings;
    }

    @Override
    public Record put(String key, Object value) {
	if (remappings.containsKey(key)) {
	    key = remappings.get(key);
	}
	return base.put(key, value);
    }

    @Override
    public List<KeyValue> columns() {
	return base.columns();
    }

    @Override
    public String name() {
	return base.name();
    }

}
