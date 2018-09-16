package com.iprogrammerr.riddle.users;

import com.iprogrammerr.riddle.database.DatabaseSession;

public class DatabaseUsersRoles implements UsersRoles {

    private final DatabaseSession session;

    public DatabaseUsersRoles(DatabaseSession session) {
	this.session = session;
    }

    @Override
    public long playerId() throws Exception {
	return session.select("select id from user_role where name = 'player'", resultSet -> resultSet.getLong(1));
    }

    @Override
    public long adminId() throws Exception {
	return session.select("select id from user_role where name = 'admin'", resultSet -> resultSet.getLong(1));
    }

}
