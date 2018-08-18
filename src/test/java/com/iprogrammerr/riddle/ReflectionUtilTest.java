package com.iprogrammerr.riddle;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;

import com.iprogrammerr.riddle.model.security.Activator;
import com.iprogrammerr.riddle.util.ReflectionUtil;

public class ReflectionUtilTest {

    @Test
    public void getGettersTest() {
	Activator activator = new Activator(1, "adada");
	List<Method> getters = ReflectionUtil.getAllGetters(Activator.class);
	for (Method getter : getters) {
	    System.out.println(getter.getName());
	}
    }
}
