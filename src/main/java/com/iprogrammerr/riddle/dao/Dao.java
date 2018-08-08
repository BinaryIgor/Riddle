package com.iprogrammerr.riddle.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class Dao<Entity> {

    private Class<Entity> clazz;
    private SessionFactory sessionFactory;

    public Dao(Class<Entity> clazz, SessionFactory sessionFactory) {
	this.clazz = clazz;
	this.sessionFactory = sessionFactory;
    }

    public long create(Entity entity) {
	Session session = sessionFactory.getCurrentSession();
	return (long) session.save(entity);
    }

    public void update(Entity entity) {
	Session session = sessionFactory.getCurrentSession();
	session.update(entity);
    }

    public void delete(Entity entity) {
	Session session = sessionFactory.getCurrentSession();
	session.delete(entity);
    }

    public Entity get(long id) {
	Session session = sessionFactory.getCurrentSession();
	return session.get(clazz, id);
    }

    public List<Entity> get() {
	return get("id");
    }

    public List<Entity> get(String toSortFieldName) {
	Session session = sessionFactory.getCurrentSession();
	String queryString = "from " + clazz.getName() + " order by " + toSortFieldName;
	Query<Entity> query = session.createQuery(queryString, clazz);
	return query.getResultList();
    }
}
