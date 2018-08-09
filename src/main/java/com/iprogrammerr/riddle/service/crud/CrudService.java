package com.iprogrammerr.riddle.service.crud;

import java.util.List;

import com.iprogrammerr.riddle.dao.Dao;

public abstract class CrudService<Entity> {

    protected Dao<Entity> dao;

    public CrudService(Dao<Entity> dao) {
	this.dao = dao;
    }

    public long create(Entity entity) {
	return dao.create(entity);
    }

    public void update(Entity entity) {
	dao.update(entity);
    }

    public void delete(Entity entity) {
	dao.delete(entity);
    }

    public Entity get(long id) {
	return dao.get(id);
    }

    public List<Entity> get() {
	return dao.get();
    }
}
