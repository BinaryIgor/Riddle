package com.iprogrammerr.riddle.service.crud;

import com.iprogrammerr.riddle.database.QueryBuilder;
import com.iprogrammerr.riddle.database.QueryExecutor;
import com.iprogrammerr.riddle.database.QueryResultToObjectConverter;
import com.iprogrammerr.riddle.model.database.User;
import com.iprogrammerr.riddle.model.database.UserRole;
import com.iprogrammerr.riddle.model.json.UserProfile;

public class UserService {

    private static final String QUERY_USER_ROLE_PREFIX = "r";
    private QueryExecutor queryExecutor;

    public UserService(QueryExecutor queryExecutor) {
	this.queryExecutor = queryExecutor;
    }

    public long createUser(User user) {
	QueryBuilder queryBuilder = new QueryBuilder();
	queryBuilder.insertInto("user").keys("email", "name", "password", "user_role_id").values(user.getEmail(),
		user.getPassword(), user.getUserRole().getId());
	return queryExecutor.executeCreate(queryBuilder.build());
    }

    public void activateUser(long id) {
	queryExecutor
		.executeUpdate(new QueryBuilder().update("user").set("active", true).where("id").isEqualTo(id).build());
    }

    public User getUser(long id) {
	QueryBuilder queryBuilder = new QueryBuilder();
	queryBuilder.select("user.*", "user_role.id as rid", "user_role.name as rname").from("user")
		.innerJoin("user_role").on("user_role_id").isEqualTo("user_role.id").where("user.id").isEqualTo(id);
	return queryExecutor.executeSelect(queryBuilder.build(), getQueryToUserConverter());
    }

    public User getUserByName(String name) {
	return getUserByEmailOrName(name, false);
    }

    public User getUserByEmail(String email) {
	return getUserByEmailOrName(email, true);
    }

    // TODO unmock mocked points!
    public UserProfile getUserProfile(long id) {
	QueryBuilder queryBuilder = new QueryBuilder();
	queryBuilder.select("name", "email").from("user").where("id").isEqualTo(id);
	return queryExecutor.executeSelect(queryBuilder.build(), queryResult -> {
	    return new UserProfile(queryResult.getString("email"), queryResult.getString("name"), 22);
	});
    }

    private User getUserByEmailOrName(String emailOrName, boolean email) {
	QueryBuilder queryBuilder = new QueryBuilder();
	queryBuilder.select("user.*", "user_role.id as rid", "user_role.name as rname").from("user")
		.innerJoin("user_role").on("user_role_id").isEqualTo("user_role.id");
	if (email) {
	    queryBuilder.where("user.email").isEqualTo(emailOrName);
	} else {
	    queryBuilder.where("user.name").isEqualTo(emailOrName);
	}
	return queryExecutor.executeSelect(queryBuilder.build(), getQueryToUserConverter());
    }

    public User getUserByNameOrEmail(String nameOrEmail) {
	if (nameOrEmail.contains("@")) {
	    return getUserByEmail(nameOrEmail);
	}
	return getUserByName(nameOrEmail);
    }

    public UserRole getUserRoleByName(String name) {
	QueryBuilder queryBuilder = new QueryBuilder();
	queryBuilder.select("*").from("user_role").where("name").isEqualTo(name);
	return queryExecutor.executeSelect(queryBuilder.build(), resultSet -> {
	    return new UserRole(resultSet.getLong("id"), resultSet.getString("name"));
	});
    }

    public boolean existsByEmail(String email) {
	return queryExecutor.executeSelect(
		new QueryBuilder().select("id").from("user").where("email").isEqualTo(email).build(), resultSet -> {
		    return resultSet.getLong("id") > 0;
		});
    }

    public boolean existsByName(String name) {
	return queryExecutor.executeSelect(
		new QueryBuilder().select("id").from("user").where("name").isEqualTo(name).build(), resultSet -> {
		    return resultSet.getLong("id") > 0;
		});
    }

    private QueryResultToObjectConverter<User> getQueryToUserConverter() {
	return (resultSet) -> {
	    UserRole userRole = new UserRole(resultSet.getLong(QUERY_USER_ROLE_PREFIX + "id"),
		    resultSet.getString(QUERY_USER_ROLE_PREFIX + "name"));
	    return new User(resultSet.getLong("id"), resultSet.getString("email"), resultSet.getString("name"),
		    resultSet.getString("password"), resultSet.getBoolean("active"), userRole);
	};
    }

}
