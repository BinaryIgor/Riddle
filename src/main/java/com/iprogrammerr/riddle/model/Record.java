package com.iprogrammerr.riddle.model;

import java.util.List;

public interface Record {

    Record put(String key, Object value);

    List<KeyValue> columns();

    String name();
}
