package com.iprogrammerr.riddle;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;

import com.iprogrammerr.riddle.model.security.Activator;
import com.iprogrammerr.riddle.util.ReflectionUtil;

public class ReflectionUtilTest {

    @Test
    public void getGettersTest() {
	List<Method> getters = ReflectionUtil.getAllGetters(Activator.class);
	assertTrue(getters.size() >= 2);
    }

    @Test
    public void getAccessibleFieldsTest() {
	Activator activator = new Activator(1, "hash");
	List<Object> fields = ReflectionUtil.getAllAccessibleFields(activator);
	assertTrue(fields.size() >= 2);
    }
}
