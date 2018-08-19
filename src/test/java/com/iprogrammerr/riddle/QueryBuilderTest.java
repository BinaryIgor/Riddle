package com.iprogrammerr.riddle;

import org.junit.Test;

import com.iprogrammerr.riddle.database.QueryBuilder;

public class QueryBuilderTest {

    @Test
    public void selectTest() {
	QueryBuilder queryBuilder = new QueryBuilder();
	queryBuilder.select("user.*", "user_role.id as rid", "user_role.name as rname").from("user")
		.innerJoin("user_role").on("user.user_role_id").isEqualTo("user_role.id").where("id").isEqualTo(1);
	System.out.println(queryBuilder.build());
    }

    @Test
    public void insertTest() {
	QueryBuilder queryBuilder = new QueryBuilder();
	queryBuilder.insertInto("user", "name", "email", "password").values("Igor", "ceigor94@gmail.com", "alamakota");
	System.out.println(queryBuilder.build());
    }
}
