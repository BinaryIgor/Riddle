package com.iprogrammerr.riddle;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Validation;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.iprogrammerr.riddle.dao.UserDao;
import com.iprogrammerr.riddle.entity.User;
import com.iprogrammerr.riddle.entity.UserRole;
import com.iprogrammerr.riddle.router.Route;
import com.iprogrammerr.riddle.router.UserRoute;
import com.iprogrammerr.riddle.server.JettyServer;
import com.iprogrammerr.riddle.service.crud.UserService;
import com.iprogrammerr.riddle.service.json.JsonService;
import com.iprogrammerr.riddle.service.security.SecurityService;
import com.iprogrammerr.riddle.service.validation.ValidationService;

public class RiddleApplication {

    private static final int SERVER_PORT = 8080;
    public static final String CONTEXT_PATH = "riddle";

    public static void main(String[] args) throws Exception {
	SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(User.class)
		.addAnnotatedClass(UserRole.class).buildSessionFactory();
	List<Route> routes = new ArrayList<>();

	UserDao userDao = new UserDao(factory);

	UserService userService = new UserService(userDao);
	ValidationService validationService = new ValidationService(
		Validation.buildDefaultValidatorFactory().getValidator());
	JsonService jsonService = new JsonService();
	SecurityService securityService = new SecurityService(userService);

	routes.add(new UserRoute(userService, validationService, securityService, jsonService));

	JettyServer server = new JettyServer(SERVER_PORT, factory);
	server.start(CONTEXT_PATH, routes, securityService);
    }

}
