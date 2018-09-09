package com.iprogrammerr.riddle.service.crud;

import com.iprogrammerr.riddle.database.SqlQueryBuilder;
import com.iprogrammerr.riddle.database.SqlDatabaseSession;
import com.iprogrammerr.riddle.database.QueryResult;
import com.iprogrammerr.riddle.model.database.User;
import com.iprogrammerr.riddle.model.database.UserRole;
import com.iprogrammerr.riddle.model.json.UserProfile;

public class UserService {

    private static final String QUERY_USER_ROLE_PREFIX = "r";
    private SqlDatabaseSession queryExecutor;

    public UserService(SqlDatabaseSession queryExecutor) {
	this.queryExecutor = queryExecutor;
    }

    public long createUser(User user) {
	SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
	queryBuilder.insertInto("user").keys("email", "name", "password", "user_role_id").values(user.getEmail(),
		user.getPassword(), user.getUserRole().getId());
	return queryExecutor.create(queryBuilder.build());
    }

    public void activateUser(long id) {
	queryExecutor.update(
		new SqlQueryBuilder().update("user").set("active", true).where("id").isEqualToValue(id).build());
    }

    public User getUser(long id) {
	SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
	queryBuilder.select("user.*", "user_role.id as rid", "user_role.name as rname").from("user")
		.innerJoin("user_role").on("user_role_id").isEqualToColumn("user_role.id").where("user.id")
		.isEqualToValue(id);
	return queryExecutor.query(queryBuilder.build(), getQueryToUserConverter());
    }

    public User getUserByName(String name) {
	return getUserByEmailOrName(name, false);
    }

    public User getUserByEmail(String email) {
	return getUserByEmailOrName(email, true);
    }

    // TODO unmock mocked points!
    public UserProfile getUserProfile(long id) {
	SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
	queryBuilder.select("name", "email").from("user").where("id").isEqualToValue(id);
	return queryExecutor.select(queryBuilder.build(), queryResult -> {
	    return new UserProfile(queryResult.getString("email"), queryResult.getString("name"), 22);
	});
    }

    public UserProfile getUserProfile(String name) {
	SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
	queryBuilder.select("name", "email").from("user").where("id").isEqualToValue(name);
	return queryExecutor.select(queryBuilder.build(), queryResult -> {
	    return new UserProfile(queryResult.getString("email"), queryResult.getString("name"), 22);
	});
    }

    private User getUserByEmailOrName(String emailOrName, boolean email) {
	SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
	queryBuilder.select("user.*", "user_role.id as rid", "user_role.name as rname").from("user")
		.innerJoin("user_role").on("user_role_id").isEqualToColumn("user_role.id");
	if (email) {
	    queryBuilder.where("user.email").isEqualToValue(emailOrName);
	} else {
	    queryBuilder.where("user.name").isEqualToValue(emailOrName);
	}
	return queryExecutor.query(queryBuilder.build(), getQueryToUserConverter());
    }

    public User getUserByNameOrEmail(String nameOrEmail) {
	if (nameOrEmail.contains("@")) {
	    return getUserByEmail(nameOrEmail);
	}
	return getUserByName(nameOrEmail);
    }

    public UserRole getUserRoleByName(String name) {
	SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
	queryBuilder.select("*").from("user_role").where("name").isEqualToValue(name);
	return queryExecutor.select(queryBuilder.build(), resultSet -> {
	    return new UserRole(resultSet.getLong("id"), resultSet.getString("name"));
	});
    }

    public boolean existsByEmail(String email) {
	return queryExecutor.select(
		new SqlQueryBuilder().select("id").from("user").where("email").isEqualToColumn(email).build(),
		resultSet -> {
		    return resultSet.getLong("id") > 0;
		});
    }

    public boolean existsByName(String name) {
	return queryExecutor.select(
		new SqlQueryBuilder().select("id").from("user").where("name").isEqualToColumn(name).build(), resultSet -> {
		    return resultSet.getLong("id") > 0;
		});
    }

    private QueryResult<User> getQueryToUserConverter() {
	return (resultSet) -> {
	    UserRole userRole = new UserRole(resultSet.getLong(QUERY_USER_ROLE_PREFIX + "id"),
		    resultSet.getString(QUERY_USER_ROLE_PREFIX + "name"));
	    return new User(resultSet.getLong("id"), resultSet.getString("email"), resultSet.getString("name"),
		    resultSet.getString("password"), resultSet.getBoolean("active"), userRole);
	};
    }

}
