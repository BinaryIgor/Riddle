package com.iprogrammerr.riddle;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.iprogrammerr.riddle.dao.Dao;
import com.iprogrammerr.riddle.entity.User;
import com.iprogrammerr.riddle.router.Route;
import com.iprogrammerr.riddle.router.UserRoute;
import com.iprogrammerr.riddle.server.JettyServer;
import com.iprogrammerr.riddle.service.JsonService;
import com.iprogrammerr.riddle.service.crud.UserService;

public class RiddleApplication {

    private final static int SERVER_PORT = 8080;

    public static void main(String[] args) throws Exception {
	SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(User.class)
		.buildSessionFactory();

	List<Route> routes = new ArrayList<>();
	Dao<User> userDao = new Dao<>(User.class, factory);
	UserService userService = new UserService(userDao);
	JsonService jsonService = new JsonService();

	routes.add(new UserRoute(userService, jsonService));

	JettyServer server = new JettyServer(SERVER_PORT);
	server.start("riddle", routes);
    }

}
