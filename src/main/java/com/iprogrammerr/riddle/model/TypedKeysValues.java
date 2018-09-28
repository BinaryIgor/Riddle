package com.iprogrammerr.riddle.model;

import java.util.List;

import com.iprogrammerr.bright.server.model.KeyValue;

public interface TypedKeysValues {

    TypedKeysValues put(String key, Object value);

    boolean has(String key);

    boolean booleanValue(String key) throws Exception;

    int intValue(String key) throws Exception;

    long longValue(String key) throws Exception;

    String stringValue(String key) throws Exception;

    float floatValue(String key) throws Exception;

    double doubleValue(String key) throws Exception;

    List<KeyValue> keysValues();
}
