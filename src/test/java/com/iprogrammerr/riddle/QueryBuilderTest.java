package com.iprogrammerr.riddle;

import org.junit.Before;
import org.junit.Test;

import com.iprogrammerr.riddle.database.QueryBuilder;

public class QueryBuilderTest {

    private QueryBuilder queryBuilder;

    @Before
    public void setup() {
	queryBuilder = new QueryBuilder();
    }

    @Test
    public void selectTest() {
	queryBuilder.select("user.*", "user_role.id as rid", "user_role.name as rname").from("user")
		.innerJoin("user_role").on("user.user_role_id").isEqualTo("user_role.id").where("id").isEqualTo(1);
	System.out.println(queryBuilder.build());
    }

    @Test
    public void insertTest() {
	queryBuilder.insertInto("user").keys("name", "email", "password").values("Igor", "ceigor94@gmail.com",
		"alamakota");
	System.out.println(queryBuilder.build());
    }

    @Test
    public void updateTest() {
	queryBuilder.update("user").set("name", "igor", "active", true, "password", "dupak", "email", "szczupak")
		.where("user").isEqualTo(1);
	System.out.println(queryBuilder.build());
    }

    @Test
    public void deleteTest() {
	queryBuilder.deleteFrom("user").where("name").like("ig");
	System.out.println(queryBuilder.build());
    }

    @Test
    public void joinTest() {
	queryBuilder.select("*").from("user").fullJoin("user_role").on("user.user_role_id").isEqualTo("user_role.id")
		.where("user.id").isEqualTo(1);
	System.out.println(queryBuilder.build());
    }
}
