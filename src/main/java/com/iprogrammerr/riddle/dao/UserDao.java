package com.iprogrammerr.riddle.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.iprogrammerr.riddle.entity.User;

public class UserDao extends Dao<User> {

    public UserDao(SessionFactory sessionFactory) {
	super(User.class, sessionFactory);
    }

    public User getUserByName(String name) {
	Session session = sessionFactory.openSession();
	try {
	    Query<User> query = session.createQuery("from user where name = :name", User.class);
	    query.setParameter("name", name);
	    return query.getSingleResult();
	} catch (Exception exception) {
	    throw exception;
	} finally {
	    session.close();
	}

    }

    public User getUserByEmail(String email) {
	Session session = sessionFactory.openSession();
	try {
	    Query<User> query = session.createQuery("from user where email = :email", User.class);
	    query.setParameter("email", email);
	    return query.getSingleResult();
	} catch (Exception exception) {
	    throw exception;
	}
    }
}
