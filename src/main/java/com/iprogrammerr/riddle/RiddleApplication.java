package com.iprogrammerr.riddle;

import java.util.ArrayList;
import java.util.List;

import com.iprogrammerr.riddle.router.Route;
import com.iprogrammerr.riddle.router.UserRoute;
import com.iprogrammerr.riddle.server.JettyServer;
import com.iprogrammerr.riddle.service.JsonService;

public class RiddleApplication {

    private final static int SERVER_PORT = 8080;

    public static void main(String[] args) throws Exception {
	JettyServer server = new JettyServer(SERVER_PORT);
	server.start("riddle", getRoutes());
    }

    private static List<Route> getRoutes() {
	List<Route> routes = new ArrayList<>();
	JsonService jsonService = new JsonService();
	routes.add(new UserRoute(jsonService));
	return routes;
    }
}
