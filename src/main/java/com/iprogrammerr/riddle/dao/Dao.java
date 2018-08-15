package com.iprogrammerr.riddle.dao;

import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class Dao<Entity> {

    private Class<Entity> clazz;
    protected SessionFactory sessionFactory;

    public Dao(Class<Entity> clazz, SessionFactory sessionFactory) {
	this.clazz = clazz;
	this.sessionFactory = sessionFactory;
    }

    public long create(Entity entity) {
	Session session = sessionFactory.openSession();
	try {
	    return (long) session.save(entity);
	} catch (Exception exception) {
	    throw exception;
	} finally {
	    session.close();
	}
    }

    public void update(Entity entity) {
	Session session = sessionFactory.openSession();
	try {
	    session.update(entity);
	} catch (Exception exception) {
	    throw exception;
	} finally {
	    session.close();
	}
    }

    public void delete(Entity entity) {
	Session session = sessionFactory.openSession();
	try {
	    session.delete(entity);
	} catch (Exception exception) {
	    throw exception;
	} finally {
	    session.close();
	}
    }

    public Entity get(long id) {
	Session session = sessionFactory.openSession();
	Entity entity = null;
	try {
	    entity = session.get(clazz, id);
	} catch (Exception exception) {
	    exception.printStackTrace();
	    throw exception;
	} finally {
	    session.close();
	}
	if (entity == null) {
	    throw new NoResultException();
	}
	return entity;
    }

    public List<Entity> get() {
	return get("id");
    }

    public List<Entity> get(String toSortFieldName) {
	Session session = sessionFactory.openSession();
	try {
	    String queryString = "from " + clazz.getName() + " order by " + toSortFieldName;
	    Query<Entity> query = session.createQuery(queryString, clazz);
	    return query.getResultList();
	} catch (Exception exception) {
	    throw exception;
	} finally {
	    session.close();
	}
    }

}
