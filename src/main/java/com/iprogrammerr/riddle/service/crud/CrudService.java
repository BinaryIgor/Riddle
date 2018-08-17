package com.iprogrammerr.riddle.service.crud;

import java.util.List;

import com.iprogrammerr.riddle.database.DatabaseConnectionManager;

public abstract class CrudService<Entity> {

    private String entityName;
    private DatabaseConnectionManager connectionManager;

    public CrudService(String entityName, DatabaseConnectionManager connectionManager) {
	this.entityName = entityName;
	this.connectionManager = connectionManager;
    }

    public void update(Entity entity) {

    }

    public void delete(Entity entity) {

    }

    public Entity get(long id) {
	return null;
    }

    public List<Entity> get() {
	return null;
    }
}
