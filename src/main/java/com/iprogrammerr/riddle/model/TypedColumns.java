package com.iprogrammerr.riddle.model;

import java.util.List;

import com.iprogrammerr.bright.server.model.KeyValue;
import com.iprogrammerr.bright.server.model.KeysValues;
import com.iprogrammerr.bright.server.model.StringsObjects;

public final class TypedColumns implements TypedKeysValues {

    private final KeysValues base;

    public TypedColumns(KeysValues base) {
	this.base = base;
    }

    public TypedColumns() {
	this.base = new StringsObjects();
    }

    @Override
    public TypedKeysValues put(String key, Object value) {
	base.put(key, value);
	return this;
    }

    @Override
    public boolean has(String key) {
	return base.has(key, Object.class);
    }

    @Override
    public boolean booleanValue(String key) throws Exception {
	return base.value(key, Boolean.class);
    }

    @Override
    public int intValue(String key) throws Exception {
	return base.value(key, Integer.class);
    }

    @Override
    public long longValue(String key) throws Exception {
	return base.value(key, Long.class);
    }

    @Override
    public String stringValue(String key) throws Exception {
	return base.value(key, String.class);
    }

    @Override
    public float floatValue(String key) throws Exception {
	return base.value(key, Float.class);
    }

    @Override
    public double doubleValue(String key) throws Exception {
	return base.value(key, Double.class);
    }

    @Override
    public List<KeyValue> keysValues() {
	return base.keysValues();
    }

}
