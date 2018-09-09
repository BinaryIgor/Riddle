package com.iprogrammerr.riddle.model.database;

import java.util.List;

import com.iprogrammerr.bright.server.model.KeyValue;

public interface Table {

    Table put(String key, Object value);

    List<KeyValue> columns();

    String name();
}
