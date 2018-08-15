package com.iprogrammerr.riddle.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.iprogrammerr.riddle.entity.User;
import com.iprogrammerr.riddle.entity.UserRole;
import com.iprogrammerr.riddle.model.Field;

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
	} finally {
	    session.close();
	}
    }

    public UserRole getUserRoleByName(String name) {
	Session session = sessionFactory.openSession();
	try {
	    Query<UserRole> query = session.createQuery("from " + UserRole.class.getName() + " where name = :name",
		    UserRole.class);
	    query.setParameter("name", name);
	    return query.getSingleResult();
	} catch (Exception exception) {
	    throw exception;
	} finally {
	    session.close();
	}
    }

    public boolean existsByEmail(String email) {
	return exists(new Field<>("email", email));
    }

    public boolean existsByName(String name) {
	return exists(new Field<>("name", name));
    }

    public boolean exists(Field<String> field) {
	Session session = sessionFactory.openSession();
	try {
	    String statement = "select count(u) from user u where " + field.getKey() + " = :" + field.getKey();
	    Query<Long> query = session.createQuery(statement, Long.class);
	    query.setParameter(field.getKey(), field.getValue());
	    return query.getSingleResult() > 0;
	} catch (Exception exception) {
	    exception.printStackTrace();
	    return false;
	} finally {
	    session.close();
	}
    }

}
