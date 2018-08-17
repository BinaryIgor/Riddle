package com.iprogrammerr.riddle.server;

import java.util.List;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.iprogrammerr.riddle.database.DatabaseConnectionManager;
import com.iprogrammerr.riddle.router.Route;
import com.iprogrammerr.riddle.router.Router;
import com.iprogrammerr.riddle.service.security.SecurityService;

public class JettyServer {

    private final Server server = new Server();
    private int port;
    private DatabaseConnectionManager connectionManager;

    public JettyServer(int port) {
	this.port = port;
    }

    public JettyServer(int port, DatabaseConnectionManager connectionManager) {
	this.port = port;
	this.connectionManager = connectionManager;
    }

    public void start(String contextPath, List<Route> routes, SecurityService securityService) throws Exception {
	ServerConnector connector = new ServerConnector(server);
	connector.setPort(port);
	server.setConnectors(new Connector[] { connector });
	ServletContextHandler contextHandler = new ServletContextHandler();
	contextHandler.setContextPath("/" + contextPath + "/");
	Router router = (Router) contextHandler.addServlet(Router.class, "/*").getServlet();
	router.init(contextPath, routes, securityService);
	server.setHandler(contextHandler);
	server.start();
    }

    public void stop() throws Exception {
	if (connectionManager != null) {
	    connectionManager.close();
	}
	server.stop();
    }
}
