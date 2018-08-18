package com.iprogrammerr.riddle.util;

public class StringUtil {

    public static boolean isNullOrEmpty(String string) {
	return string == null || string.isEmpty();
    }

    public static boolean isNullOrShorterThan(String string, int minLength) {
	return string == null || string.length() < minLength;
    }

    public static boolean isNullOrLenghtNotBetween(String string, int minLength, int maxLength) {
	return string == null || string.length() < minLength || string.length() > maxLength;
    }
}
