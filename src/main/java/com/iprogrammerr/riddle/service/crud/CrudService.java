package com.iprogrammerr.riddle.service.crud;

import java.util.List;

import javax.transaction.Transactional;

import com.iprogrammerr.riddle.dao.Dao;

@Transactional
public abstract class CrudService<Entity> {

    private Dao<Entity> dao;

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
