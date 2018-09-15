package com.iprogrammerr.riddle.user;

public class MockedUserProfile implements UserProfile {

    @Override
    public int points() {
	return -99;
    }

    @Override
    public byte[] image() {
	return new byte[0];
    }

}
